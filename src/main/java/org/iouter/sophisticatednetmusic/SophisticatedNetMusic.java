package org.iouter.sophisticatednetmusic;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.iouter.sophisticatednetmusic.init.ModItems;
import org.iouter.sophisticatednetmusic.init.NetworkHandler;
import org.iouter.sophisticatednetmusic.netmusicupgrade.ServerMusicSoundHandler;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SophisticatedNetMusic.MODID)
public class SophisticatedNetMusic {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "sophisticatednetmusic";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final boolean isSophisticatedStorageLoaded = ModList.get().isLoaded("sophisticatedstorage");
    public static final boolean isSophisticatedBackpacksLoaded = ModList.get().isLoaded("sophisticatedbackpacks");

    public SophisticatedNetMusic() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        ModItems.registerHandlers(modEventBus);

        NetworkHandler.init();
        ServerMusicSoundHandler.init();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Sophisticated Net Music: glad to see you!");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }
}
