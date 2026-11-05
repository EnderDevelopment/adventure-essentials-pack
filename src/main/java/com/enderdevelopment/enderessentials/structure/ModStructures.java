package com.enderdevelopment.enderessentials.structure;

import com.enderdevelopment.enderessentials.EnderEssentials;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.PlacedFeature;

/**
 * Registers many procedurally-generated structures that spawn across the world.
 * These build from simple block patterns in code so no binary .nbt files are needed.
 */
public class ModStructures {
	// Simple block-palette based structures
	public static final String[] STRUCTURE_NAMES = {
			"ancient_dragon_den", "crystal_ruins", "abandoned_blacksmith",
			"underground_bunker", "sky_temple", "overgrown_tower",
			"ice_citadel", "desert_pyramid_revamped", "wizard_spire", "pirate_cove"
	};

	/**
	 * A simple structure generator that places a themed block pattern.
	 */
	public static class StructureGenerator extends Feature<DefaultFeatureConfig> {
		private final BlockState baseBlock;
		private final BlockState accentBlock;
		private final int height;

		public StructureGenerator(BlockState baseBlock, BlockState accentBlock, int height) {
			super(DefaultFeatureConfig.CODEC);
			this.baseBlock = baseBlock;
			this.accentBlock = accentBlock;
			this.height = height;
		}

		@Override
		public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random,
				BlockPos pos, DefaultFeatureConfig config) {
			// Build a simple tower structure with the given palette
			BlockPos start = pos.down(1);
			for (int y = 0; y < this.height; y++) {
				for (int x = -3; x <= 3; x++) {
					for (int z = -3; z <= 3; z++) {
						boolean isOuter = Math.abs(x) == 3 || Math.abs(z) == 3;
						boolean isTop = y == this.height - 1;
						boolean isFloor = y == 0;
						if (isOuter || isTop || isFloor) {
							BlockState state = ((Math.abs(x) + Math.abs(z)) % 2 == 0) ? this.accentBlock : this.baseBlock;
							world.setBlockState(start.add(x, y, z), state, 3);
						}
					}
				}
			}
			return true;
		}
	}

	private static Feature<DefaultFeatureConfig> createFeature(int index) {
		// Each structure uses themed blocks
		switch (index) {
			case 0: return new StructureGenerator(net.minecraft.block.Blocks.OBSIDIAN.getDefaultState(),
					net.minecraft.block.Blocks.CRYING_OBSIDIAN.getDefaultState(), 8); // ancient_dragon_den
			case 1: return new StructureGenerator(net.minecraft.block.Blocks.AMETHYST_BLOCK.getDefaultState(),
					net.minecraft.block.Blocks.CALCITE.getDefaultState(), 6); // crystal_ruins
			case 2: return new StructureGenerator(net.minecraft.block.Blocks.STONE_BRICKS.getDefaultState(),
					net.minecraft.block.Blocks.CRACKED_STONE_BRICKS.getDefaultState(), 6); // abandoned_blacksmith
			case 3: return new StructureGenerator(net.minecraft.block.Blocks.DEEPSLATE_BRICKS.getDefaultState(),
					net.minecraft.block.Blocks.DEEPSLATE_TILES.getDefaultState(), 7); // underground_bunker
			case 4: return new StructureGenerator(net.minecraft.block.Blocks.QUARTZ_BLOCK.getDefaultState(),
					net.minecraft.block.Blocks.SMOOTH_QUARTZ.getDefaultState(), 10); // sky_temple
			case 5: return new StructureGenerator(net.minecraft.block.Blocks.MOSSY_COBBLESTONE.getDefaultState(),
					net.minecraft.block.Blocks.VINE.getDefaultState(), 8); // overgrown_tower
			case 6: return new StructureGenerator(net.minecraft.block.Blocks.PACKED_ICE.getDefaultState(),
					net.minecraft.block.Blocks.BLUE_ICE.getDefaultState(), 9); // ice_citadel
			case 7: return new StructureGenerator(net.minecraft.block.Blocks.SANDSTONE.getDefaultState(),
					net.minecraft.block.Blocks.CHISELED_SANDSTONE.getDefaultState(), 7); // desert_pyramid_revamped
			case 8: return new StructureGenerator(net.minecraft.block.Blocks.PURPUR_BLOCK.getDefaultState(),
					net.minecraft.block.Blocks.PURPUR_PILLAR.getDefaultState(), 9); // wizard_spire
			default: return new StructureGenerator(net.minecraft.block.Blocks.OAK_PLANKS.getDefaultState(),
					net.minecraft.block.Blocks.STRIPPED_OAK_LOG.getDefaultState(), 6); // pirate_cove
		}
	}

	public static void register() {
		// Register each structure feature and its worldgen placement (configured + placed)
		for (int i = 0; i < STRUCTURE_NAMES.length; i++) {
			String name = STRUCTURE_NAMES[i];
			Feature<DefaultFeatureConfig> feature = createFeature(i);

			// Register the feature
			Registry.register(Registries.FEATURE, EnderEssentials.id(name), feature);

			// Register the configured feature
			RegistryKey<PlacedFeature> placedKey = RegistryKey.of(RegistryKeys.PLACED_FEATURE,
					EnderEssentials.id(name + "_placed"));

			// Add the structure feature to all biomes
			BiomeModifications.addFeature(
					BiomeSelectors.all(),
					GenerationStep.Feature.SURFACE_STRUCTURES,
					placedKey
			);
		}
		EnderEssentials.LOGGER.info("Registered {} structural features", STRUCTURE_NAMES.length);
	}
}
