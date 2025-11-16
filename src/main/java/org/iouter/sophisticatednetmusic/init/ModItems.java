package org.iouter.sophisticatednetmusic.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeGuiManager;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerRegistry;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerType;
import net.p3pp3rf1y.sophisticatedcore.util.ItemBase;
import net.p3pp3rf1y.sophisticatedstorage.Config;
import org.iouter.sophisticatednetmusic.SophisticatedNetMusic;
import org.iouter.sophisticatednetmusic.netmusicupgrade.NetMusicUpgradeContainer;
import org.iouter.sophisticatednetmusic.netmusicupgrade.NetMusicUpgradeItem;
import org.iouter.sophisticatednetmusic.netmusicupgrade.NetMusicUpgradeTab;
import org.iouter.sophisticatednetmusic.netmusicupgrade.NetMusicUpgradeWrapper;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SophisticatedNetMusic.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB.location(), SophisticatedNetMusic.MODID);
    public static final RegistryObject<NetMusicUpgradeItem> NET_MUSIC_UPGRADE_STORAGE;
    public static final RegistryObject<NetMusicUpgradeItem> ADVANCED_NET_MUSIC_UPGRADE_STORAGE;
    public static final RegistryObject<NetMusicUpgradeItem> NET_MUSIC_UPGRADE_BACKPACKS;
    public static final RegistryObject<NetMusicUpgradeItem> ADVANCED_NET_MUSIC_UPGRADE_BACKPACKS;
    private static final UpgradeContainerType<NetMusicUpgradeWrapper, NetMusicUpgradeContainer> NET_MUSIC_TYPE = new UpgradeContainerType<>(NetMusicUpgradeContainer::new);
    private static final UpgradeContainerType<NetMusicUpgradeWrapper, NetMusicUpgradeContainer> ADVANCED_NET_MUSIC_TYPE = new UpgradeContainerType<>(NetMusicUpgradeContainer::new);
    public static RegistryObject<CreativeModeTab> CREATIVE_TAB;

    static {
        if (SophisticatedNetMusic.isSophisticatedStorageLoaded) {
            NET_MUSIC_UPGRADE_STORAGE = ITEMS.register("net_music_upgrade_storage", () -> new NetMusicUpgradeItem(() -> 1, () -> 1));
            ADVANCED_NET_MUSIC_UPGRADE_STORAGE = ITEMS.register("advanced_net_music_upgrade_storage", () -> new NetMusicUpgradeItem(Config.SERVER.advancedJukeboxUpgrade.numberOfSlots::get, Config.SERVER.advancedJukeboxUpgrade.slotsInRow::get));
        } else {
            NET_MUSIC_UPGRADE_STORAGE = null;
            ADVANCED_NET_MUSIC_UPGRADE_STORAGE = null;
        }
        if (SophisticatedNetMusic.isSophisticatedBackpacksLoaded) {
            NET_MUSIC_UPGRADE_BACKPACKS = ITEMS.register("net_music_upgrade_backpacks", () -> new NetMusicUpgradeItem(() -> 1, () -> 1));
            ADVANCED_NET_MUSIC_UPGRADE_BACKPACKS = ITEMS.register("advanced_net_music_upgrade_backpacks", () -> new NetMusicUpgradeItem(net.p3pp3rf1y.sophisticatedbackpacks.Config.SERVER.advancedJukeboxUpgrade.numberOfSlots::get, net.p3pp3rf1y.sophisticatedbackpacks.Config.SERVER.advancedJukeboxUpgrade.slotsInRow::get));
        } else {
            NET_MUSIC_UPGRADE_BACKPACKS = null;
            ADVANCED_NET_MUSIC_UPGRADE_BACKPACKS = null;
        }
        if (SophisticatedNetMusic.isSophisticatedStorageLoaded) {
            CREATIVE_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder().icon(() -> NET_MUSIC_UPGRADE_STORAGE.get().getDefaultInstance()).title(Component.translatable("itemGroup." + SophisticatedNetMusic.MODID)).displayItems((featureFlags, output) -> {
                ITEMS.getEntries().stream().filter(i -> i.get() instanceof ItemBase).forEach(i -> ((ItemBase) i.get()).addCreativeTabItems(output::accept));
            }).build());
        } else if (SophisticatedNetMusic.isSophisticatedBackpacksLoaded) {
            CREATIVE_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder().icon(() -> NET_MUSIC_UPGRADE_BACKPACKS.get().getDefaultInstance()).title(Component.translatable("itemGroup." + SophisticatedNetMusic.MODID)).displayItems((featureFlags, output) -> {
                ITEMS.getEntries().stream().filter(i -> i.get() instanceof ItemBase).forEach(i -> ((ItemBase) i.get()).addCreativeTabItems(output::accept));
            }).build());
        } else {
            throw new RuntimeException("Sophisticated Net Music needs Sophisticated Storage or Sophisticated Backpacks");
        }
    }

    private ModItems() {
    }

    public static void registerHandlers(IEventBus modBus) {
        ITEMS.register(modBus);
        CREATIVE_MODE_TABS.register(modBus);
        modBus.addListener(ModItems::registerContainers);
    }

    public static void registerContainers(RegisterEvent event) {
        if (SophisticatedNetMusic.isSophisticatedStorageLoaded) {
            UpgradeContainerRegistry.register(NET_MUSIC_UPGRADE_STORAGE.getId(), NET_MUSIC_TYPE);
            UpgradeContainerRegistry.register(ADVANCED_NET_MUSIC_UPGRADE_STORAGE.getId(), ADVANCED_NET_MUSIC_TYPE);
        }
        if (SophisticatedNetMusic.isSophisticatedBackpacksLoaded) {
            UpgradeContainerRegistry.register(NET_MUSIC_UPGRADE_BACKPACKS.getId(), NET_MUSIC_TYPE);
            UpgradeContainerRegistry.register(ADVANCED_NET_MUSIC_UPGRADE_BACKPACKS.getId(), ADVANCED_NET_MUSIC_TYPE);
        }


        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            UpgradeGuiManager.registerTab(NET_MUSIC_TYPE, NetMusicUpgradeTab.Basic::new);
            UpgradeGuiManager.registerTab(ADVANCED_NET_MUSIC_TYPE, (NetMusicUpgradeContainer uc, Position p, StorageScreenBase<?> s) -> new NetMusicUpgradeTab.Advanced(uc, p, s, Config.SERVER.advancedJukeboxUpgrade.slotsInRow.get()));
        });
    }
}
