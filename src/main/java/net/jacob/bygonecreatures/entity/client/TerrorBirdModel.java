package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.GlyptodonEntity;
import net.jacob.bygonecreatures.entity.custom.TerrorBirdEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class TerrorBirdModel extends AnimatedGeoModel<TerrorBirdEntity> {
    @Override
    public ResourceLocation getModelResource(TerrorBirdEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/tbird.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TerrorBirdEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/terrorbird/tbird2text.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TerrorBirdEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/tbird.animation.json");
    }

    @Override
    public void setLivingAnimations(TerrorBirdEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head_rotation = this.getAnimationProcessor().getBone("headhold");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
