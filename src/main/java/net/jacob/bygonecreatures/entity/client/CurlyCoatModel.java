package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.CurlyCoatEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class CurlyCoatModel extends AnimatedGeoModel<CurlyCoatEntity> {

    @Override
    public ResourceLocation getModelResource(CurlyCoatEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/curlycoat.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CurlyCoatEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/curlycoat/pigtexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CurlyCoatEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/curlycoat.animation.json");
    }

    @Override
    public void setLivingAnimations(CurlyCoatEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head_rotation = this.getAnimationProcessor().getBone("headhold");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

//        IBone head_rotation = this.getAnimationProcessor().getBone("head");
//
//
//
//        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
//        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }


}
