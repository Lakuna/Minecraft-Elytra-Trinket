package pw.lakuna.elytra_trinket;

import java.util.List;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

/** A feature renderer for an Elytra trinket. */
public class ElytraTrinketFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>>
		extends FeatureRenderer<T, M> {
	// This class is almost functionally identical to `ElytraFeatureRenderer`.

	/** The default Elytra texture. */
	private static final Identifier defaultTexture = Identifier.ofVanilla("textures/entity/elytra.png");

	/** The Elytra entity model. */
	private final ElytraEntityModel<T> model;

	/**
	 * Create an Elytra trinket feature renderer.
	 * 
	 * @param context The renderer of the entity.
	 * @param loader  The model loader to use to load the Elytra model.
	 */
	public ElytraTrinketFeatureRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
		super(context);
		this.model = new ElytraEntityModel<T>(loader.getModelPart(EntityModelLayers.ELYTRA));
	}

	/**
	 * Render the Elytra trinket when necessary.
	 * 
	 * @param matrices          The current transformation matrix stack for the
	 *                          renderer.
	 * @param vertices          The vertex consumer provider.
	 * @param light             The light value to render with.
	 * @param entity            The entity to render onto.
	 * @param limbAngle         The angle to the relevant limb.
	 * @param limbDistance      The distance to the relevant limb.
	 * @param tickDelta         The time between this tick and the one before it.
	 * @param animationProgress The progress through the animation loop.
	 * @param headYaw           The yaw of the entity's head.
	 * @param headPitch         The pitch of the entity's head.
	 */
	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, T entity, float limbAngle,
			float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		// Get the stack of the Elytra trinket.
		List<ItemStack> equippedElytraTrinkets = ServerTools.getEquippedElytraTrinkets(entity);
		if (equippedElytraTrinkets.size() < 1) {
			return;
		}
		ItemStack stack = equippedElytraTrinkets.get(0);

		// Get the texture of the Elytra.
		Identifier identifier = ElytraTrinketFeatureRenderer.defaultTexture;
		if (entity instanceof AbstractClientPlayerEntity abstractEntity) {
			SkinTextures skinTextures = abstractEntity.getSkinTextures();
			if (skinTextures.elytraTexture() != null) {
				identifier = skinTextures.elytraTexture();
			} else if (skinTextures.capeTexture() != null && abstractEntity.isPartVisible(PlayerModelPart.CAPE)) {
				identifier = skinTextures.capeTexture();
			}
		}

		// Render the Elytra.
		matrices.push();
		matrices.translate(0, 0, 0.125f);
		this.getContextModel().copyStateTo(this.model);
		this.model.setAngles(entity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
		VertexConsumer vertex = ItemRenderer.getArmorGlintConsumer(vertices,
				RenderLayer.getArmorCutoutNoCull(identifier), stack.hasGlint());
		this.model.render(matrices, vertex, light, OverlayTexture.DEFAULT_UV);
		matrices.pop();
	}
}
