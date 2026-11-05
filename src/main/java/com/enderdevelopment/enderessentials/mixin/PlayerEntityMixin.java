package com.enderdevelopment.enderessentials.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Prevents the player from taking fall damage for convenience.
 */
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void preventFallDamage(float fallDistance, float damageMultiplier, net.minecraft.entity.damage.DamageSource source,
			CallbackInfo ci) {
		ci.cancel();
	}
}
