package org.iouter.sophisticatednetmusic.netmusicupgrade;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SophisticatedStopMusicToClientMessage {
    private final UUID uuid;

    public SophisticatedStopMusicToClientMessage(UUID uuid) {
        this.uuid = uuid;
    }

    public static SophisticatedStopMusicToClientMessage decode(FriendlyByteBuf buf) {
        return new SophisticatedStopMusicToClientMessage(UUID.fromString(buf.readUtf()));
    }

    public static void encode(SophisticatedStopMusicToClientMessage message, FriendlyByteBuf buf) {
        buf.writeUtf(message.uuid.toString());
    }

    public static void handle(SophisticatedStopMusicToClientMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(SophisticatedStopMusicToClientMessage message) {
        SophisticatedNetMusicSound.setStop(message.uuid);
    }
}
