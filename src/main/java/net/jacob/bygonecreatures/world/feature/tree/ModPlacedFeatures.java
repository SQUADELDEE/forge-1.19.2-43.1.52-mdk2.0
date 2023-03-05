package net.jacob.bygonecreatures.world.feature.tree;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {

    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, BygoneCreatures.MOD_ID);



    public static final RegistryObject<PlacedFeature> KINGSAGO_PLACED = PLACED_FEATURES.register("kingsago_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.KINGSAGO.getHolder().get(), List.of(RarityFilter.onAverageOnceEvery(16),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));

    public static final RegistryObject<PlacedFeature> DESERTSCRUB_PLACED = PLACED_FEATURES.register("desertscrub_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.DESERTSCRUB.getHolder().get(), List.of(RarityFilter.onAverageOnceEvery(16),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));



    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);

    }

}
