package net.jacob.bygonecreatures.item;


import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.ModEntityTypes;
import net.jacob.bygonecreatures.item.custom.BreatherSet;
import net.jacob.bygonecreatures.item.custom.DodoEggItem;
import net.jacob.bygonecreatures.item.custom.DragonflyBootsItem;
import net.jacob.bygonecreatures.item.custom.ItemModFishBucket;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BygoneCreatures.MOD_ID);

    public static final RegistryObject<Item> DUNG = ITEMS.register("dung",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));
    public static final RegistryObject<Item> DODOEGG = ITEMS.register("dodoegg",
            () -> new DodoEggItem(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> DODOMEAT = ITEMS.register("dodomeat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).food(ModFoods.DODOMEAT)));

    public static final RegistryObject<Item> AMBER = ITEMS.register("amber",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> GLYPTODONSCUTE = ITEMS.register("glyptodonscute",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> WHEATONASTICK = ITEMS.register("wheatonastick",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).stacksTo(1)));

    public static final RegistryObject<Item> ANEMONEGEL = ITEMS.register("anemonegel",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> SAGOCONE = ITEMS.register("sagocone",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> DRAGONFLYCARAPACE = ITEMS.register("dragonflycarapace",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> PROTOSHED = ITEMS.register("protoshed",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> ITYPESKULL = ITEMS.register("itypeskull",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> EMBRYOCORE = ITEMS.register("embryocore",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> GILDEDPROTOSTEAK = ITEMS.register("gildedprotosteak",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> ITYPETOOTH = ITEMS.register("itypetooth",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> ARMOREDFISH = ITEMS.register("armoredfish",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).food(ModFoods.ARMOREDFISH)));

    public static final RegistryObject<Item> COOKEDARMOREDFISH = ITEMS.register("cookedarmoredfish",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).food(ModFoods.COOKEDARMOREDFISH)));

    public static final RegistryObject<Item> COOKEDDODOMEAT = ITEMS.register("cookeddodomeat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).food(ModFoods.COOKEDDODOMEAT)));
    public static final RegistryObject<Item> COOKEDGLYPTODONMEAT = ITEMS.register("cookedglyptodonmeat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).food(ModFoods.COOKEDGLYPTODONMEAT)));
    public static final RegistryObject<Item> COOKEDDODOEGG = ITEMS.register("cookeddodoegg",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).food(ModFoods.COOKEDDODOEGG)));
    public static final RegistryObject<Item> GLYPTODONMEAT = ITEMS.register("glyptodonmeat",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).food(ModFoods.GLYPTODONMEAT)));

    public static final RegistryObject<Item> PROTOSTEAK = ITEMS.register("protosteak",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).food(ModFoods.PROTOSTEAK)));

    public static final RegistryObject<Item> COOKEDPROTOSTEAK = ITEMS.register("cookedprotosteak",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).food(ModFoods.COOKEDPROTOSTEAK)));

    public static final RegistryObject<Item> BONECLEAVER = ITEMS.register("bonecleaver",
            () -> new SwordItem(Tiers.DIAMOND, 3, 0.05f, new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB).stacksTo(1).fireResistant()));

    public static final RegistryObject<Item> DRAGONFLYBOOTS = ITEMS.register("dragonflyboots",
            () -> new DragonflyBootsItem(ModArmorMaterials.DRAGONFLY, EquipmentSlot.FEET,  new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item>  BREATHERBAG = ITEMS.register("breatherbag",
            () -> new BreatherSet(ModArmorMaterials.BREATHER, EquipmentSlot.CHEST,  new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));


    public static final RegistryObject<Item> ARMOREDFISHBUCKET = ITEMS.register("armoredfishbucket", () -> new ItemModFishBucket(ModEntityTypes.ARMOREDFISH, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));
    public static final RegistryObject<Item> CEPHABUCKET = ITEMS.register("cephabucket", () -> new ItemModFishBucket(ModEntityTypes.CEPHALASPIS, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> KEMBUCKET = ITEMS.register("kemkembucket", () -> new ItemModFishBucket(ModEntityTypes.KEMKEM, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> ITCHBUCKET = ITEMS.register("itchbucket", () -> new ItemModFishBucket(ModEntityTypes.ICHTHYOSAUR, Fluids.WATER, new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> DODOSPAWNEGG = ITEMS.register("dodospawnegg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.DODO,0x744700, 0xeeeeee,
                    new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));

    public static final RegistryObject<Item> ARMOREDFISHSPAWNEGG = ITEMS.register("armoredfishspawnegg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.ARMOREDFISH,0xf54f4f, 0x5b5b5b,
                    new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));
    public static final RegistryObject<Item> CEPHASPAWNEGG = ITEMS.register("cephaspawnegg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.CEPHALASPIS,0x38761d, 0xffd700,
                    new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));
    public static final RegistryObject<Item> TERRORBIRDSPAWNEGG = ITEMS.register("terrorbirdspawnegg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.TERRORBIRD,0x073763, 0xf1c232,
                    new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));
    public static final RegistryObject<Item> GLYPTODONSPAWNEGG = ITEMS.register("glyptodonspawnegg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.GLYPTODON,0xff870a, 0xffd700,
                    new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));
    public static final RegistryObject<Item> RAPTORSPAWNEGG = ITEMS.register("raptorspawnegg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.RAPTOR,0x7f6000, 0xbf9000,
                    new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));
    public static final RegistryObject<Item> DRAGONFLYSPAWNEGG = ITEMS.register("dragonflyspawnegg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.DRAGONFLY,0x0b5394, 0x3d85c6,
                    new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));
    public static final RegistryObject<Item> ITCHYSPAWNEGG = ITEMS.register("itchyspawnegg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.ICHTHYOSAUR,0x0b5394, 0xeeeeee,
                    new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));
    public static final RegistryObject<Item> KEMKEMSPAWNEGG = ITEMS.register("kemkemspawnegg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.KEMKEM,0x5b5b5b, 0xeeeeee,
                    new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));
    public static final RegistryObject<Item> PROTOSPAWNEGG = ITEMS.register("protospawnegg",
            () -> new ForgeSpawnEggItem(ModEntityTypes.PROTOCERATOPS,0xffd700, 0xbf9000,
                    new Item.Properties().tab(ModCreativeModeTab.BygoneCreatures_TAB)));








    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}