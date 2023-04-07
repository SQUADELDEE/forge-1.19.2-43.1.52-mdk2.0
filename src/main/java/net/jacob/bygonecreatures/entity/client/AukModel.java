package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.AukEntity;
import net.jacob.bygonecreatures.entity.custom.AukEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class AukModel extends AnimatedGeoModel<AukEntity> {

    @Override
    public ResourceLocation getModelResource(AukEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/auk2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AukEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/auk/auktexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AukEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/auk.animation2.json");
    }

    @Override
    public void setLivingAnimations(AukEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone rotBone = this.getAnimationProcessor().getBone("holder");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (entity.isInWater()) {
            rotBone.setRotationX(extraData.headPitch * ((float) Math.PI / 180.0F));
            rotBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        }

//        IBone head_rotation = this.getAnimationProcessor().getBone("head");
//
//
//
//        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
//        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }


}
