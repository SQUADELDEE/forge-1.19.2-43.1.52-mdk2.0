package net.jacob.bygonecreatures.block;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.block.custom.kingsago;
import net.jacob.bygonecreatures.block.custom.sleekstonepebble;
import net.jacob.bygonecreatures.item.ModCreativeModeTab;
import net.jacob.bygonecreatures.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, BygoneCreatures.MOD_ID);

    public static final RegistryObject<Block> FOSSILMUD = registerBlock("fossilmud",
            () -> new Block(BlockBehaviour.Properties.of(Material.DIRT)
                    .strength(0.5f)), ModCreativeModeTab.BygoneCreatures_TAB);

    public static final RegistryObject<Block> MOSSYFOSSILMUD = registerBlock("mossyfossilmud",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.DIRT)
                    .strength(0.5f),
                    UniformInt.of(3, 7)), ModCreativeModeTab.BygoneCreatures_TAB);

    public static final RegistryObject<Block> DODOEGGCRATE = registerBlock("dodoeggcrate",
            () -> new Block(BlockBehaviour.Properties.of(Material.DECORATION)
                    .strength(0f)), ModCreativeModeTab.BygoneCreatures_TAB);

    public static final RegistryObject<Block> SLEEKSTONE = registerBlock("sleekstone",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.DECORATION)
                    .strength(2f),
                    UniformInt.of(3, 7)), ModCreativeModeTab.BygoneCreatures_TAB);


    public static final RegistryObject<Block> SLEEKSTONEPEBBLE = registerBlock("sleekstonepebble",
            () -> new sleekstonepebble(BlockBehaviour.Properties.of(Material.SAND)
                    .strength(0f).noOcclusion()), ModCreativeModeTab.BygoneCreatures_TAB);

    public static final RegistryObject<Block> KINGSAGO = registerBlock("kingsago",
            () -> new kingsago(BlockBehaviour.Properties.of(Material.DECORATION)
                    .strength(0.5f).noOcclusion()), ModCreativeModeTab.BygoneCreatures_TAB);

    public static final RegistryObject<Block> DESERTSCRUB = registerBlock("desertscrub",
            () -> new DeadBushBlock(
                    BlockBehaviour.Properties.copy(Blocks.DEAD_BUSH)), ModCreativeModeTab.BygoneCreatures_TAB);


    //.requiresCorrectToolForDrops()  USE THIS!!!


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}