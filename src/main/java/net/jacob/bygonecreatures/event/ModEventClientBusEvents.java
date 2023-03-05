package net.jacob.bygonecreatures.event;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.client.armor.BreatherSetRenderer;
import net.jacob.bygonecreatures.entity.client.armor.DragonflyBootsRenderer;
import net.jacob.bygonecreatures.item.custom.BreatherSet;
import net.jacob.bygonecreatures.item.custom.DragonflyBootsItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod.EventBusSubscriber(modid = BygoneCreatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
    @SubscribeEvent
    public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
        GeoArmorRenderer.registerArmorRenderer(DragonflyBootsItem.class, new DragonflyBootsRenderer());
        GeoArmorRenderer.registerArmorRenderer(BreatherSet.class, new BreatherSetRenderer());
    }
}
