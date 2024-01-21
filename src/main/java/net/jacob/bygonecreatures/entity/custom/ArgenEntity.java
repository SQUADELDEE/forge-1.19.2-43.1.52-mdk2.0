package net.jacob.bygonecreatures.entity.custom;
//
//import net.jacob.bygonecreatures.entity.ai.DragonBodyController;
//import net.jacob.bygonecreatures.entity.ai.DragonMoveController;
import net.jacob.bygonecreatures.entity.ai.DragonBodyController;
import net.jacob.bygonecreatures.entity.ai.DragonMoveController;
import net.jacob.bygonecreatures.entity.client.KeyMap;
import net.jacob.bygonecreatures.entity.util.BCConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.NotNull;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.minecraft.world.entity.ai.attributes.Attributes.*;

public class ArgenEntity extends TamableAnimal implements Saddleable, FlyingAnimal, PlayerRideable, IAnimatable {


    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private static final EntityDataAccessor<Boolean> SITTING =
            SynchedEntityData.defineId(ArgenEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving() && (this.isOnGround())) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

//        if (event.isMoving() && this.hasControllingPassenger() && !(this.isOnGround())) {
        if (isFlying()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("fly", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        if (isFlying() && event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("glide", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
////
//        if (this.isSitting()) {
//            event.getController().setAnimation(new AnimationBuilder().addAnimation("sit", ILoopType.EDefaultLoopTypes.LOOP));
//            return PlayState.CONTINUE;
//        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationEvent event) {

        if(this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bite", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }




    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                4, this::predicate));
        data.addAnimationController(new AnimationController(this, "attackController",
                0, this::attackPredicate));

    }


    // base attributes
    public static final double BASE_SPEED_GROUND = 0.3;
    public static final double BASE_SPEED_FLYING = 0.525;
    public static final double BASE_DAMAGE = 8;
    public static final double BASE_HEALTH = 60;
    public static final double BASE_FOLLOW_RANGE = 16;
    public static final double BASE_FOLLOW_RANGE_FLYING = BASE_FOLLOW_RANGE * 2;
    public static final int BASE_KB_RESISTANCE = 1;
    public static final float BASE_WIDTH = 2.75f; // adult sizes
    public static final float BASE_HEIGHT = 2.75f;
    public static final int BASE_REPRO_LIMIT = 2;
    public static final int BASE_GROWTH_TIME = 72000;
    public static final float BASE_SIZE_MODIFIER = 1.0f;

    // data value IDs
    private static final EntityDataAccessor<String> DATA_BREED = SynchedEntityData.defineId(ArgenEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_FLYING = SynchedEntityData.defineId(ArgenEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SADDLED = SynchedEntityData.defineId(ArgenEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_AGE = SynchedEntityData.defineId(ArgenEntity.class, EntityDataSerializers.INT);

    // data NBT IDs
    public static final String NBT_BREED = "Breed";
    private static final String NBT_SADDLED = "Saddle";
    private static final String NBT_REPRO_COUNT = "ReproCount";

    // other constants
    public static final int AGE_UPDATE_INTERVAL = 100;
    public static final UUID SCALE_MODIFIER_UUID = UUID.fromString("856d4ba4-9ffe-4a52-8606-890bb9be538b"); // just a random uuid I took online
    public static final int ALTITUDE_FLYING_THRESHOLD = 3;

    // server/client delegates

    private int reproCount;
    private float ageProgress;

    public ArgenEntity(EntityType<? extends ArgenEntity> type, Level level)
    {
        super(type, level);

        // enables walking over blocks
        maxUpStep = 1;
        noCulling = true;

        moveControl = new DragonMoveController(this);
//        animator = level.isClientSide? new DragonAnimator(this) : null;
//        breed = BreedRegistry.getFallback();
    }

    @Override
    @NotNull
    public BodyRotationControl createBodyControl()
    {
        return new DragonBodyController(this);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
                .add(MOVEMENT_SPEED, BASE_SPEED_GROUND)
                .add(MAX_HEALTH, BASE_HEALTH)
                .add(ATTACK_DAMAGE, BASE_FOLLOW_RANGE)
                .add(KNOCKBACK_RESISTANCE, BASE_KB_RESISTANCE)
                .add(ATTACK_DAMAGE, BASE_DAMAGE)
                .add(FLYING_SPEED, BASE_SPEED_FLYING);
    }

    @Override
    protected void registerGoals() // TODO: Much Smarter AI and features
    {
//        goalSelector.addGoal(1, new DragonLandGoal(this));
        goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        goalSelector.addGoal(3, new MeleeAttackGoal(this, 1, true));
//        goalSelector.addGoal(4, new DragonBabuFollowParent(this, 10));
        goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.1, 10f, 3.5f, true));
        this.goalSelector.addGoal(0, new FloatGoal(this));

        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, LivingEntity.class, 16f));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        targetSelector.addGoal(0, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(1, new OwnerHurtTargetGoal(this));
        targetSelector.addGoal(2, new HurtByTargetGoal(this));
//        targetSelector.addGoal(3, new NonTameRandomTargetGoal<>(this, Animal.class, false, e -> !(e instanceof ArgenEntity)));
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);

        entityData.define(DATA_BREED,"");
        entityData.define(DATA_FLYING, false);
        entityData.define(DATA_SADDLED, false);
        entityData.define(DATA_AGE, 0); // default to adult stage
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> data)
    {

        if (DATA_FLAGS_ID.equals(data)) refreshDimensions();
        else super.onSyncedDataUpdated(data);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound)
    {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isSitting", this.isSitting());

        compound.putBoolean(NBT_SADDLED, isSaddled());
        compound.putInt(NBT_REPRO_COUNT, reproCount);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound)
    {
        super.readAdditionalSaveData(compound);
        setSitting(compound.getBoolean("isSitting"));

        setSaddled(compound.getBoolean(NBT_SADDLED));
        this.reproCount = compound.getInt(NBT_REPRO_COUNT);

        entityData.set(DATA_AGE, getAge());
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }





    /**
     * Returns true if the dragon is saddled.
     */
    public boolean isSaddled()
    {
        return entityData.get(DATA_SADDLED);
    }

    @Override
    public boolean isSaddleable()
    {
        return isAlive() && !isBaby() && isTame();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource source)
    {
        setSaddled(true);
        level.playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }

    /**
     * Set or remove the saddle of the dragon.
     */
    public void setSaddled(boolean saddled)
    {
        entityData.set(DATA_SADDLED, saddled);
    }

    public void addReproCount()
    {
        reproCount++;
    }

    public boolean canFly()
    {
        // hatchling's can't fly
        return !isBaby();
    }




    public boolean shouldFly()
    {
        return canFly() && !isInWater() && isHighEnough(ALTITUDE_FLYING_THRESHOLD);
    }

    /**
     * Returns true if the entity is flying.
     */
    public boolean isFlying()
    {
        return entityData.get(DATA_FLYING);
    }

    /**
     * Set the flying flag of the entity.
     */
    public void setFlying(boolean flying)
    {
        entityData.set(DATA_FLYING, flying);
    }

    public boolean isServer()
    {
        return !level.isClientSide;
    }

    @Override
    public void tick()
    {
        super.tick();

        if (isServer())
        {
            if (age < 0 && tickCount % AGE_UPDATE_INTERVAL == 0) entityData.set(DATA_AGE, age);

            // update flying state based on the distance to the ground
            boolean flying = shouldFly();
            if (flying != isFlying())
            {
                // notify client
                setFlying(flying);

                // update AI follow range (needs to be updated before creating
                // new PathNavigate!)
                getAttribute(FOLLOW_RANGE).setBaseValue(flying? BASE_FOLLOW_RANGE_FLYING : BASE_FOLLOW_RANGE);

                // update pathfinding method
                if (flying) navigation = new FlyingPathNavigation(this, level);
                else navigation = new GroundPathNavigation(this, level);
            }
        }

    }


    public boolean canBeControlledByRider()
    {
        return getControllingPassenger() instanceof LivingEntity driver && isOwnedBy(driver);
    }



    @Override
    public void travel(Vec3 vec3)
    {
        boolean isFlying = isFlying();
        float speed = (float) getAttributeValue(isFlying? FLYING_SPEED : MOVEMENT_SPEED) * 0.225f;

        if (canBeControlledByRider()) // Were being controlled; override ai movement
        {
            LivingEntity driver = (LivingEntity) getControllingPassenger();
            double moveSideways = vec3.x;
            double moveY = vec3.y;
            double moveForward = Math.min(Math.abs(driver.zza) + Math.abs(driver.xxa), 1);

            // rotate head to match driver.
            float yaw = driver.yHeadRot;
            if (moveForward > 0) // rotate in the direction of the drivers controls
                yaw += (float) Mth.atan2(driver.zza, driver.xxa) * (180f / (float) Math.PI) - 90;
            yHeadRot = yaw;
            setXRot(driver.getXRot() * 0.68f);

            // rotate body towards the head
            setYRot(Mth.rotateIfNecessary(yHeadRot, getYRot(), 4));

            if (isControlledByLocalInstance()) // Client applies motion
            {
//                if (isFlying)
//                {
//                    moveForward = moveForward > 0? moveForward : 0;
//                    if (moveForward > 0 && BCConfig.cameraFlight()) moveY = -driver.getXRot() * (Math.PI / 180);
//                    else moveY = driver.jumping? 1 : KeyMap.FLIGHT_DESCENT.isDown()? -1 : 0;
//                }
                if (isFlying)
                {
                    moveForward = moveForward > 0? moveForward : 0;
                    moveY = 0;
                    if (driver.jumping) moveY = 1;
//                    && BCConfig.cameraFlight() this goes after moveforward > 0 below
                    else if (moveForward > 0) moveY = -driver.getXRot() * (Math.PI / 180);
                    else if (KeyMap.FLIGHT_DESCENT.isDown()) moveY = -1;
                }
                else if (driver.jumping && canFly()) liftOff();

                vec3 = new Vec3(moveSideways, moveY, moveForward);
                setSpeed(speed);
            }
            else if (driver instanceof Player) // other clients recieve animations
            {
                calculateEntityAnimation(this, true);
                setDeltaMovement(Vec3.ZERO);
                return;
            }
        }

        if (isFlying)
        {
            // Move relative to yaw - handled in the move controller or by driver
            moveRelative(speed, vec3);
            move(MoverType.SELF, getDeltaMovement());
            if (getDeltaMovement().lengthSqr() < 0.1) // we're not actually going anywhere, bob up and down.
                setDeltaMovement(getDeltaMovement().add(0, Math.sin(tickCount / 4f) * 0.03, 0));
            setDeltaMovement(getDeltaMovement().scale(0.9f)); // smoothly slow down

            calculateEntityAnimation(this, true);
        }
        else super.travel(vec3);
    }

    private int meatFeedings;

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

//
//        if (isTamedFor(player) && isSaddled() && !(item == Items.MUTTON))
//        {
//            if (isServer())
//            {
//                setRidingPlayer(player);
//                navigation.stop();
//                setTarget(null);
//            }
//            setOrderedToSit(false);
//            setInSittingPose(false);
//            return InteractionResult.SUCCESS;
//        }

//

        if(player.isShiftKeyDown() && isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND && !isSitting()) {
            player.sendSystemMessage(Component.literal(player.getName().getString() + " Ordered Argentavis to wait"));
            setSitting(!isSitting());
            return InteractionResult.SUCCESS;
        }

        if(player.isShiftKeyDown() && isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND && isSitting()) {
            player.sendSystemMessage(Component.literal(player.getName().getString() + " Ordered Argentavis to follow and ready for mounting"));
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



//        if(player.isShiftKeyDown() && isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND) {
//            setInSittingPose(false);
//            return InteractionResult.SUCCESS;
//        }

//        if(isTame() && !this.level.isClientSide && hand == InteractionHand.MAIN_HAND) {
//            setSitting(!isSitting());
//            return InteractionResult.SUCCESS;
//        }




//        if (itemstack.getItem() == itemForTaming) {
//            return InteractionResult.PASS;
//        }

        return super.mobInteract(player, hand);
    }



    /**
     * Returns the int-precision distance to solid ground.
     * Describe an inclusive limit to reduce iterations.
     */
    public double getAltitude(int limit)
    {
        var pointer = blockPosition().mutable().move(0, -1, 0);
        var min = level.dimensionType().minY();
        var i = 0;

        while(i <= limit && pointer.getY() > min && !level.getBlockState(pointer).getMaterial().isSolid())
            pointer.setY(getBlockY() - ++i);

        return i;
    }

    /**
     * Returns the distance to the ground while the entity is flying.
     */
    public double getAltitude()
    {
        return getAltitude(level.getMaxBuildHeight());
    }

    public boolean isHighEnough(int height)
    {
        return getAltitude(height) >= height;
    }

    public void liftOff()
    {
        if (canFly()) jumpFromGround();
    }

    @Override
    protected float getJumpPower()
    {
        // stronger jumps for easier lift-offs
        return super.getJumpPower() * (canFly()? 3 : 1);
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource)
    {
        return !canFly() && super.causeFallDamage(pFallDistance, pMultiplier, pSource);
    }

    @Override
    protected void tickDeath()
    {
        // unmount any riding entities
        ejectPassengers();

        // freeze at place
        setDeltaMovement(Vec3.ZERO);
        setYRot(yRotO);
        setYHeadRot(yHeadRotO);

        if (deathTime >= getMaxDeathTime()) remove(RemovalReason.KILLED); // actually delete entity after the time is up

        deathTime++;
    }

//    @Override
//    protected SoundEvent getAmbientSound()
//    {
//        double random = getRandom().nextDouble();
//
//        return SoundEvents.ENDER_DRAGON_GROWL;
//    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENDER_DRAGON_HURT;
    }


    @Override
    public SoundEvent getEatingSound(ItemStack itemStackIn)
    {
        return SoundEvents.GENERIC_EAT;
    }

    public SoundEvent getAttackSound()
    {
        return SoundEvents.GENERIC_EAT;
    }

    public SoundEvent getWingsSound()
    {
        return SoundEvents.ENDER_DRAGON_FLAP;
    }





    public boolean isFoodItem(ItemStack stack)
    {
        var food = stack.getItem().getFoodProperties(stack, this);
        return food != null && food.isMeat();
    }

    // the "food" that enables breeding mode
//    @Override
//    public boolean isFood(ItemStack stack)
//    {
//        return getBreed().breedingItems().contains(stack.getItem().builtInRegistryHolder());
//    }

    public void tamedFor(Player player, boolean successful)
    {
        if (successful)
        {
            setTame(true);
            navigation.stop();
            setTarget(null);
            setOwnerUUID(player.getUUID());
            level.broadcastEntityEvent(this, (byte) 7);
        }
        else
        {
            level.broadcastEntityEvent(this, (byte) 6);
        }
    }

    public boolean isTamedFor(Player player)
    {
        return isTame() && isOwnedBy(player);
    }

    /**
     * Returns the height of the eyes. Used for looking at other entities.
     */
    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn)
    {
        return sizeIn.height * 1.2f;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    @Override
    public double getPassengersRidingOffset()
    {
        if (!(isFlying())) {
            return getBbHeight() - 0.5;
        } else {
            return getBbHeight() - 1.5;
        }
    }





    /**
     * Returns render size modifier
     * <p>
     * 0.33 is the value representing the size for baby dragons.
     * 1.0 is the value representing the size for adult dragons.
     * We are essentially rough lerping from baby size to adult size, using ageProgress
     * as an input.
     * This value can be manipulated using the breed's size modifier
     */

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer)
    {
        return false;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    @Override
    public boolean onClimbable()
    {
        // this better doesn't happen...
        return false;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn)
    {
        super.dropCustomDeathLoot(source, looting, recentlyHitIn);

        if (isSaddled()) spawnAtLocation(Items.SADDLE);
    }



    @Override
    public boolean doHurtTarget(Entity entityIn)
    {
        boolean attacked = entityIn.hurt(DamageSource.mobAttack(this), (float) getAttribute(ATTACK_DAMAGE).getValue());

        if (attacked) doEnchantDamageEffects(this, entityIn);

        return attacked;
    }

    public void onWingsDown(float speed)
    {
        if (!isInWater())
        {
            // play wing sounds
            float pitch = (1 - speed);
            float volume = 0.3f + (1 - speed) * 0.2f;
            pitch *= getVoicePitch();
            volume *= getSoundVolume();
            level.playLocalSound(getX(), getY(), getZ(), getWingsSound(), SoundSource.VOICE, volume, pitch, true);
        }
    }

    @Override
    public void swing(InteractionHand hand)
    {
        // play eating sound
        playSound(getAttackSound(), 1, 0.7f);
        super.swing(hand);
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean hurt(DamageSource src, float par2)
    {
        if (isInvulnerableTo(src)) return false;

        // don't just sit there!
        setOrderedToSit(false);

        return super.hurt(src, par2);
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */



    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob)
    {
        return null;
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner)
    {
        return !(target instanceof TamableAnimal tameable) || !Objects.equals(tameable.getOwner(), owner);
    }



//    @Override
//    public boolean canBeControlledByRider()
//    {
//        return getControllingPassenger() instanceof LivingEntity driver && isOwnedBy(driver);
//    }

    /**
     * For vehicles, the first passenger is generally considered the controller and "drives" the vehicle. For example,
     * Pigs, Horses, and Boats are generally "steered" by the controlling passenger.
     */
    @Override
    public Entity getControllingPassenger()
    {
        List<Entity> list = getPassengers();
        return list.isEmpty()? null : list.get(0);
    }

    public void setRidingPlayer(Player player)
    {
        player.setYRot(getYRot());
        player.setXRot(getXRot());
        player.startRiding(this);
    }

//    @Override
//    public void positionRider(Entity passenger)
//    {
//        Entity riddenByEntity = getControllingPassenger();
//        if (riddenByEntity != null)
//        {
//            Vec3 pos = new Vec3(0, getPassengersRidingOffset() + riddenByEntity.getMyRidingOffset(), getScale())
//                    .yRot((float) Math.toRadians(-yBodyRot))
//                    .add(position());
//            passenger.setPos(pos.x, pos.y, pos.z);
//
//            // fix rider rotation
//            if (getFirstPassenger() instanceof LivingEntity)
//            {
//                LivingEntity rider = ((LivingEntity) riddenByEntity);
//                rider.xRotO = rider.getXRot();
//                rider.yRotO = rider.getYRot();
//                rider.yBodyRot = yBodyRot;
//            }
//        }
//    }



    @Override
    public boolean isInvulnerableTo(DamageSource src)
    {
        Entity srcEnt = src.getEntity();
        if (srcEnt != null && (srcEnt == this || hasPassenger(srcEnt))) return true;

        if (src == DamageSource.DRAGON_BREATH // inherited from it anyway
                || src == DamageSource.CACTUS) // assume cactus needles don't hurt thick scaled lizards
            return true;

        return super.isInvulnerableTo(src);
    }

    /**
     * Returns the entity's health relative to the maximum health.
     *
     * @return health normalized between 0 and 1
     */
    public double getHealthRelative()
    {
        return getHealth() / (double) getMaxHealth();
    }

    public int getMaxDeathTime()
    {
        return 120;
    }

    /**
     * Public wrapper for protected final setScale(), used by DragonLifeStageHelper.
     */
    @Override
    public void refreshDimensions()
    {
        double posXTmp = getX();
        double posYTmp = getY();
        double posZTmp = getZ();
        boolean onGroundTmp = onGround;

        super.refreshDimensions();

        // workaround for a vanilla bug; the position is apparently not set correcty
        // after changing the entity size, causing asynchronous server/client positioning
        setPos(posXTmp, posYTmp, posZTmp);

        // otherwise, setScale stops the dragon from landing while it is growing
        onGround = onGroundTmp;
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn)
    {
        var height = isInSittingPose()? 2.15f : BASE_HEIGHT;
        var scale = getScale();
        return new EntityDimensions(BASE_WIDTH * scale, height * scale, false);
    }










    @Override
    public boolean isInWall()
    {
        if (noPhysics) return false;
        else
        {
            var collider = getBoundingBox().deflate(getBbWidth() * 0.2f);
            return BlockPos.betweenClosedStream(collider).anyMatch((pos) ->
            {
                BlockState state = level.getBlockState(pos);
                return !state.isAir() && state.isSuffocating(level, pos) && Shapes.joinIsNotEmpty(state.getCollisionShape(level, pos).move(pos.getX(), pos.getY(), pos.getZ()), Shapes.create(collider), BooleanOp.AND);
            });
        }
    }

    @Override
    public Vec3 getLightProbePosition(float p_20309_)
    {
        return new Vec3(getX(), getY() + getBbHeight(), getZ());
    }
}
