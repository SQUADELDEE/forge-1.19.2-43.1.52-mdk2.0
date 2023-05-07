package net.jacob.bygonecreatures.entity.custom;

import com.google.common.collect.Maps;
import com.mojang.math.Vector3f;
import net.jacob.bygonecreatures.item.ModItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Map;
import java.util.function.Predicate;

import static net.minecraft.world.entity.animal.Turtle.FOOD_ITEMS;

public class ArmoredFishEntity extends AbstractFish implements IAnimatable {

    private final Map<String, Vector3f> modelRotationValues = Maps.newHashMap();

    public Map<String, Vector3f> getModelRotationValues() {
        return this.modelRotationValues;
    }

    public static final Predicate<LivingEntity> PREY_SELECTOR = (p_30437_) -> {
        EntityType<?> entitytype = p_30437_.getType();
        return entitytype == EntityType.SQUID;
    };
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public ArmoredFishEntity(EntityType<? extends AbstractFish> type, Level worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.maxUpStep = 1.0F;
}


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));

        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.5, 10));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, ItchyEntity.class, 8.0F, 2.2D, 2.2D));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));

    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.7D).add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.ATTACK_DAMAGE, 3.0f)
                .add(Attributes.ATTACK_SPEED, 1.0f);
    }





    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }


    public void tick() {
        super.tick();


        if (this.level.isClientSide && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.03D) {
            Vec3 vec3 = this.getViewVector(0.0F);
            float f = Mth.cos(this.getYRot() * ((float) Math.PI / 180F)) * 0.3F;
            float f1 = Mth.sin(this.getYRot() * ((float) Math.PI / 180F)) * 0.3F;

        }

    }









    @Override
    public MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.isUnobstructed(this);
    }



    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.COD_FLOP;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.COD_HURT;
    }


    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }



    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("swim2", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("swim2", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));

    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    @Override
    public ItemStack getBucketItemStack() {
        ItemStack stack = new ItemStack(ModItems.ARMOREDFISHBUCKET.get());
        return stack;
    }
}
