package net.jacob.bygonecreatures.entity.client;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.jacob.bygonecreatures.entity.custom.ArgenEntity;
import net.jacob.bygonecreatures.entity.util.BCConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class KeyMap
{
    private static final List<KeyMapping> REGISTRY = new ArrayList<>();

    public static final KeyMapping FLIGHT_DESCENT = keymap("flight_descent", GLFW.GLFW_KEY_Z, "key.categories.movement");
    public static final KeyMapping CAMERA_CONTROLS = keymap("camera_flight", GLFW.GLFW_KEY_F6, "key.categories.movement");







    private static KeyMapping keymap(String name, int defaultMapping, String category)
    {
        var keymap = new KeyMapping(String.format("key.%s.%s", BygoneCreatures.MOD_ID, name), defaultMapping, category);
        REGISTRY.add(keymap);
        return keymap;
    }

    public static void register(Consumer<KeyMapping> method)
    {
        REGISTRY.forEach(method);
    }
}
