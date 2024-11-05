package net.chiuchohin.biomedependentores.worldgen;

import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.chiuchohin.biomedependentores.BiomeDependentOre;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;


public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> TEST_ORE = registerKey("rm_ore_redstone");
    public static final Map<String , String> RESOURCE_MAP = Map.of(
        "a", "b",
        "c", "d"
    );
    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        unRegisterOre(context, BiomeTags.IS_OVERWORLD, TEST_ORE, ModPlacedFeatures.registerKey("ore_redstone"), Set.of(GenerationStep.Decoration.UNDERGROUND_ORES));
        //registerOre(context, BiomeTags.IS_OVERWORLD, TEST_ORE, ModPlacedFeatures.SAPPHIRE_ORE_PLACED_KEY);
        // RESOURCE_MAP.forEach((key, value) -> {
        //     registerOre(context, BiomeTags.IS_OVERWORLD, ADD_SAPPHIRE_ORE, ModPlacedFeatures.SAPPHIRE_ORE_PLACED_KEY);
        // });
    }

    // void onGatherData(GatherDataEvent event)
    // {
    // DataGenerator generator = event.getDataGenerator();
    // ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
    // RegistryAccess registryAccess = RegistryAccess.builtinCopy();
    // RegistryOps<JsonElement> registryOps = RegistryOps.create(JsonOps.INSTANCE, registryAccess);

    // ResourceLocation placedFeatureRL = new ResourceLocation("modid", "sponge_everywhere");
    // PlacedFeature placedFeature = new PlacedFeature(//etc;)
    // // All placed features to be datagenerated can be in the map
    // Map<ResourceLocation, PlacedFeature> map = Map.of(placedFeatureRL, placedFeature);

    // JsonCodecProvider provider = JsonCodecProvider.forDatapackRegistry(
    //     dataGenerator, existingFileHelper, "modid", registryOps, Registry.PLACED_FEATURE_REGISTRY, map);

    // generator.addProvider(event.includeServer(), provider);
    // }

    public static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(BiomeDependentOre.MODID, name));
    }

    private static void registerOre(BootstapContext<BiomeModifier> context, TagKey<Biome> biomeTag, ResourceKey<BiomeModifier> resourceKey, ResourceKey<PlacedFeature> placedFeatureKey){
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        context.register(resourceKey, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
            biomes.getOrThrow(biomeTag),
            HolderSet.direct(placedFeatures.getOrThrow(placedFeatureKey)),
            GenerationStep.Decoration.UNDERGROUND_ORES
        ));

    }

    private static void unRegisterOre(BootstapContext<BiomeModifier> context, TagKey<Biome> biomeTag, ResourceKey<BiomeModifier> resourceKey, ResourceKey<PlacedFeature> placedFeatureKey, Set<Decoration> decorationSet){
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);
        context.register(resourceKey, new ForgeBiomeModifiers.RemoveFeaturesBiomeModifier(
            biomes.getOrThrow(biomeTag),
            HolderSet.direct(placedFeatures.getOrThrow(placedFeatureKey)),
            decorationSet
            )
        );
    }
}