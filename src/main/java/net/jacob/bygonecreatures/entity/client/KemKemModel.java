package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.KemKemEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class KemKemModel extends AnimatedGeoModel<KemKemEntity> {


    @Override
    public ResourceLocation getModelResource(KemKemEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/kemkem.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KemKemEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/kemkem/kemtexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(KemKemEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/kemkem.animation.json");
    }

    @Override
    public void setLivingAnimations(KemKemEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone rotBone = this.getAnimationProcessor().getBone("body");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (entity.isInWater()) {
            rotBone.setRotationX(extraData.headPitch * ((float) Math.PI / 180.0F));
            rotBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        }
    }
}
