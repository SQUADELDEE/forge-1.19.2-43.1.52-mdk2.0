package net.jacob.bygonecreatures.entity.custom;

import net.jacob.bygonecreatures.entity.ModEntityTypes;
import net.jacob.bygonecreatures.entity.ai.TameableAIRide;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.scores.Team;
import software.bernie.geckolib3.core.AnimationState;
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
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import static net.jacob.bygonecreatures.item.ModItems.GLYPTODONMEAT;
import static net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED;

public class BearEntity extends TamableAnimal implements IAnimatable {

    public static final EntityDataAccessor<Boolean> KNOCKED_OUT = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CHASING = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.BOOLEAN);


    private int meatFeedings;


    private static final EntityDataAccessor<Integer> ATTACK_TICK = SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.INT);

    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
        return entitytype == EntityType.FOX || entitytype == EntityType.RABBIT || entitytype == EntityType.PIG;
    };
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(BearEntity.class, EntityDataSerializers.BOOLEAN);




    public BearEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ATTACK_DAMAGE, 13.0f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(MOVEMENT_SPEED, 0.2f)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D);
    }




    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new BearMeleeAttackGoal(this, 2.0D, true));
        this.goalSelector.addGoal(3, new BearEntity.SleepGoal(200));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new TameableAIRide(this, 3D));
        this.targetSelector.addGoal(3, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));

        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
//        this.targetSelector.addGoal(7, new ResetUniversalAngerTargetGoal<>(this, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PeccaryEntity.class, false));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());

    }

//    prevents tamable entities from attacking other tamed ones. ALWAYS INCLUDE FOR MODDED TAMAMBLES
//
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






    public boolean isControlledByLocalInstance() {
        return false;
    }

    public boolean isChasing() {
        return this.entityData.get(CHASING);
    }

    public void setChasing(boolean chasing) {
        this.entityData.set(CHASING, chasing);
    }

    public class BearMeleeAttackGoal extends MeleeAttackGoal {

        public BearMeleeAttackGoal(PathfinderMob Mob, double SpeedModifier, boolean FollowingTargetEvenIfNotSeen) {
            super(Mob, SpeedModifier, FollowingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return mob.getMainHandItem().isEmpty() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return mob.getMainHandItem().isEmpty() && super.canContinueToUse();
        }

        @Override
        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return pAttackTarget instanceof AbstractSchoolingFish ? super.getAttackReachSqr(pAttackTarget) : 4.0F + pAttackTarget.getBbWidth();
        }

        public void stop() {
            BearEntity.this.setChasing(false);

        }

        public void start() {

            BearEntity.this.setChasing(true);

        }
    }


//    public static boolean checkBearSpawnRules(EntityType<BearEntity> type, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
//        return isBrightEnoughToSpawn(level, pos);
//
//    }

    public boolean doHurtTarget(Entity entity) {
        if(this.isTame()){
            this.getOwner().addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200));
        }
//        ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0), this);
        startAttackAnim();
        return super.doHurtTarget(entity);

    }










    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        return ModEntityTypes.BEAR.get().create(level);
    }


    @Override
    public void tick() {
        super.tick();

        if (!this.level.isClientSide()) {
            if (this.getAttackTick() > 0) this.getEntityData().set(ATTACK_TICK, this.getAttackTick() - 1);


        }

        if (this.isChasing()){
            spawnGroundEffects();
        }






    }

    public void spawnGroundEffects() {
        float radius = 0.3F;
        for (int i1 = 0; i1 < 3; i1++) {
            double motionX = getRandom().nextGaussian() * 0.07D;
            double motionY = getRandom().nextGaussian() * 0.07D;
            double motionZ = getRandom().nextGaussian() * 0.07D;
            float angle = (0.01745329251F * this.yBodyRot) + i1;
            double extraX = radius * Mth.sin((float) (Math.PI + angle));
            double extraY = 0.8F;
            double extraZ = radius * Mth.cos(angle);
            BlockPos ground = this.getBlockPosBelowThatAffectsMyMovement();
            BlockState BlockState = this.level.getBlockState(ground);
            if (BlockState.getMaterial() != Material.AIR && BlockState.getMaterial() != Material.WATER) {
                if (level.isClientSide) {
                    level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, BlockState), true, this.getX() + extraX, ground.getY() + extraY, this.getZ() + extraZ, motionX, motionY, motionZ);
                }
            }
        }
    }

    public int getAttackTick() {
        return this.getEntityData().get(ATTACK_TICK);
    }

    public void startAttackAnim() {
        this.getEntityData().set(ATTACK_TICK, 20);
    }






    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving() && !(this.getAttackTick() > 0) && !(this.hasControllingPassenger()) && !this.isChasing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (event.isMoving() && !(this.getAttackTick() > 0) && !(this.hasControllingPassenger()) && this.isChasing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isSleeping()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sleep", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (event.isMoving() && this.hasControllingPassenger() && !(this.getAttackTick() > 0)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("run", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.getAttackTick() > 0){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sleep", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (this.isKnockedOut()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sleep", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

//    private PlayState attackPredicate(AnimationEvent event) {
//
//        if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
//            event.getController().markNeedsReload();
//            event.getController().setAnimation(new AnimationBuilder().addAnimation("attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
//            this.swinging = false;
//        }
//
//        return PlayState.CONTINUE;
//    }




    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                4, this::predicate));
//        data.addAnimationController(new AnimationController(this, "attackController",
//                0, this::attackPredicate));

    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof Player) {
                Player player = (Player) passenger;
                return player;
            }
        }
        return null;
    }


    public class SleepGoal extends Goal {
        private final int countdownTime;
        private int countdown;



        protected boolean hasShelter() {
            BlockPos blockpos = new BlockPos(BearEntity.this.getX(), BearEntity.this.getBoundingBox().maxY, BearEntity.this.getZ());
            return !BearEntity.this.level.canSeeSky(blockpos);
//            && BearEntity.this.getWalkTargetValue(blockpos) >= 0.0F
        }


        public SleepGoal(int countdownTime) {
            this.countdownTime = countdownTime;
            this.countdown = BearEntity.this.random.nextInt(reducedTickDelay(countdownTime));
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        public boolean canUse() {
            if (BearEntity.this.xxa == 0.0F && BearEntity.this.yya == 0.0F && BearEntity.this.zza == 0.0F) {
                return this.canSleep() || BearEntity.this.isSleeping();
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return this.canSleep();
        }

        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            } else {
                return this.hasShelter();
//                BearEntity.this.level.isNight();
            }
        }

        public void stop() {
            BearEntity.this.setSleeping(false);
            this.countdown = BearEntity.this.random.nextInt(this.countdownTime);
        }

        public void start() {
            BearEntity.this.setJumping(false);
            BearEntity.this.setSleeping(true);
            BearEntity.this.getNavigation().stop();
            BearEntity.this.getMoveControl().setWantedPosition(BearEntity.this.getX(), BearEntity.this.getY(), BearEntity.this.getZ(), 0.0D);
        }
    }




    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.POLAR_BEAR_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.POLAR_BEAR_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
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
        Item itemForTaming = Items.BEEF;

        if(isFood(itemstack)) {
            return super.mobInteract(player, hand);
        }

        if (isTame() && itemstack.is(Items.BEEF)) {
            if (this.getHealth() < this.getMaxHealth()) {
                this.usePlayerItem(player, hand, itemstack);
                this.gameEvent(GameEvent.EAT);
                this.playSound(SoundEvents.STRIDER_EAT, this.getSoundVolume(), this.getVoicePitch());
                this.heal(5);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;

        }








        if (!isTame() && item == Items.BEEF && isKnockedOut()) {
            this.usePlayerItem(player, hand, itemstack);
            this.gameEvent(GameEvent.EAT);
            this.playSound(SoundEvents.STRIDER_EAT, this.getSoundVolume(), this.getVoicePitch());
            meatFeedings++;
            if (meatFeedings > 15) {
                this.tame(player);
                this.level.broadcastEntityEvent(this, (byte) 7);
                setKnockedOut(false);
                this.heal(20);
            } else {
                this.level.broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        }


        if(player.isShiftKeyDown() && isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
        if (interactionresult != InteractionResult.SUCCESS && type != InteractionResult.SUCCESS && isTame() && isOwnedBy(player) && !isFood(itemstack)){
            if(!player.isShiftKeyDown() && !this.isBaby() && !isSitting()){
                player.startRiding(this);
                return InteractionResult.SUCCESS;

            }
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
        tag.putBoolean("Sleeping", this.isSleeping());
    }


    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
        this.getEntityData().define(ATTACK_TICK, 0);
        this.entityData.define(SLEEPING, false);
        this.entityData.define(CHASING, false);
        this.entityData.define(KNOCKED_OUT, false);
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
        return pStack.getItem() == GLYPTODONMEAT.get();
    }


    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(13D);
            getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue (1.5D);
            getAttribute(MOVEMENT_SPEED).setBaseValue((double)0.2f);
        } else {
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0D);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(13D);
            getAttribute(Attributes.ATTACK_KNOCKBACK).setBaseValue (1.5D);
            getAttribute(MOVEMENT_SPEED).setBaseValue((double)0.2f);
        }
    }

    @Override
    public int getMaxHeadYRot() {
        return 30;
    }

    @Override
    public int getMaxHeadXRot() {
        return 30;
    }

    //A KNOCKOUT!!

    @Override
    public void die(DamageSource cause)
    {
        if (isTame() || isKnockedOut() || cause.getEntity() == null)
            super.die(cause);
        else // knockout Bears instead of killing them
        {
            setHealth(getMaxHealth() * 0.25f); // reset to 25% health
            setKnockedOut(true);
        }
    }

    @Override
    public boolean isImmobile()
    {
        return super.isImmobile() || isKnockedOut();
    }

    public boolean isKnockedOut()
    {
        return entityData.get(KNOCKED_OUT);
    }

    public void setKnockedOut(boolean b)
    {
        entityData.set(KNOCKED_OUT, b);
        BearEntity.this.getNavigation().stop();
        this.stunEffect();

    }

    private void stunEffect() {
        if (this.random.nextInt(6) == 0) {
            double d0 = this.getX() - (double)this.getBbWidth() * Math.sin((double)(this.yBodyRot * ((float)Math.PI / 180F))) + (this.random.nextDouble() * 0.6D - 0.3D);
            double d1 = this.getY() + (double)this.getBbHeight() - 0.3D;
            double d2 = this.getZ() + (double)this.getBbWidth() * Math.cos((double)(this.yBodyRot * ((float)Math.PI / 180F))) + (this.random.nextDouble() * 0.6D - 0.3D);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, d0, d1, d2, 0.4980392156862745D, 0.5137254901960784D, 0.5725490196078431D);
        }

    }







}
