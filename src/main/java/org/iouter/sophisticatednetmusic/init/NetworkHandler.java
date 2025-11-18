package org.iouter.sophisticatednetmusic.init;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.iouter.sophisticatednetmusic.SophisticatedNetMusic;
import org.iouter.sophisticatednetmusic.netmusicupgrade.SophisticatedMusicToClientMessage;
import org.iouter.sophisticatednetmusic.netmusicupgrade.SophisticatedMusicToClientEntityMessage;
import org.iouter.sophisticatednetmusic.netmusicupgrade.SophisticatedStopMusicToClientMessage;

import java.util.Optional;

/**
 * Based on {@link com.github.tartaricacid.netmusic.network.NetworkHandler}
 */
public class NetworkHandler {
    private static final String VERSION = "1.0.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(SophisticatedNetMusic.MODID, "network"),
            () -> VERSION, it -> it.equals(VERSION), it -> it.equals(VERSION));

    public static void init() {
        CHANNEL.registerMessage(0,
                SophisticatedMusicToClientMessage.class,
                SophisticatedMusicToClientMessage::encode,
                SophisticatedMusicToClientMessage::decode,
                SophisticatedMusicToClientMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(1,
                SophisticatedStopMusicToClientMessage.class,
                SophisticatedStopMusicToClientMessage::encode,
                SophisticatedStopMusicToClientMessage::decode,
                SophisticatedStopMusicToClientMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(2,
                SophisticatedMusicToClientEntityMessage.class,
                SophisticatedMusicToClientEntityMessage::encode,
                SophisticatedMusicToClientEntityMessage::decode,
                SophisticatedMusicToClientEntityMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendToNearby(Level world, Vec3 pos, Object toSend) {
        if (!(world instanceof ServerLevel ws)) {
            return;
        }

        ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(BlockPos.containing(pos)), false).stream()
                .filter(p -> p.distanceToSqr(pos.x, pos.y, pos.z) < 96 * 96)
                .forEach(p -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), toSend));
    }
}
