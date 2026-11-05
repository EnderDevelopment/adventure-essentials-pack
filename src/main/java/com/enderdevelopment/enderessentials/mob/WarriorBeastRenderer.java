package com.enderdevelopment.enderessentials.mob;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * Renders the Warrior Beast using the wolf model with a custom texture.
 */
public class WarriorBeastRenderer extends MobEntityRenderer<WarriorBeastEntity, WolfEntityModel<WarriorBeastEntity>> {

	public WarriorBeastRenderer(EntityRendererFactory.Context context) {
		super(context, new WolfEntityModel<>(context.getPart(EntityModelLayers.WOLF)), 0.8f);
	}

	@Override
	protected void scale(WarriorBeastEntity entity, MatrixStack matrices, float amount) {
		matrices.scale(1.5f, 1.5f, 1.5f);
	}

	@Override
	public Identifier getTexture(WarriorBeastEntity entity) {
		return new Identifier("ender_essentials", "textures/entity/warrior_beast.png");
	}
}
