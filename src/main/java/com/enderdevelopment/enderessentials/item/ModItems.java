package com.enderdevelopment.enderessentials.item;

import com.enderdevelopment.enderessentials.EnderEssentials;
import com.enderdevelopment.enderessentials.mob.ModMobs;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * Registers all items for the mod, including big melee weapons and spawn eggs.
 */
public class ModItems {
	public static final Item DRAGON_GREATSWORD = registerItem("dragon_greatsword",
			new SwordItem(ToolMaterials.NETHERITE, 9, -2.8f,
					new FabricItemSettings().maxCount(1)));

	public static final Item TITAN_CLEAVER = registerItem("titan_cleaver",
			new SwordItem(ToolMaterials.DIAMOND, 11, -3.2f,
					new FabricItemSettings().maxCount(1)));

	public static final Item OBSIDIAN_WARHAMMER = registerItem("obsidian_warhammer",
			new SwordItem(ToolMaterials.NETHERITE, 14, -3.5f,
					new FabricItemSettings().maxCount(1)));

	public static final Item MYTHRIL_LONGSWORD = registerItem("mythril_longsword",
			new SwordItem(ToolMaterials.DIAMOND, 7, -2.4f,
					new FabricItemSettings().maxCount(1)));

	public static final Item WARRIOR_BEAST_SPAWN_EGG = registerItem("warrior_beast_spawn_egg",
			new SpawnEggItem(ModMobs.WARRIOR_BEAST, 0x4a1a1a, 0xc84c00,
					new FabricItemSettings()));

	public static final Item PET_WOLF_SPAWN_EGG = registerItem("pet_wolf_spawn_egg",
			new SpawnEggItem(ModMobs.PET_WOLF, 0xdedede, 0x7a7a7a,
					new FabricItemSettings()));

	public static void register() {
		EnderEssentials.LOGGER.info("Registered {} items", 6);
	}

	private static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, EnderEssentials.id(name), item);
	}
}
