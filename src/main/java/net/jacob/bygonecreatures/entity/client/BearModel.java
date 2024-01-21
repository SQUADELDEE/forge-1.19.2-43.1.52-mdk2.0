package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.BearEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class BearModel extends AnimatedGeoModel<BearEntity> {


    @Override
    public ResourceLocation getModelResource(BearEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/bear.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BearEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/bear/beartexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BearEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/bear.animation.json");
    }


    @Override
    public void setLivingAnimations(BearEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head_rotation = this.getAnimationProcessor().getBone("headhold");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / -180F));
        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
