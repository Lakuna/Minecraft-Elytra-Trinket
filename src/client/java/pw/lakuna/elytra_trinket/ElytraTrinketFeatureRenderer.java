package pw.lakuna.elytra_trinket;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NonNull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.object.equipment.ElytraModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;

/**
 * A feature renderer for an Elytra trinket. This class is almost functionally
 * identical to
 * {@link net.minecraft.client.renderer.entity.layers.WingsLayer}.
 */
public class ElytraTrinketFeatureRenderer<S extends HumanoidRenderState, M extends EntityModel<S>>
		extends RenderLayer<S, M> {
	/** The Elytra entity model. */
	private final ElytraModel elytraModel;

	/** The Elytra entity model for babies. */
	private final ElytraModel elytraBabyModel;

	/** The equipment renderer that is used to render the Elytra. */
	private final EquipmentLayerRenderer equipmentRenderer;

	/**
	 * Create an Elytra trinket feature renderer.
	 * 
	 * @param renderLayerParent      The renderer of the entity.
	 * @param entityModelSet         The model loader to use to load the Elytra
	 *                               model.
	 * @param equipmentLayerRenderer The equipment renderer that is used to render
	 *                               the Elytra.
	 */
	public ElytraTrinketFeatureRenderer(RenderLayerParent<S, M> renderLayerParent, EntityModelSet entityModelSet,
			EquipmentLayerRenderer equipmentLayerRenderer) {
		super(renderLayerParent);
		this.elytraModel = new ElytraModel(entityModelSet.bakeLayer(ModelLayers.ELYTRA));
		this.elytraBabyModel = new ElytraModel(entityModelSet.bakeLayer(ModelLayers.ELYTRA_BABY));
		this.equipmentRenderer = equipmentLayerRenderer;
	}

	/**
	 * Render the Elytra trinket when necessary.
	 * 
	 * @param poseStack           The current transformation matrix stack for the
	 *                            renderer.
	 * @param vertices            The vertex consumer provider.
	 * @param i                   The light value to render with.
	 * @param humanoidRenderState The entity to render onto.
	 * @param f                   The angle to the relevant limb.
	 * @param g                   The distance to the relevant limb.
	 */
	@Override
	public void submit(@NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, int i,
			S humanoidRenderState,
			float f,
			float g) {
		// Get the player entity state.
		if (!(humanoidRenderState instanceof AvatarRenderState avatarRenderState)) {
			return;
		}

		// Get the world.
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) {
			return;
		}

		// Get the entity.
		Entity entity = level.getEntity(avatarRenderState.id);
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}

		// Get the stack of the Elytra trinket.
		List<ItemStack> equippedElytraTrinkets = ServerTools.getEquippedElytraTrinkets(livingEntity);
		if (equippedElytraTrinkets.size() < 1) {
			return;
		}

		ItemStack stack = equippedElytraTrinkets.get(0);

		// Get the equippable component asset ID.
		Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
		if (equippable == null) {
			return;
		}

		Optional<ResourceKey<EquipmentAsset>> optionalAssetId = equippable.assetId();
		if (optionalAssetId.isEmpty()) {
			return;
		}

		ResourceKey<EquipmentAsset> assetId = optionalAssetId.get();
		if (assetId == null) {
			return;
		}

		// Get the texture of the Elytra.
		Identifier identifier = null;
		Texture elytraTexture = avatarRenderState.skin.elytra();
		if (elytraTexture != null) {
			identifier = elytraTexture.texturePath();
		} else if (avatarRenderState.showCape) {
			Texture capeTexture = avatarRenderState.skin.cape();
			if (capeTexture != null) {
				identifier = capeTexture.texturePath();
			}
		}

		ElytraModel elytraModel = humanoidRenderState.isBaby ? this.elytraBabyModel : this.elytraModel;
		if (elytraModel == null) {
			return;
		}

		// Render the Elytra.
		poseStack.pushPose();
		poseStack.translate(0, 0, 0.125);
		this.equipmentRenderer.renderLayers(EquipmentClientInfo.LayerType.WINGS, assetId, elytraModel,
				humanoidRenderState, stack, poseStack, submitNodeCollector, i, identifier,
				humanoidRenderState.outlineColor, 0);
		poseStack.popPose();
	}
}
