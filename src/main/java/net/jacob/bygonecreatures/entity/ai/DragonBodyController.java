package net.jacob.bygonecreatures.entity.ai;

import net.jacob.bygonecreatures.entity.custom.ArgenEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class DragonBodyController extends BodyRotationControl
{
    private final ArgenEntity dragon;

    public DragonBodyController(ArgenEntity dragon)
    {
        super(dragon);
        this.dragon = dragon;
    }

    @Override
    public void clientTick()
    {

        dragon.yBodyRot = dragon.getYRot();


        dragon.yHeadRot = Mth.rotateIfNecessary(dragon.yHeadRot, dragon.yBodyRot, dragon.getMaxHeadYRot());
    }
}