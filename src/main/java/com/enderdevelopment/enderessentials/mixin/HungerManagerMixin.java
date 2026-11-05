package com.enderdevelopment.enderessentials.mixin;

import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Makes the player always have full hunger so they never need to eat.
 * This provides the "infinite hunger" convenience feature.
 */
@Mixin(HungerManager.class)
public class HungerManagerMixin {

	@Inject(method = "update", at = @At("HEAD"))
	private void forceFullHunger(PlayerEntity player, CallbackInfo ci) {
		HungerManager self = (HungerManager) (Object) this;
		self.setFoodLevel(20);
		self.setSaturationLevel(20.0f);
	}
}
