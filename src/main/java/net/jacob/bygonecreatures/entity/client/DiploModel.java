package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.DiplocaulusEntity;
import net.jacob.bygonecreatures.entity.custom.DiplocaulusEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class DiploModel extends AnimatedGeoModel<DiplocaulusEntity> {
    @Override
    public ResourceLocation getModelResource(DiplocaulusEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/diplocaulus.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DiplocaulusEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/diplocaulus/diplotexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DiplocaulusEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/diplocaulus.animation.json");
    }

    @Override
    public void setLivingAnimations(DiplocaulusEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone rotBone = this.getAnimationProcessor().getBone("skeleton");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (entity.isInWater()) {
            rotBone.setRotationX(extraData.headPitch * ((float) Math.PI / 180.0F));
            rotBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        }

//
    }
}
