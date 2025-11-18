package org.iouter.sophisticatednetmusic.data;

import com.github.tartaricacid.netmusic.init.InitBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedcore.crafting.ShapeBasedRecipeBuilder;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.util.RegistryHelper;
import net.p3pp3rf1y.sophisticatedstorage.SophisticatedStorage;
import org.iouter.sophisticatednetmusic.init.ModItems;

import java.util.function.Consumer;

public class ModRecipes extends RecipeProvider {

    public ModRecipes(DataGenerator generator) {
        super(generator.getPackOutput());
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> recipeConsumer) {
        addRecipes(recipeConsumer);
    }

    private void addRecipes(Consumer<FinishedRecipe> consumer) {
        ShapeBasedRecipeBuilder.shaped(ModItems.NET_MUSIC_UPGRADE_STORAGE.get())
                .pattern(" J ")
                .pattern("IBI")
                .pattern(" R ")
                .define('B', net.p3pp3rf1y.sophisticatedstorage.init.ModItems.UPGRADE_BASE.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('J', InitBlocks.MUSIC_PLAYER.get())
                .unlockedBy("has_upgrade_base", has(net.p3pp3rf1y.sophisticatedstorage.init.ModItems.UPGRADE_BASE.get()))
                .condition(new ModLoadedCondition(SophisticatedStorage.MOD_ID))
                .save(consumer);
        ShapeBasedRecipeBuilder.shaped(ModItems.ADVANCED_NET_MUSIC_UPGRADE_STORAGE.get(), net.p3pp3rf1y.sophisticatedcore.init.ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
                .pattern(" D ")
                .pattern("GJG")
                .pattern("RRR")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('J', ModItems.NET_MUSIC_UPGRADE_STORAGE.get())
                .unlockedBy("has_net_music_upgrade", has(ModItems.NET_MUSIC_UPGRADE_STORAGE.get()))
                .condition(new ModLoadedCondition(SophisticatedStorage.MOD_ID))
                .save(consumer);
        ShapeBasedRecipeBuilder.shaped(ModItems.NET_MUSIC_UPGRADE_BACKPACKS.get())
                .pattern(" J ")
                .pattern("IBI")
                .pattern(" R ")
                .define('B', net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems.UPGRADE_BASE.get())
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('I', Tags.Items.INGOTS_IRON)
                .define('J', InitBlocks.MUSIC_PLAYER.get())
                .unlockedBy("has_upgrade_base", has(net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems.UPGRADE_BASE.get()))
                .condition(new ModLoadedCondition(SophisticatedBackpacks.MOD_ID))
                .save(consumer);
        ShapeBasedRecipeBuilder.shaped(ModItems.ADVANCED_NET_MUSIC_UPGRADE_BACKPACKS.get(), net.p3pp3rf1y.sophisticatedcore.init.ModRecipes.UPGRADE_NEXT_TIER_SERIALIZER.get())
                .pattern(" D ")
                .pattern("GJG")
                .pattern("RRR")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('R', Tags.Items.DUSTS_REDSTONE)
                .define('J', ModItems.NET_MUSIC_UPGRADE_BACKPACKS.get())
                .unlockedBy("has_net_music_upgrade", has(ModItems.NET_MUSIC_UPGRADE_BACKPACKS.get()))
                .condition(new ModLoadedCondition(SophisticatedBackpacks.MOD_ID))
                .save(consumer);
        addStorageUpgradeFromBackpackUpgradeRecipe(consumer, ModItems.NET_MUSIC_UPGRADE_STORAGE.get(), ModItems.NET_MUSIC_UPGRADE_BACKPACKS.get());
        addStorageUpgradeFromBackpackUpgradeRecipe(consumer, ModItems.ADVANCED_NET_MUSIC_UPGRADE_STORAGE.get(), ModItems.ADVANCED_NET_MUSIC_UPGRADE_BACKPACKS.get());
        addBackpackUpgradeFromStorageUpgradeRecipe(consumer, ModItems.NET_MUSIC_UPGRADE_BACKPACKS.get(), ModItems.NET_MUSIC_UPGRADE_STORAGE.get());
        addBackpackUpgradeFromStorageUpgradeRecipe(consumer, ModItems.ADVANCED_NET_MUSIC_UPGRADE_BACKPACKS.get(), ModItems.ADVANCED_NET_MUSIC_UPGRADE_STORAGE.get());
    }

    private void addStorageUpgradeFromBackpackUpgradeRecipe(Consumer<FinishedRecipe> consumer, UpgradeItemBase<?> storageUpgrade, UpgradeItemBase<?> backpackUpgrade) {
        ShapeBasedRecipeBuilder.shaped(storageUpgrade)
                .pattern("PUP")
                .pattern(" P ")
                .pattern("P P")
                .define('P', ItemTags.PLANKS)
                .define('U', backpackUpgrade)
                .unlockedBy("has_backpack_upgrade", has(backpackUpgrade))
                .condition(new ModLoadedCondition(SophisticatedStorage.MOD_ID))
                .condition(new ModLoadedCondition(SophisticatedBackpacks.MOD_ID))
                .save(consumer, SophisticatedStorage.getRL("storage_" + RegistryHelper.getItemKey(storageUpgrade).getPath() + "_from_backpack_" + RegistryHelper.getItemKey(backpackUpgrade).getPath()));
    }

    private void addBackpackUpgradeFromStorageUpgradeRecipe(Consumer<FinishedRecipe> consumer, UpgradeItemBase<?> backpackUpgrade, UpgradeItemBase<?> storageUpgrade) {
        ShapeBasedRecipeBuilder.shaped(backpackUpgrade)
                .pattern("TUT")
                .pattern(" L ")
                .pattern("T T")
                .define('T', Tags.Items.STRING)
                .define('L', Tags.Items.LEATHER)
                .define('U', storageUpgrade)
                .unlockedBy("has_storage_upgrade", has(storageUpgrade))
                .condition(new ModLoadedCondition(SophisticatedStorage.MOD_ID))
                .condition(new ModLoadedCondition(SophisticatedBackpacks.MOD_ID))
                .save(consumer, SophisticatedStorage.getRL("backpack_" + RegistryHelper.getItemKey(backpackUpgrade).getPath() + "_from_storage_" + RegistryHelper.getItemKey(storageUpgrade).getPath()));
    }
}
