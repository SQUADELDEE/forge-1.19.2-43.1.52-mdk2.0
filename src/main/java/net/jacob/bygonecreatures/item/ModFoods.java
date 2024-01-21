package net.jacob.bygonecreatures.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import org.lwjgl.glfw.GLFW;

import java.util.function.BooleanSupplier;

public class ModFoods {


    public static final FoodProperties DODOMEAT = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.3F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).meat().build();
    public static final FoodProperties GLYPTODONMEAT = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.4F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).meat().build();
    public static final FoodProperties COOKEDDODOMEAT = (new FoodProperties.Builder()).nutrition(5).saturationMod(0.4F).meat().build();

    public static final FoodProperties PROTOSTEAK = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.4F).effect(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F).meat().build();
    public static final FoodProperties COOKEDPROTOSTEAK = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.4F).meat().build();

    public static final FoodProperties COOKEDDODOEGG = (new FoodProperties.Builder()).nutrition(3).saturationMod(0.4F).build();
    public static final FoodProperties COOKEDGLYPTODONMEAT = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.6F).meat().build();
    public static final FoodProperties ARMOREDFISH = (new FoodProperties.Builder()).nutrition(2).saturationMod(0.1F).build();
    public static final FoodProperties COOKEDARMOREDFISH = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.6F).build();

    public static final FoodProperties SMOLDERSTEAK = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.6F).effect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0), 0.3F).effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0), 1.0F).meat().build();
}
