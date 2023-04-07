package net.jacob.bygonecreatures.item.client;

import net.jacob.bygonecreatures.item.custom.AnimatedBlockItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class AnimatedBlockItemRenderer extends GeoItemRenderer<AnimatedBlockItem> {
    public AnimatedBlockItemRenderer() {
        super(new AnimatedBlockItemModel());
    }
}
