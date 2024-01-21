package net.jacob.bygonecreatures.entity.custom;

import net.jacob.bygonecreatures.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.scores.Team;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

import java.util.function.Predicate;

import static net.jacob.bygonecreatures.item.ModItems.*;

public class RaptorEntity extends TamableAnimal implements IAnimatable {

    //prevents tamable entities from attacking other tamed ones. ALWAYS INCLUDE FOR MODDED TAMAMBLES

    public boolean wantsToAttack(LivingEntity entity, LivingEntity livingentity) {
        if (!(entity instanceof Creeper) && !(entity instanceof Ghast)) {
            if (entity instanceof Wolf) {
                Wolf wolf = (Wolf)entity;
                return !wolf.isTame() || wolf.getOwner() != livingentity;
            } else if (entity instanceof Player && livingentity instanceof Player && !((Player)livingentity).canHarmPlayer((Player)entity)) {
                return false;
            } else if (entity instanceof AbstractHorse && ((AbstractHorse)entity).isTamed()) {
                return false;
            } else {
                return !(entity instanceof TamableAnimal) || !((TamableAnimal)entity).isTame();
            }
        } else {
            return false;
        }
    }

    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(RaptorEntity.class, EntityDataSerializers.INT);

    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
        return entitytype == EntityType.CHICKEN || entitytype == EntityType.RABBIT || entitytype == EntityType.PIG;
    };
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(RaptorEntity.class, EntityDataSerializers.BOOLEAN);

    private int meatFeedings;




    public RaptorEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    @Override
    public void awardKillScore(Entity killedEntity, int i, DamageSource damageSource) {
        super.awardKillScore(killedEntity, i, damageSource);
        this.getEntityData().set(ATTACK_TICK, 0);

    }





    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(5, new NonTameRandomTargetGoal<>(this, Animal.class, false, PREY_SELECTOR));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        //this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, DodoEntity.class, false));
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this)).setAlertOthers());
    }

//    public boolean isControlledByLocalInstance() {
//        return false;
//    }

    public boolean doHurtTarget(Entity entity) {

        ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0), this);
        startAttackAnim();
        return super.doHurtTarget(entity);

    }








    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        return ModEntityTypes.RAPTOR.get().create(level);
    }


    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide()) {
            if (this.getAttackTick() > 0) this.getEntityData().set(ATTACK_TICK, this.getAttackTick() - 1);


        }

    }

    public int getAttackTick() {
        return this.getEntityData().get(ATTACK_TICK);
    }

    public void startAttackAnim() {
        this.getEntityData().set(ATTACK_TICK, 20);
    }






    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving() && !(this.getAttackTick() > 0) && !(this.isSitting()) && !(this.isTame())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (event.isMoving() && !(this.getAttackTick() > 0) && this.isTame() && !(this.isSitting())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.getAttackTick() > 0){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sitting", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }



    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                4, this::predicate));

    }






    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 0.15F, 1.0F);
    }

//    protected SoundEvent getAmbientSound() {
//        return SoundEvents.CAT_STRAY_AMBIENT;
//    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.DOLPHIN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

    /* TAMEABLE */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.mobInteract(player, hand);
        Item itemForTaming = Items.MUTTON;

        if(isFood(itemstack)) {
            return super.mobInteract(player, hand);
        }

        if (isTame() && itemstack.is(Items.MUTTON)) {
            if (this.getHealth() < this.getMaxHealth()) {
                this.usePlayerItem(player, hand, itemstack);
                this.gameEvent(GameEvent.EAT);
                this.playSound(SoundEvents.STRIDER_EAT, this.getSoundVolume(), this.getVoicePitch());
                this.heal(5);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;

        }








        if (!isTame() && item == Items.MUTTON) {
            this.usePlayerItem(player, hand, itemstack);
            this.gameEvent(GameEvent.EAT);
            this.playSound(SoundEvents.STRIDER_EAT, this.getSoundVolume(), this.getVoicePitch());
            meatFeedings++;
            if (meatFeedings > 9) {
                this.tame(player);
                this.level.broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level.broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        }


        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }




//        if (itemstack.getItem() == itemForTaming) {
//            return InteractionResult.PASS;
//        }

        return super.mobInteract(player, hand);
    }



    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSitting(tag.getBoolean("isSitting"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isSitting", this.isSitting());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
        this.getEntityData().define(ATTACK_TICK, 0);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public Team getTeam() {
        return super.getTeam();
    }

    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.getItem() == DODOMEAT.get();
    }


    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(2D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.4f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(1.5D);
            getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((double)0.1f);
        }
    }

    @Override
    public int getMaxHeadYRot() {
        return 10;
    }

    @Override
    public int getMaxHeadXRot() {
        return 10;
    }

}
