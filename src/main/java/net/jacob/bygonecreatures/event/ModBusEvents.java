package net.jacob.bygonecreatures.event;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.ModEntityTypes;
import net.jacob.bygonecreatures.entity.client.*;
import net.jacob.bygonecreatures.entity.client.armor.BreatherSetRenderer;
import net.jacob.bygonecreatures.entity.client.armor.DragonflyBootsRenderer;
import net.jacob.bygonecreatures.entity.custom.*;
import net.jacob.bygonecreatures.item.custom.BreatherSet;
import net.jacob.bygonecreatures.item.custom.DragonflyBootsItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.LogManager;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.logging.Logger;

public class ModBusEvents {

    @Mod.EventBusSubscriber(modid = BygoneCreatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(ModEntityTypes.DODO.get(), DodoEntity.setAttributes().build());
            event.put(ModEntityTypes.ARMOREDFISH.get(), ArmoredFishEntity.setAttributes().build());
            event.put(ModEntityTypes.CEPHALASPIS.get(), CephaEntity.setAttributes().build());
            event.put(ModEntityTypes.TERRORBIRD.get(), TerrorBirdEntity.setAttributes().build());
            event.put(ModEntityTypes.GLYPTODON.get(), GlyptodonEntity.setAttributes().build());
            event.put(ModEntityTypes.RAPTOR.get(), RaptorEntity.setAttributes().build());
            event.put(ModEntityTypes.DRAGONFLY.get(), DragonflyEntity.setAttributes().build());
            event.put(ModEntityTypes.ICHTHYOSAUR.get(), ItchyEntity.setAttributes().build());
            event.put(ModEntityTypes.KEMKEM.get(), KemKemEntity.setAttributes().build());
            event.put(ModEntityTypes.PROTOCERATOPS.get(), ProtoceratopsEntity.setAttributes().build());
        }

        @SubscribeEvent
        public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event){
            event.registerEntityRenderer(ModEntityTypes.DODO.get(), DodoRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.ARMOREDFISH.get(), ArmoredFishRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.CEPHALASPIS.get(), CephaRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.TERRORBIRD.get(), TerrorBirdRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.GLYPTODON.get(), GlyptodonRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.RAPTOR.get(), RaptorRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.DRAGONFLY.get(), DragonflyRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.ICHTHYOSAUR.get(), ItchyRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.KEMKEM.get(), KemKemRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.PROTOCERATOPS.get(), ProtoceratopsRenderer::new);
        }


        @SubscribeEvent
        public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
            GeoArmorRenderer.registerArmorRenderer(DragonflyBootsItem.class, new DragonflyBootsRenderer());
            GeoArmorRenderer.registerArmorRenderer(BreatherSet.class, new BreatherSetRenderer());
        }









    }
}
