package net.jacob.bygonecreatures.entity.custom;

import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class SlingshotProjectile extends ThrowableItemProjectile {

    protected boolean ricochet;
    protected int ricochetTimes = 0;

    private boolean dropItem;

    public SlingshotProjectile(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public SlingshotProjectile(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    public SlingshotProjectile(EntityType<? extends ThrowableItemProjectile> type, LivingEntity shooter, Level level) {
        super(type, shooter, level);
    }

    @Override
    protected Item getDefaultItem() {
        return null;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        BlockState blockstate = this.level.getBlockState(result.getBlockPos());
        LivingEntity shooter = (LivingEntity) this.getOwner();
        if (!blockstate.getCollisionShape(this.level, result.getBlockPos()).isEmpty()) {
            this.playStepSound(result.getBlockPos(), blockstate);
            if (!this.level.isClientSide) {
                this.level.broadcastEntityEvent(this, (byte) 3);
                if (this.ricochet) {
                    Vec3 delta = this.getDeltaMovement();
                    Direction direction = result.getDirection();
                    float velocity = (float) delta.length() / 2.0F;
                    if (direction == Direction.UP || direction == Direction.DOWN) {
                        this.shoot(delta.x, delta.reverse().y, delta.z, velocity, 1.0F);
                    } else if (direction == Direction.WEST || direction == Direction.EAST) {
                        this.shoot(delta.reverse().x, delta.reverse().y, delta.z, velocity, 1.0F);
                    } else {
                        this.shoot(delta.x, delta.reverse().y, delta.reverse().z, velocity, 1.0F);
                    }
                    this.ricochetTimes--;
                    if (this.ricochetTimes == 0) {
                        this.discard();
                        if (!(shooter instanceof Player) || ((Player) shooter).getAbilities().instabuild) {
                            //don't drop anything
                        } else if (this.dropItem) {
                            this.spawnAtLocation(new ItemStack(getDefaultItem()));
                        }
                    }
                } else {
                    this.discard();
                    if (!(shooter instanceof Player) || ((Player) shooter).getAbilities().instabuild) {
                        //don't drop anything
                    } else if (this.dropItem) {
                        this.spawnAtLocation(new ItemStack(getDefaultItem()));
                    }
                }
            }
        }
    }

    public void setRicochetTimes(int times) {
        this.ricochet = true;
        this.ricochetTimes = times;
    }

    protected void setDropItem(boolean dropItem) {
        this.dropItem = dropItem;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
