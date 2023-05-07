package net.jacob.bygonecreatures.item.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.item.client.custom.AnimatedBlockItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class AnimatedBlockItemModel extends AnimatedGeoModel<AnimatedBlockItem> {
    @Override
    public ResourceLocation getModelResource(AnimatedBlockItem object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/biterblock.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AnimatedBlockItem object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/block/bitertexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AnimatedBlockItem animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/biterblock.animation.json");
    }
}
