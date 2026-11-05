package com.enderdevelopment.enderessentials.animation;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

/**
 * A simple procedural animation utility that gives entities
 * smooth idle and attack animations without needing model files.
 */
public class AnimationHelper {

	/**
	 * Computes a smooth sine-wave bob for an entity's idle animation.
	 * Returns an offset in the range [-1, 1].
	 */
	public static float idleBob(float tickDelta, LivingEntity entity) {
		float totalTicks = entity.age + tickDelta;
		return MathHelper.sin(totalTicks * 0.1f) * 0.15f;
	}

	/**
	 * Computes the attack lunge offset for a melee attack animation.
	 */
	public static float attackLunge(float attackProgress) {
		// attackProgress goes 0 to 1 during an attack, producing a quick lunge-and-return
		return MathHelper.sin(attackProgress * MathHelper.PI) * 0.5f;
	}

	/**
	 * Computes a wing flap or tail wag animation value.
	 */
	public static float flap(float tickDelta, LivingEntity entity, float speed) {
		float totalTicks = entity.age + tickDelta;
		return MathHelper.sin(totalTicks * speed) * 0.3f;
	}
}
