package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.ArmoredFishEntity;
import net.jacob.bygonecreatures.entity.custom.CephaEntity;
import net.jacob.bygonecreatures.entity.custom.DodoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class CephaModel extends AnimatedGeoModel<CephaEntity> {

    @Override
    public ResourceLocation getModelResource(CephaEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/cephalaspis.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CephaEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/cepha/texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CephaEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/model.animation.json");
    }

    @Override
    public void setLivingAnimations(CephaEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone rotBone = this.getAnimationProcessor().getBone("bone");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (entity.isInWater()) {
            rotBone.setRotationX(extraData.headPitch * ((float) Math.PI / 180.0F));
            rotBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        }
    }
}
