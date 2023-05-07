package net.jacob.bygonecreatures.entity.ai;

//import net.jacob.bygonecreatures.entity.custom.ArgenEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

//public class DragonBodyController extends BodyRotationControl
//{
//    private final ArgenEntity dragon;
//
//    public DragonBodyController(ArgenEntity dragon)
//    {
//        super(dragon);
//        this.dragon = dragon;
//    }
//
//    @Override
//    public void clientTick()
//    {
//        // sync the body to the yRot; no reason to have any other random rotations.
//        dragon.yBodyRot = dragon.getYRot();
//
//        // clamp head rotations so necks don't fucking turn inside out
//        dragon.yHeadRot = Mth.rotateIfNecessary(dragon.yHeadRot, dragon.yBodyRot, dragon.getMaxHeadYRot());
//    }
//}