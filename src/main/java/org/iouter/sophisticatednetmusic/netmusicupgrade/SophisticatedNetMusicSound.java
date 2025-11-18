package org.iouter.sophisticatednetmusic.netmusicupgrade;

import com.github.tartaricacid.netmusic.api.lyric.LyricRecord;
import com.github.tartaricacid.netmusic.client.audio.NetMusicSound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SophisticatedNetMusicSound extends NetMusicSound {
    protected static final Map<UUID, SophisticatedNetMusicSound> sophisticatedNetMusicMap = new HashMap<>();
    private static final int SOUND_STOP_CHECK_INTERVAL = 10;
    private static long lastPlaybackChecked = 0;
    private final Entity entity;

    public SophisticatedNetMusicSound(BlockPos pos, URL songUrl, int timeSecond, @Nullable LyricRecord lyricRecord, UUID uuid, Entity entity) {
        super(pos, songUrl, timeSecond, lyricRecord);
        sophisticatedNetMusicMap.put(uuid, this);
        this.entity = entity;
    }

    public SophisticatedNetMusicSound(BlockPos pos, URL songUrl, int timeSecond, @Nullable LyricRecord lyricRecord, UUID uuid) {
        this(pos, songUrl, timeSecond, lyricRecord, uuid, null);
    }

    protected static void setStop(UUID uuid) {
        sophisticatedNetMusicMap.get(uuid).stop();
        sophisticatedNetMusicMap.remove(uuid);
    }

    @Override
    public void tick() {
        if (!sophisticatedNetMusicMap.isEmpty()) {
            Level world = Minecraft.getInstance().level;
            if (world == null) return;
            if (entity != null) {
                this.x = (float) this.entity.getX();
                this.y = (float) this.entity.getY();
                this.z = (float) this.entity.getZ();
            }
            if (lastPlaybackChecked >= world.getGameTime() - SOUND_STOP_CHECK_INTERVAL) return;
            lastPlaybackChecked = world.getGameTime();
            sophisticatedNetMusicMap.entrySet().removeIf(entry -> !Minecraft.getInstance().getSoundManager().isActive(entry.getValue()));

        }
    }
}
