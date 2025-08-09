package pw.lakuna.elytra_trinket;

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.LivingEntity;

/** Client-side methods for Elytra Trinket. */
public final class ClientTools {
	/** Disable rendering capes when wearing an Elytra in a cape trinket slot. */
	protected static void registerCapeRenderer() {
		LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((state) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			return client.world == null || !(client.world.getEntityById(state.id) instanceof LivingEntity livingEntity)
					|| !ServerTools.isElytraTrinketEquipped(livingEntity);
		});
	}

	/** Enable rendering Elytra when wearing an Elytra in a cape trinket slot. */
	protected static void registerRenderer() {
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, registrar, context) -> {
			if (!(renderer instanceof PlayerEntityRenderer playerRenderer)) {
				return;
			}

			registrar.register(new ElytraTrinketFeatureRenderer<>(playerRenderer, context.getEntityModels(),
					context.getEquipmentRenderer()));
		});
	}
}
