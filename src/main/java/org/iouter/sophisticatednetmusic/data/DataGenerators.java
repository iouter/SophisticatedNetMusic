package org.iouter.sophisticatednetmusic.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;

/**
 * Based on {@link DataGenerators}
 */
public class DataGenerators {
    private DataGenerators() {}

    public static void gatherData(GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        generator.addProvider(evt.includeServer(), new ModRecipes(generator));
    }
}
