package org.iouter.sophisticatednetmusic.netmusicupgrade;

import net.minecraft.resources.ResourceLocation;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeGroup;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.IntSupplier;

/**
 * Based on {@link net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeItem}
 */
public class NetMusicUpgradeItem extends UpgradeItemBase<NetMusicUpgradeWrapper> {
    public static final UpgradeGroup UPGRADE_GROUP = new UpgradeGroup("net_music_upgrades", "net_music_upgrades");
    public static final UpgradeType<NetMusicUpgradeWrapper> TYPE = new UpgradeType<>(NetMusicUpgradeWrapper::new);
    private final IntSupplier numberOfSlots;
    private final IntSupplier slotsInRow;

    public NetMusicUpgradeItem(IntSupplier numberOfSlots, IntSupplier slotsInRow) {
        super(new IUpgradeCountLimitConfig() {
            @Override
            public int getMaxUpgradesPerStorage(String s, @Nullable ResourceLocation resourceLocation) {
                return 1;
            }

            @Override
            public int getMaxUpgradesInGroupPerStorage(String s, UpgradeGroup upgradeGroup) {
                return 1;
            }
        });
        this.numberOfSlots = numberOfSlots;
        this.slotsInRow = slotsInRow;
    }

    @Override
    public UpgradeType<NetMusicUpgradeWrapper> getType() {
        return TYPE;
    }

    @Override
    public List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
        return List.of();
    }

    @Override
    public UpgradeGroup getUpgradeGroup() {
        return UPGRADE_GROUP;
    }

    public int getNumberOfSlots() {
        return numberOfSlots.getAsInt();
    }

    public int getSlotsInRow() {
        return slotsInRow.getAsInt();
    }

}
