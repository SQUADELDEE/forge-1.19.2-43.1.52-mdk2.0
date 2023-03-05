package net.jacob.bygonecreatures.entity.client.armor;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.item.custom.BreatherSet;
import net.jacob.bygonecreatures.item.custom.BreatherSet;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class BreatherSetModel extends AnimatedGeoModel<BreatherSet> {
    @Override
    public ResourceLocation getModelResource(BreatherSet object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "geo/breatherbag.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BreatherSet object) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "textures/armor/bbagtexture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BreatherSet animatable) {
        return new ResourceLocation(BygoneCreatures.MOD_ID, "animations/bbag.animation.json");
    }
}
