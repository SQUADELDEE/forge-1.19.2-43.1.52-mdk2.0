package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.PteraEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class PteraModel extends AnimatedGeoModel<PteraEntity> {

    @Override
    public ResourceLocation getModelResource(PteraEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/ptera.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PteraEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/ptera/pteratext.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PteraEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/ptera.animation.json");
    }

    @Override
    public void setLivingAnimations(PteraEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head_rotation = this.getAnimationProcessor().getBone("headhold");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
