package net.chiuchohin.biomedependentores.datagen;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.chiuchohin.biomedependentores.BiomeDependentOre;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

@Mod.EventBusSubscriber(modid = BiomeDependentOre.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        DatapackBuiltinEntriesProvider test = new DatapackBuiltinEntriesProvider(
            packOutput,
            event.getLookupProvider(),
            // The builder containing the datapack registry objects to generate
            new RegistrySetBuilder()
            // Create configured features
            .add(Registries.CONFIGURED_FEATURE, bootstrap -> {
                // Register configured features here
                new ConfiguredFeature<>(
                    Feature.ORE, // Create an ore feature
                    new OreConfiguration(
                        List.of(
                            OreConfiguration.target(stoneReplaceable, Blocks.GLOWSTONE.defaultBlockState())
                        ), // Does nothing
                        8 // in veins of at most 8
                    )
                );
            })
            // Create placed features
            .add(Registries.PLACED_FEATURE, bootstrap -> {
                // Register placed features here

            })
            ,
            // Set of mod ids to generate the datapack registry objects of
            Set.of(BiomeDependentOre.MODID)
        );
        generator.addProvider(
            // Tell generator to run only when server data are generating
            event.includeServer(),
            test
        );
        //DataGenerator generator = event.getGenerator();

        //ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

    }

    public static final ResourceKey<ConfiguredFeature<?, ?>> EXAMPLE_CONFIGURED_FEATURE = ResourceKey.create(
        Registries.CONFIGURED_FEATURE,
        new ResourceLocation(BiomeDependentOre.MODID, "example_configured_feature")
    );

    public static final ResourceKey<PlacedFeature> EXAMPLE_PLACED_FEATURE = ResourceKey.create(
    Registries.PLACED_FEATURE,
    new ResourceLocation(BiomeDependentOre.MODID, "example_placed_feature")
    );
    
}