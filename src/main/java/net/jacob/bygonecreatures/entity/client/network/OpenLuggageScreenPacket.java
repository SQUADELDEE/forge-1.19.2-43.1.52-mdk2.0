package net.jacob.bygonecreatures.entity.client.network;

import net.jacob.bygonecreatures.entity.client.GMenu;
import net.jacob.bygonecreatures.entity.client.LuggageScreen;
import net.jacob.bygonecreatures.entity.custom.GlyptodonEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenLuggageScreenPacket {
    private final int containerId;
    private final int entityId;

    public OpenLuggageScreenPacket(int containerId, int entityId) {
        this.containerId = containerId;
        this.entityId = entityId;
    }

    public OpenLuggageScreenPacket(FriendlyByteBuf buf) {
        this.containerId = buf.readUnsignedByte();
        this.entityId = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeInt(this.entityId);
    }

    public static class Handler {

        @SuppressWarnings("Convert2Lambda")
        public static void onMessage(OpenLuggageScreenPacket message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(new Runnable() {
                @Override
                public void run() {
                    assert Minecraft.getInstance().level != null;
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityId);
                    if (entity instanceof GlyptodonEntity luggage) {
                        LocalPlayer localplayer = Minecraft.getInstance().player;
                        SimpleContainer simplecontainer = new SimpleContainer( 27);
                        assert localplayer != null;
                        GMenu menu = new GMenu(message.containerId, localplayer.getInventory(), simplecontainer, luggage);
                        localplayer.containerMenu = menu;
                        Minecraft.getInstance().setScreen(new LuggageScreen(menu, localplayer.getInventory(), luggage));
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
