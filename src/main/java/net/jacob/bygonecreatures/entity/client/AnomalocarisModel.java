package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.AnomaloEntity;
import net.jacob.bygonecreatures.entity.custom.ArmoredFishEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class AnomalocarisModel extends AnimatedGeoModel<AnomaloEntity> {
    @Override
    public ResourceLocation getModelResource(AnomaloEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/anomalo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AnomaloEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/anomalocaris/anotexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AnomaloEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/anomalo.animation.json");
    }

    @Override
    public void setLivingAnimations(AnomaloEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone rotBone = this.getAnimationProcessor().getBone("bone");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (entity.isInWater()) {
            rotBone.setRotationX(extraData.headPitch * ((float) Math.PI / 180.0F));
            rotBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        }
    }

}
