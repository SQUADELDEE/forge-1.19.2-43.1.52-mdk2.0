package net.jacob.bygonecreatures.entity.client.network;

import net.jacob.bygonecreatures.BygoneCreatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class LuggageNetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(BygoneCreatures.MOD_ID, "channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    @SuppressWarnings("UnusedAssignment")
    public static void init() {
        int id = 0;

        CHANNEL.registerMessage(id++, OpenLuggageScreenPacket.class, OpenLuggageScreenPacket::encode, OpenLuggageScreenPacket::new, OpenLuggageScreenPacket.Handler::onMessage);

    }
}

