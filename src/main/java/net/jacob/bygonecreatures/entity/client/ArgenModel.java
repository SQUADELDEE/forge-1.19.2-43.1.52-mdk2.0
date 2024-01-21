package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.ArgenEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class ArgenModel extends AnimatedGeoModel<ArgenEntity> {

    @Override
    public ResourceLocation getModelResource(ArgenEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/argen.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ArgenEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/argen/argtexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ArgenEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/argen.animation.json");
    }

    @Override
    public void setLivingAnimations(ArgenEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head_rotation = this.getAnimationProcessor().getBone("head");
        IBone body_rotation = this.getAnimationProcessor().getBone("bone");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));




//

//      if (entity.isFlying()) {
//          body_rotation.setRotationX((extraData.headPitch * ((float) Math.PI / 310F)));
//        }
//

    }


}

