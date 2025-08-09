package pw.lakuna.elytra_trinket;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.equipment.EquipmentModel;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

/**
 * A feature renderer for an Elytra trinket. This class is almost functionally
 * identical to
 * {@link net.minecraft.client.render.entity.feature.ElytraFeatureRenderer}.
 */
public class ElytraTrinketFeatureRenderer<S extends BipedEntityRenderState, M extends EntityModel<S>>
		extends FeatureRenderer<S, M> {
	/** The Elytra entity model. */
	private final ElytraEntityModel model;

	/** The Elytra entity model for babies. */
	private final ElytraEntityModel babyModel;

	/** The equipment renderer that is used to render the Elytra. */
	private final EquipmentRenderer renderer;

	/**
	 * Create an Elytra trinket feature renderer.
	 * 
	 * @param context  The renderer of the entity.
	 * @param loader   The model loader to use to load the Elytra model.
	 * @param renderer The equipment renderer that is used to render the Elytra.
	 */
	public ElytraTrinketFeatureRenderer(FeatureRendererContext<S, M> context, LoadedEntityModels loader,
			EquipmentRenderer renderer) {
		super(context);
		this.model = new ElytraEntityModel(loader.getModelPart(EntityModelLayers.ELYTRA));
		this.babyModel = new ElytraEntityModel(loader.getModelPart(EntityModelLayers.ELYTRA_BABY));
		this.renderer = renderer;
	}

	/**
	 * Render the Elytra trinket when necessary.
	 * 
	 * @param matrices     The current transformation matrix stack for the renderer.
	 * @param vertices     The vertex consumer provider.
	 * @param light        The light value to render with.
	 * @param state        The entity to render onto.
	 * @param limbAngle    The angle to the relevant limb.
	 * @param limbDistance The distance to the relevant limb.
	 */
	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertices, int light, S state, float limbAngle,
			float limbDistance) {
		// Get the player entity state.
		if (!(state instanceof PlayerEntityRenderState playerState)) {
			return;
		}

		// Get the world.
		MinecraftClient client = MinecraftClient.getInstance();
		if (client.world == null) {
			return;
		}

		// Get the entity.
		Entity entity = client.world.getEntityById(playerState.id);
		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}

		// Get the stack of the Elytra trinket.
		List<ItemStack> stacks = ServerTools.getEquippedElytraTrinkets(livingEntity);
		if (stacks.size() < 1) {
			return;
		}
		ItemStack stack = stacks.get(0);

		// Get the equippable component asset ID.
		EquippableComponent equippableComponent = stack.get(DataComponentTypes.EQUIPPABLE);
		if (equippableComponent == null) {
			return;
		}
		Optional<RegistryKey<EquipmentAsset>> optionalAssetId = equippableComponent.assetId();
		if (optionalAssetId.isEmpty()) {
			return;
		}
		RegistryKey<EquipmentAsset> assetId = optionalAssetId.get();

		// Get the texture of the Elytra.
		Identifier identifier = null;
		Identifier elytraTexture = playerState.skinTextures.elytraTexture();
		if (elytraTexture != null) {
			identifier = elytraTexture;
		} else if (playerState.capeVisible) {
			Identifier capeTexture = playerState.skinTextures.capeTexture();
			if (capeTexture != null) {
				identifier = capeTexture;
			}
		}
		ElytraEntityModel model = state.baby ? this.babyModel : this.model;

		// Render the Elytra.
		matrices.push();
		matrices.translate(0, 0, 0.125);
		model.setAngles(state);
		this.renderer.render(EquipmentModel.LayerType.WINGS, assetId, model, stack, matrices, vertices, light,
				identifier);
		matrices.pop();
	}
}
