package net.jacob.bygonecreatures.entity.custom;

import net.jacob.bygonecreatures.entity.ModEntityTypes;
import net.jacob.bygonecreatures.entity.client.GMenu;
import net.jacob.bygonecreatures.entity.client.network.LuggageNetworkHandler;
import net.jacob.bygonecreatures.entity.client.network.OpenLuggageScreenPacket;
import net.jacob.bygonecreatures.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.EnumSet;

import static net.jacob.bygonecreatures.item.ModItems.*;


public class GlyptodonEntity extends Animal implements IAnimatable, ItemSteerable, Saddleable, ContainerListener {

    public static final String INVENTORY_TAG = "Inventory";

    private SimpleContainer inventory;
    private LazyOptional<?> itemHandler = null;
    private int soundCooldown = 15;
    private int fetchCooldown = 0;
    private boolean tryingToFetchItem;
    private boolean isInventoryOpen;



    @Override
    public void containerChanged(Container container) {
        //I don't think I need this for anything
    }

    public boolean hasInventoryChanged(Container container) {
        return this.inventory != container;
    }

    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(GlyptodonEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_ID_CHEST = SynchedEntityData.defineId(AbstractChestedHorse.class, EntityDataSerializers.BOOLEAN);
    public static final int INV_CHEST_COUNT = 15;
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(GlyptodonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(GlyptodonEntity.class, EntityDataSerializers.INT);

    private final ItemBasedSteering steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);

    public int geggTime = this.random.nextInt(3000) + 3000;
    public GlyptodonEntity(EntityType<? extends Animal> entityType, Level level) {

        super(entityType, level);
        this.createInventory();
    }


    private void createInventory() {
        SimpleContainer simplecontainer = this.inventory;
//        this.hasExtendedInventory() ? 54 : (used to say this)
        this.inventory = new SimpleContainer( 27);
        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

            for (int j = 0; j < i; ++j) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this);
        this.itemHandler = LazyOptional.of(() -> new InvWrapper(this.inventory));
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    private AnimationFactory factory = GeckoLibUtil.createFactory(this);


    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        return ModEntityTypes.GLYPTODON.get().create(level);
    }

//    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
//        if (!this.isSaddled()) {
//            this.playerLooking = player;
//            this.playerLookTicks = 15;
//        }
//        MenuType<GlyptodonContainer> menuType = (MenuType<GlyptodonContainer>) Registry.MENU.get(this.isSaddled() ? Constants.TAME_MIMIC_CONTAINER : Constants.EVIL_MIMIC_CONTAINER);
//        return new GlyptodonContainer(menuType, id, playerInventory, this, 3);
//    }


//    @Override
//    public ItemStack removeItem(int p_70298_1_, int p_70298_2_) {
//        ItemStack itemstack = ContainerHelper.removeItem(this.heldItems, p_70298_1_, p_70298_2_);
//        if (!itemstack.isEmpty()) {
//            this.setChanged();
//        }
//
//        return itemstack;
//    }

//    @Override
//    public int getContainerSize() {
//        return 27;
//    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller",
                4, this::predicate));
    }

    public <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walking", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }
        if (this.isSleeping()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("sleeping", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public boolean isFood(ItemStack stack) {

        return Ingredient.of(Items.WHEAT).test(stack);
    }


    @javax.annotation.Nullable
    public Entity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        return entity != null && this.canBeControlledBy(entity) ? entity : null;
    }

    private boolean canBeControlledBy(Entity entity) {
        if (this.isSaddled() && entity instanceof Player player) {
            return player.getMainHandItem().is(WHEATONASTICK.get()) || player.getOffhandItem().is(WHEATONASTICK.get());
        } else {
            return false;
        }
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLEEPING, false);
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
        this.entityData.define(DATA_ID_CHEST, false);

    }

    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Sleeping", this.isSleeping());
        ListTag listtag = new ListTag();

        this.steering.addAdditionalSaveData(nbt);

        for (int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte) i);
                itemstack.save(compoundtag);
                listtag.add(compoundtag);
            }
        }

        nbt.put("Items", listtag);
    }

    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setSleeping(nbt.getBoolean("Sleeping"));
        this.steering.readAdditionalSaveData(nbt);

        ListTag listtag = nbt.getList("Items", 10);



        for (int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int j = compoundtag.getByte("Slot") & 255;
            if (j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.of(compoundtag));
            }
        }


    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if (DATA_BOOST_TIME.equals(data) && this.level.isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(data);
    }

//    @Override
//    public void positionRider(Entity passenger) {
//        float ySin = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));
//        float yCos = Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
//        passenger.setPos(this.getX() + (double)(0.0002F * ySin), this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset() - 0.1F, this.getZ() - (double)(0.0002F * yCos));
//    }



    public double getPassengersRidingOffset() {
        return super.getPassengersRidingOffset() + 0.5D;
    }


    public class SleepGoal extends Goal {
        private final int countdownTime;
        private int countdown;

        public SleepGoal(int countdownTime) {
            this.countdownTime = countdownTime;
            this.countdown = GlyptodonEntity.this.random.nextInt(reducedTickDelay(countdownTime));
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        public boolean canUse() {
            if (GlyptodonEntity.this.xxa == 0.0F && GlyptodonEntity.this.yya == 0.0F && GlyptodonEntity.this.zza == 0.0F) {
                return this.canSleep() || GlyptodonEntity.this.isSleeping();
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
                return GlyptodonEntity.this.level.isNight();
            }
        }

        public void stop() {
            GlyptodonEntity.this.setSleeping(false);
            this.countdown = GlyptodonEntity.this.random.nextInt(this.countdownTime);
        }

        public void start() {
            GlyptodonEntity.this.setJumping(false);
            GlyptodonEntity.this.setSleeping(true);
            GlyptodonEntity.this.getNavigation().stop();
            GlyptodonEntity.this.getMoveControl().setWantedPosition(GlyptodonEntity.this.getX(), GlyptodonEntity.this.getY(), GlyptodonEntity.this.getZ(), 0.0D);
        }
    }

    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }


    @Override
    public boolean isInvulnerableTo(DamageSource src)
    {
        Entity srcEnt = src.getEntity();
        if (srcEnt != null && (srcEnt == this || hasPassenger(srcEnt))) return true;

        if ( src == DamageSource.CACTUS) // THICK SHELLED
            return true;
        if (src == DamageSource.ANVIL)
            return true;

        return super.isInvulnerableTo(src);
    }



    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean flag = this.isFood(player.getItemInHand(hand));
        if (!flag && this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level.isClientSide  && !player.isShiftKeyDown()) {
                player.startRiding(this);
            }



            return InteractionResult.sidedSuccess(this.level.isClientSide);

        } else {
            InteractionResult interactionresult = super.mobInteract(player, hand);
            if (!interactionresult.consumesAction()) {
                ItemStack itemstack = player.getItemInHand(hand);
                return itemstack.is(Items.SADDLE) ? itemstack.interactLivingEntity(player, this, hand) : InteractionResult.PASS;
            } else {
                return interactionresult;
            }
        }






    }
    @Override
    public boolean removeWhenFarAway(double dist) {
        return false;
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.NAME_TAG)) return InteractionResult.PASS;

        if(player.isShiftKeyDown() && this.isSaddled()) {
            this.getLevel().gameEvent(player, GameEvent.CONTAINER_OPEN, player.blockPosition());
            //prevents sound from playing 4 times (twice on server only). Apparently interactAt fires 4 times????
            if (this.soundCooldown == 0) {
                this.playSound(SoundEvents.CHEST_OPEN, 0.5F, this.getRandom().nextFloat() * 0.1F + 0.9F);
                this.soundCooldown = 5;
            }
            if (!this.getLevel().isClientSide()) {
                ServerPlayer sp = (ServerPlayer) player;
                if (sp.containerMenu != sp.inventoryMenu) sp.closeContainer();

                sp.nextContainerCounter();
                LuggageNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), new OpenLuggageScreenPacket(sp.containerCounter, this.getId()));
                sp.containerMenu = new GMenu(sp.containerCounter, sp.getInventory(), this.inventory, this);
                sp.initMenu(sp.containerMenu);
                this.isInventoryOpen = true;
                MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(sp, sp.containerMenu));
            }
        }

        return super.interactAt(player, vec, hand);


    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (this.isAlive() && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.itemHandler != null)
            return this.itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (this.itemHandler != null) {
            LazyOptional<?> oldHandler = this.itemHandler;
            this.itemHandler = null;
            oldHandler.invalidate();
        }
    }


    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    public void setInventoryOpen(boolean open) {
        this.isInventoryOpen = open;
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }

    }

    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    public void equipSaddle(@Nullable SoundSource sound) {
        this.steering.setSaddle(true);
        if (sound != null) {
            this.level.playSound((Player)null, this, SoundEvents.PIG_SADDLE, sound, 0.5F, 1.0F);
        }

    }

    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }

    public float getSteeringSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.1F;
    }

    @Override
    public void travel(Vec3 vector) {
        this.travel(this, this.steering, vector);
    }

    @Override
    public void travelWithInput(Vec3 vector) {
        super.travel(vector);
    }

    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        Direction direction = this.getMotionDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.getDismountLocationForPassenger(livingEntity);
        } else {
            int[][] aint = DismountHelper.offsetsForDirection(direction);
            BlockPos blockpos = this.blockPosition();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(Pose pose : livingEntity.getDismountPoses()) {
                AABB aabb = livingEntity.getLocalBoundsForPose(pose);

                for(int[] aint1 : aint) {
                    blockpos$mutableblockpos.set(blockpos.getX() + aint1[0], blockpos.getY(), blockpos.getZ() + aint1[1]);
                    double d0 = this.level.getBlockFloorHeight(blockpos$mutableblockpos);
                    if (DismountHelper.isBlockFloorValid(d0)) {
                        Vec3 vec3 = Vec3.upFromBottomCenterOf(blockpos$mutableblockpos, d0);
                        if (DismountHelper.canDismountTo(this.level, livingEntity, aabb.move(vec3))) {
                            livingEntity.setPose(pose);
                            return vec3;
                        }
                    }
                }
            }

            return super.getDismountLocationForPassenger(livingEntity);
        }
    }



    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.FOLLOW_RANGE, 100);

    }




    public void aiStep() {
        super.aiStep();


        if (!this.level.isClientSide && this.isAlive() && !this.isBaby() && --this.geggTime <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(DUNG.get());
            this.gameEvent(GameEvent.ENTITY_PLACE);
            this.geggTime = this.random.nextInt(3000) + 3000;
        }

    }

    private EatBlockGoal eatBlockGoal;

    protected void registerGoals() {
        this.eatBlockGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(ModItems.WHEATONASTICK.get()), false));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new SleepGoal(200));
        this.goalSelector.addGoal(5, this.eatBlockGoal);
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(7, (new HurtByTargetGoal(this)).setAlertOthers());
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
