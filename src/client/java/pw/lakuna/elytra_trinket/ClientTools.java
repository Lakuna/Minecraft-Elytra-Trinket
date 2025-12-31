package pw.lakuna.elytra_trinket;

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.LivingEntity;

/** Client-side methods for Elytra Trinket. */
public final class ClientTools {
	/** Disable rendering capes when wearing an Elytra in a cape trinket slot. */
	protected static void registerCapeRenderer() {
		LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((state) -> {
			ClientLevel level = Minecraft.getInstance().level;
			return level == null || !(level.getEntity(state.id) instanceof LivingEntity livingEntity)
					|| !ServerTools.isElytraTrinketEquipped(livingEntity);
		});
	}

	/** Enable rendering Elytra when wearing an Elytra in a cape trinket slot. */
	@SuppressWarnings("unchecked")
	protected static void registerRenderer() {
		LivingEntityFeatureRendererRegistrationCallback.EVENT
				.register((entityType, entityRenderer, registrationHelper, context) -> {
					if (!(entityRenderer instanceof AvatarRenderer avatarRenderer)) {
						return;
					}

					registrationHelper.register(new ElytraTrinketFeatureRenderer<AvatarRenderState, PlayerModel>(
							avatarRenderer, context.getModelSet(), context.getEquipmentRenderer()));
				});
	}
}
