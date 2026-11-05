package com.enderdevelopment.enderessentials.command;

import com.enderdevelopment.enderessentials.EnderEssentials;
import com.enderdevelopment.enderessentials.item.ModItems;
import com.enderdevelopment.enderessentials.mob.ModMobs;
import com.enderdevelopment.enderessentials.mob.PetWolfEntity;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.server.command.CommandManager.literal;

/**
 * Registers convenience commands for pets and giving the mod's weapons.
 */
public class ModCommands {

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(literal("mod")
				.then(literal("give")
						.then(literal("greatsword").executes(ctx -> giveItem(ctx.getSource(), "dragon_greatsword")))
						.then(literal("cleaver").executes(ctx -> giveItem(ctx.getSource(), "titan_cleaver")))
						.then(literal("warhammer").executes(ctx -> giveItem(ctx.getSource(), "obsidian_warhammer")))
						.then(literal("longsword").executes(ctx -> giveItem(ctx.getSource(), "mythril_longsword"))))
				.then(literal("pet")
						.then(literal("spawn").executes(ctx -> spawnPet(ctx.getSource())))));

		EnderEssentials.LOGGER.info("Registered mod commands");
	}

	private static int giveItem(ServerCommandSource source, String itemName) {
		ItemStack stack = switch (itemName) {
			case "dragon_greatsword" -> new ItemStack(ModItems.DRAGON_GREATSWORD);
			case "titan_cleaver" -> new ItemStack(ModItems.TITAN_CLEAVER);
			case "obsidian_warhammer" -> new ItemStack(ModItems.OBSIDIAN_WARHAMMER);
			case "mythril_longsword" -> new ItemStack(ModItems.MYTHRIL_LONGSWORD);
			default -> ItemStack.EMPTY;
		};

		ServerPlayerEntity player = source.getPlayer();
		if (!stack.isEmpty()) {
			player.getInventory().offerOrDrop(stack);
			source.sendFeedback(() -> Text.literal("Gave you " + stack.getItem().getName().getString()), false);
			return 1;
		}
		return 0;
	}

	private static int spawnPet(ServerCommandSource source) {
		ServerPlayerEntity player = source.getPlayer();
		ServerWorld world = (ServerWorld) player.getWorld();
		Vec3d pos = player.getPos();

		PetWolfEntity pet = new PetWolfEntity(ModMobs.PET_WOLF, world);
		pet.setPosition(pos.x, pos.y + 1, pos.z);
		pet.setTamed(true);
		pet.setOwnerUuid(player.getUuid());
		world.spawnEntity(pet);

		source.sendFeedback(() -> Text.literal("Your pet has been summoned!"), false);
		return 1;
	}
}
