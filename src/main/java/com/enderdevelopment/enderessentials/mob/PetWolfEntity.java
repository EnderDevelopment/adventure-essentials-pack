package com.enderdevelopment.enderessentials.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * A friendly pet wolf that follows and protects its owner.
 * Can be tamed with bones.
 */
public class PetWolfEntity extends TameableEntity {

	public PetWolfEntity(EntityType<? extends TameableEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new SwimGoal(this));
		this.goalSelector.add(2, new SitGoal(this));
		this.goalSelector.add(3, new MeleeAttackGoal(this, 1.4D, true));
		this.goalSelector.add(4, new FollowOwnerGoal(this, 1.2D, 10.0F, 2.0F, false));
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));

		this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
		this.targetSelector.add(2, new AttackWithOwnerGoal(this));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, net.minecraft.entity.mob.Monster.class, true));
	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);

		if (!this.isTamed() && stack.isOf(Items.BONE)) {
			// Tame the pet with bones
			if (this.random.nextInt(3) == 0) {
				this.setOwnerUuid(player.getUuid());
				this.setTamed(true);
				this.getWorld().sendEntityStatus(this, (byte) 7); // tame hearts
			} else {
				this.getWorld().sendEntityStatus(this, (byte) 6); // smoke
			}
			stack.decrement(1);
			return ActionResult.success(this.getWorld().isClient);
		}

		if (this.isTamed() && this.isOwner(player)) {
			// Toggle sit / stand behavior
			this.setSitting(!this.isSitting());
			return ActionResult.success(this.getWorld().isClient);
		}

		return super.interactMob(player, hand);
	}
}
