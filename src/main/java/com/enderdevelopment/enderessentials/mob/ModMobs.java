package com.enderdevelopment.enderessentials.mob;

import com.enderdevelopment.enderessentials.EnderEssentials;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WolfEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.biome.BiomeKeys;

/**
 * Registers all custom mobs for the mod.
 */
public class ModMobs {

	// A dangerous "Warrior Beast" hostile mob
	public static final EntityType<WarriorBeastEntity> WARRIOR_BEAST = Registry.register(
			Registries.ENTITY_TYPE,
			EnderEssentials.id("warrior_beast"),
			FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WarriorBeastEntity::new)
					.dimensions(EntityDimensions.fixed(1.4f, 1.6f))
					.maxTrackingRange(80)
					.build());

	// A friendly pet entity
	public static final EntityType<PetWolfEntity> PET_WOLF = Registry.register(
			Registries.ENTITY_TYPE,
			EnderEssentials.id("pet_wolf"),
			FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, PetWolfEntity::new)
					.dimensions(EntityDimensions.fixed(0.6f, 0.85f))
					.maxTrackingRange(80)
					.build());

	public static void register() {
		FabricDefaultAttributeRegistry.register(WARRIOR_BEAST, createWarriorBeastAttributes());
		FabricDefaultAttributeRegistry.register(PET_WOLF, createPetWolfAttributes());

		// Spawn warrior beasts in dangerous biomes at night
		BiomeModifications.addSpawn(
				BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST, BiomeKeys.DEEP_DARK,
						BiomeKeys.NETHER_WASTES, BiomeKeys.SOUL_SAND_VALLEY),
				SpawnGroup.MONSTER,
				WARRIOR_BEAST,
				20, 1, 3);

		// Spawn pet wolves in plains and forests
		BiomeModifications.addSpawn(
				BiomeSelectors.includeByKey(BiomeKeys.PLAINS, BiomeKeys.FOREST, BiomeKeys.BIRCH_FOREST),
				SpawnGroup.CREATURE,
				PET_WOLF,
				10, 1, 2);

		EnderEssentials.LOGGER.info("Registered custom mobs");
	}

	private static DefaultAttributeContainer createWarriorBeastAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 60.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 10.0D)
				.add(EntityAttributes.GENERIC_ARMOR, 8.0D)
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 40.0D)
				.build();
	}

	private static DefaultAttributeContainer createPetWolfAttributes() {
		return AnimalEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3D)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0D)
				.build();
	}
}
