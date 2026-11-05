package com.enderdevelopment.enderessentials.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;

/**
 * A dangerous hostile mob - the Warrior Beast.
 * Larger and tougher than normal mobs, deal heavy damage.
 */
public class WarriorBeastEntity extends PathAwareEntity {

	public WarriorBeastEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		// Attack player nearby
		this.targetSelector.add(1, new net.minecraft.entity.ai.goal.ActiveTargetGoal<>(this,
				net.minecraft.entity.player.PlayerEntity.class, true));
		this.goalSelector.add(2, new net.minecraft.entity.ai.goal.MeleeAttackGoal(this, 1.2D, true));
		this.goalSelector.add(3, new net.minecraft.entity.ai.goal.WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(4, new net.minecraft.entity.ai.goal.LookAtEntityGoal(this,
				net.minecraft.entity.player.PlayerEntity.class, 8.0F));
	}
}
