package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.ProtoceratopsEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class ProtoceratopsModel extends AnimatedGeoModel<ProtoceratopsEntity> {


    @Override
    public ResourceLocation getModelResource(ProtoceratopsEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/protoceratops2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ProtoceratopsEntity object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/entity/protoceratops/ptexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ProtoceratopsEntity animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/protoceratops.animation.json");
    }


    @Override
    public void setLivingAnimations(ProtoceratopsEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setCustomAnimations(entity, uniqueID, customPredicate);
        IBone head_rotation = this.getAnimationProcessor().getBone("platehold");

        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);

        head_rotation.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
        head_rotation.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
    }
}
