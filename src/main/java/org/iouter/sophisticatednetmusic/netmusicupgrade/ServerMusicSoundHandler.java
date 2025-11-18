package org.iouter.sophisticatednetmusic.netmusicupgrade;

import com.github.tartaricacid.netmusic.item.ItemMusicCD;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import org.iouter.sophisticatednetmusic.init.NetworkHandler;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Based on {@link net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.ServerStorageSoundHandler}
 */
public class ServerMusicSoundHandler {
    private static final int KEEP_ALIVE_CHECK_INTERVAL = 10;
    private static final Map<ResourceKey<Level>, Long> lastWorldCheck = new HashMap<>();
    private static final Map<ResourceKey<Level>, Map<UUID, KeepAliveInfo>> worldStorageSoundKeepAlive = new HashMap<>();

    private ServerMusicSoundHandler() {}

    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ServerMusicSoundHandler::tick);
    }

    public static void tick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide()) {
            return;
        }
        ServerLevel world = (ServerLevel) event.level;
        ResourceKey<Level> dim = world.dimension();
        if (lastWorldCheck.computeIfAbsent(dim, key -> world.getGameTime()) > world.getGameTime() - KEEP_ALIVE_CHECK_INTERVAL || !worldStorageSoundKeepAlive.containsKey(dim)) {
            return;
        }
        lastWorldCheck.put(dim, world.getGameTime());

        worldStorageSoundKeepAlive.get(dim).entrySet().removeIf(entry -> {
            if (entry.getValue().getLastKeepAliveTime() < world.getGameTime() - KEEP_ALIVE_CHECK_INTERVAL) {
                NetworkHandler.sendToNearby(world, entry.getValue().getLastPosition(), new SophisticatedStopMusicToClientMessage(entry.getKey()));
                return true;
            }
            return false;
        });
    }

    public static void startPlayingMusic(UUID uuid, ServerLevel level, Vec3 pos, ItemMusicCD.SongInfo songInfo, Runnable onFinishedCallback, int entityID) {
        if (entityID == -1) {
            SophisticatedMusicToClientMessage msg = new SophisticatedMusicToClientMessage(BlockPos.containing(pos), songInfo.songUrl, songInfo.songTime, songInfo.songName, uuid);
            NetworkHandler.sendToNearby(level, pos, msg);
        } else {
            SophisticatedMusicToClientEntityMessage msg = new SophisticatedMusicToClientEntityMessage(BlockPos.containing(pos), songInfo.songUrl, songInfo.songTime, songInfo.songName, uuid, entityID);
            NetworkHandler.sendToNearby(level, pos, msg);
        }

        putKeepAliveInfo(level, uuid, onFinishedCallback, pos);
    }

    public static void updateKeepAlive(UUID storageUuid, Level world, Vec3 position, Runnable onNoLongerRunning) {
        ResourceKey<Level> dim = world.dimension();
        if (!worldStorageSoundKeepAlive.containsKey(dim) || !worldStorageSoundKeepAlive.get(dim).containsKey(storageUuid)) {
            onNoLongerRunning.run();
            return;
        }
        if (worldStorageSoundKeepAlive.get(dim).containsKey(storageUuid)) {
            worldStorageSoundKeepAlive.get(dim).get(storageUuid).update(world.getGameTime(), position);
        }
    }

    private static void putKeepAliveInfo(ServerLevel serverWorld, UUID storageUuid, Runnable onFinishedHandler, Vec3 pos) {
        worldStorageSoundKeepAlive.computeIfAbsent(serverWorld.dimension(), dim -> new HashMap<>()).put(storageUuid, new KeepAliveInfo(onFinishedHandler, serverWorld.getGameTime(), pos));
    }

    public static void soundFinished(ServerLevel level, UUID storageUuid) {
        removeKeepAliveInfo(level, storageUuid, true);
    }

    public static void stopPlayingDisc(ServerLevel serverWorld, Vec3 position, UUID storageUuid) {
        removeKeepAliveInfo(serverWorld, storageUuid, false);
        NetworkHandler.sendToNearby(serverWorld, position, new SophisticatedStopMusicToClientMessage(storageUuid));
    }

    private static void removeKeepAliveInfo(ServerLevel serverWorld, UUID storageUuid, boolean finished) {
        ResourceKey<Level> dim = serverWorld.dimension();
        if (worldStorageSoundKeepAlive.containsKey(dim) && worldStorageSoundKeepAlive.get(dim).containsKey(storageUuid)) {
            KeepAliveInfo keepAliveInfo = worldStorageSoundKeepAlive.get(dim).remove(storageUuid);
            if (finished) {
                keepAliveInfo.runOnFinished();
            }
        }
    }

    private static class KeepAliveInfo {
        private final WeakReference<Runnable> onFinishedHandler;
        private long lastKeepAliveTime;
        private Vec3 lastPosition;

        private KeepAliveInfo(Runnable onFinishedHandler, long lastKeepAliveTime, Vec3 lastPosition) {
            this.onFinishedHandler = new WeakReference<>(onFinishedHandler);
            this.lastKeepAliveTime = lastKeepAliveTime;
            this.lastPosition = lastPosition;
        }

        public long getLastKeepAliveTime() {
            return lastKeepAliveTime;
        }

        public Vec3 getLastPosition() {
            return lastPosition;
        }

        public void update(long gameTime, Vec3 position) {
            lastKeepAliveTime = gameTime;
            lastPosition = position;
        }

        public void runOnFinished() {
            Runnable handler = onFinishedHandler.get();
            if (handler != null) {
                handler.run();
            }
        }
    }
}
