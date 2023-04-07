package net.jacob.bygonecreatures.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab BygoneCreatures_TAB = new CreativeModeTab("bygonecreaturestab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.EMBRYOCORE.get());
        }
    };
}
