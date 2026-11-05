package com.enderdevelopment.enderessentials.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Provides infinite durability by preventing item damage.
 * Cancels the boolean damage() method that all damage paths funnel through.
 */
@Mixin(ItemStack.class)
public class ItemStackMixin {

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void preventItemDamage(int amount, Random random, @Nullable ServerPlayerEntity player,
			Consumer<Item> breakCallback, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
		cir.cancel();
	}
}
