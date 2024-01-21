package net.jacob.bygonecreatures.event;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.block.client.AnimatedBlockRenderer;
import net.jacob.bygonecreatures.block.entity.ModBlockEntities;
import net.jacob.bygonecreatures.entity.ModEntityTypes;
import net.jacob.bygonecreatures.entity.client.*;
import net.jacob.bygonecreatures.entity.client.armor.BreatherSetRenderer;
import net.jacob.bygonecreatures.entity.client.armor.DragonflyBootsRenderer;
import net.jacob.bygonecreatures.entity.client.armor.FinsRenderer;
import net.jacob.bygonecreatures.entity.custom.*;
import net.jacob.bygonecreatures.item.client.custom.BreatherSet;
import net.jacob.bygonecreatures.item.client.custom.DragonflyBootsItem;
import net.jacob.bygonecreatures.item.client.custom.FinSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

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
            event.put(ModEntityTypes.AUK.get(), AukEntity.createAttributes().build());
            event.put(ModEntityTypes.DIPLOCAULUS.get(), DiplocaulusEntity.createAttributes().build());
            event.put(ModEntityTypes.MOUSE.get(), MouseEntity.setAttributes().build());
            event.put(ModEntityTypes.ANOMALOCARIS.get(), AnomaloEntity.setAttributes().build());
            event.put(ModEntityTypes.CURLYCOAT.get(), CurlyCoatEntity.setAttributes().build());
            event.put(ModEntityTypes.ARGEN.get(), ArgenEntity.createAttributes().build());
            event.put(ModEntityTypes.PTERA.get(), PteraEntity.bakeAttributes().build());
            event.put(ModEntityTypes.BEAR.get(), BearEntity.setAttributes().build());
            event.put(ModEntityTypes.PECCARY.get(), PeccaryEntity.setAttributes().build());
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
            event.registerEntityRenderer(ModEntityTypes.AUK.get(), AukRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.DIPLOCAULUS.get(), DiploRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.MOUSE.get(), MouseRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.ANOMALOCARIS.get(), AnomalocarisRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.CURLYCOAT.get(), CurlyCoatRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.ARGEN.get(), ArgenRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.PTERA.get(), PteraRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.BEAR.get(), BearRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.PECCARY.get(), PeccaryRenderer::new);
        }


        @SubscribeEvent
        public static void registerArmorRenderers(final EntityRenderersEvent.AddLayers event) {
            GeoArmorRenderer.registerArmorRenderer(DragonflyBootsItem.class, new DragonflyBootsRenderer());
            GeoArmorRenderer.registerArmorRenderer(BreatherSet.class, new BreatherSetRenderer());
            GeoArmorRenderer.registerArmorRenderer(FinSet.class, new FinsRenderer());
        }

        @SubscribeEvent
        public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.ANIMATED_BLOCK_ENTITY.get(), AnimatedBlockRenderer::new);
        }












    }
}
