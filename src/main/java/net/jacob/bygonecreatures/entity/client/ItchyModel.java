package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.ArmoredFishEntity;
import net.jacob.bygonecreatures.entity.custom.ItchyEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class ItchyModel extends AnimatedGeoModel<ItchyEntity> {

    @Override
    public ResourceLocation getModelResource(ItchyEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/icthyosaurfinal2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ItchyEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/itch/itchytexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ItchyEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/ich.animation3.json");
    }

    @Override
    public void setLivingAnimations(ItchyEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone rotBone = this.getAnimationProcessor().getBone("bone");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (entity.isInWater()) {
            rotBone.setRotationX(extraData.headPitch * ((float) Math.PI / 180.0F));
            rotBone.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180.0F));
        }
    }
}
