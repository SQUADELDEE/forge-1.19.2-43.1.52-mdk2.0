package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.DodoEntity;
import net.jacob.bygonecreatures.entity.custom.GlyptodonEntity;
import net.jacob.bygonecreatures.entity.custom.RaptorEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class RaptorModel extends AnimatedGeoModel<RaptorEntity> {



    @Override
    public ResourceLocation getModelResource(RaptorEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/smallraptor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RaptorEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/raptorentity/raptortexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RaptorEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/smallraptor.animation.json");
    }

    @Override
    public void setLivingAnimations(RaptorEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head_rotation = this.getAnimationProcessor().getBone("head");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));

    }
}
