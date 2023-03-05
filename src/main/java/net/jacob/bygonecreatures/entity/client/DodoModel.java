package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.DodoEntity;
import net.jacob.bygonecreatures.entity.custom.GlyptodonEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;


public class DodoModel extends AnimatedGeoModel<DodoEntity> {

    @Override
    public ResourceLocation getModelResource(DodoEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/dodoanimationsnew.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DodoEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/dodo/bodytexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DodoEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/ok.animationfinal.json");
    }

//    @Override
//    public void setLivingAnimations(DodoEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
//        super.setCustomAnimations(entity, uniqueID, customPredicate);
//        IBone head_rotation = this.getAnimationProcessor().getBone("head");
//
//        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
//
//        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
//        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
//    }
}