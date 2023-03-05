package net.jacob.bygonecreatures.entity;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.ibm.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BygoneCreatures.MOD_ID);

    public static final RegistryObject<EntityType<DodoEntity>> DODO =
            ENTITY_TYPES.register("dodo",
                    () -> EntityType.Builder.of(DodoEntity::new, MobCategory.CREATURE)
                            .sized(0.8f, 0.6f)
                            .build(new ResourceLocation(BygoneCreatures.MOD_ID, "dodo").toString()));
    public static final RegistryObject<EntityType<ArmoredFishEntity>> ARMOREDFISH =
            ENTITY_TYPES.register("armoredfish",
                    () -> EntityType.Builder.of(ArmoredFishEntity::new, MobCategory.WATER_CREATURE)
                            .sized(0.8f, 0.6f)
                            .build(new ResourceLocation(BygoneCreatures.MOD_ID, "armoredfish").toString()));

    public static final RegistryObject<EntityType<CephaEntity>> CEPHALASPIS =
            ENTITY_TYPES.register("cephalaspis",
                    () -> EntityType.Builder.of(CephaEntity::new, MobCategory.WATER_CREATURE)
                            .sized(0.8f, 0.6f)
                            .build(new ResourceLocation(BygoneCreatures.MOD_ID, "cephalaspis").toString()));
    public static final RegistryObject<EntityType<TerrorBirdEntity>> TERRORBIRD =
            ENTITY_TYPES.register("terrorbird",
                    () -> EntityType.Builder.of(TerrorBirdEntity::new, MobCategory.CREATURE)
                            .sized(2.0f, 2.7f)
                            .build(new ResourceLocation(BygoneCreatures.MOD_ID, "terrorbird").toString()));

    public static final RegistryObject<EntityType<GlyptodonEntity>> GLYPTODON =
            ENTITY_TYPES.register("glyptodon",
                    () -> EntityType.Builder.of(GlyptodonEntity::new, MobCategory.CREATURE)
                            .sized(1.7f, 1.8f)
                            .build(new ResourceLocation(BygoneCreatures.MOD_ID, "glyptodon").toString()));

    public static final RegistryObject<EntityType<RaptorEntity>> RAPTOR =
            ENTITY_TYPES.register("raptor",
                    () -> EntityType.Builder.of(RaptorEntity::new, MobCategory.CREATURE)
                            .sized(0.8f, 0.6f)
                            .build(new ResourceLocation(BygoneCreatures.MOD_ID, "raptor").toString()));

    public static final RegistryObject<EntityType<DragonflyEntity>> DRAGONFLY =
            ENTITY_TYPES.register("dragonfly",
                    () -> EntityType.Builder.of(DragonflyEntity::new, MobCategory.CREATURE)
                            .sized(1.0f, 0.7f)
                            .build(new ResourceLocation(BygoneCreatures.MOD_ID, "dragonfly").toString()));

    public static final RegistryObject<EntityType<ItchyEntity>> ICHTHYOSAUR =
            ENTITY_TYPES.register("ichthyosaur",
                    () -> EntityType.Builder.of(ItchyEntity::new, MobCategory.WATER_CREATURE)
                            .sized(1.7f, 1.8f)
                            .build(new ResourceLocation(BygoneCreatures.MOD_ID, "ichthyosaur").toString()));

    public static final RegistryObject<EntityType<KemKemEntity>> KEMKEM =
            ENTITY_TYPES.register("kemkem",
                    () -> EntityType.Builder.of(KemKemEntity::new, MobCategory.WATER_CREATURE)
                            .sized(1.0f, 0.7f)
                            .build(new ResourceLocation(BygoneCreatures.MOD_ID, "kemkem").toString()));
    public static final RegistryObject<EntityType<ProtoceratopsEntity>> PROTOCERATOPS =
            ENTITY_TYPES.register("protoceratops",
                    () -> EntityType.Builder.of(ProtoceratopsEntity::new, MobCategory.CREATURE)
                            .sized(1.0f, 1.0f)
                            .build(new ResourceLocation(BygoneCreatures.MOD_ID, "protoceratops").toString()));
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

}


