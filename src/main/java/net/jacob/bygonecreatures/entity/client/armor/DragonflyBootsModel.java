package net.jacob.bygonecreatures.entity.client.armor;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.item.custom.DragonflyBootsItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DragonflyBootsModel extends AnimatedGeoModel<DragonflyBootsItem> {


    @Override
    public ResourceLocation getModelResource(DragonflyBootsItem object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/customarmor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DragonflyBootsItem object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/armor/dbootstexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DragonflyBootsItem animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/model1.animation.json");
    }
}
