package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.DodoEntity;
import net.jacob.bygonecreatures.entity.custom.GlyptodonEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class GlyptodonModel extends AnimatedGeoModel<GlyptodonEntity> {


    @Override
    public ResourceLocation getModelResource(GlyptodonEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/glyptodon2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GlyptodonEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/glyptodon/gtexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GlyptodonEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/glyptodon.animation2.json");
    }


    @Override
    public void setLivingAnimations(GlyptodonEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head_rotation = this.getAnimationProcessor().getBone("headhold");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
