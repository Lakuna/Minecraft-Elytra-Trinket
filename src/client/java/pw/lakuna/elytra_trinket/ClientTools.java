package pw.lakuna.elytra_trinket;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

/** Client-side methods for Elytra Trinket. */
@Environment(EnvType.CLIENT)
public final class ClientTools {
	/** Disable rendering capes when wearing an Elytra in a cape trinket slot. */
	protected static void registerCapeRenderer() {
		LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register((state) -> {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client == null || client.world == null) {
				return true;
			}

			Entity entity = client.world.getEntityById(state.id);
			if (entity == null || !(entity instanceof LivingEntity livingEntity)) {
				return true;
			}

			return !ServerTools.isElytraTrinketEquipped(livingEntity);
		});
	}

	/** Enable rendering Elytra when wearing an Elytra in a cape trinket slot. */
	protected static void registerRenderer() {
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, register, context) -> {
			if (!(renderer instanceof PlayerEntityRenderer playerRenderer)) {
				return;
			}

			register.register(new ElytraTrinketFeatureRenderer<>(playerRenderer, context.getEntityModels(),
					context.getEquipmentRenderer()));
		});
	}
}
