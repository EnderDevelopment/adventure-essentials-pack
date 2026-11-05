package com.enderdevelopment.enderessentials.mob;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.util.Identifier;

/**
 * Renders the Pet Wolf using the vanilla wolf model.
 */
public class PetWolfRenderer extends MobEntityRenderer<PetWolfEntity, WolfEntityModel<PetWolfEntity>> {

	public PetWolfRenderer(EntityRendererFactory.Context context) {
		super(context, new WolfEntityModel<>(context.getPart(EntityModelLayers.WOLF)), 0.5f);
	}

	@Override
	public Identifier getTexture(PetWolfEntity entity) {
		return new Identifier("minecraft", "textures/entity/wolf/wolf.png");
	}
}
