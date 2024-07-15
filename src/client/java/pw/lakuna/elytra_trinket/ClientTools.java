package pw.lakuna.elytra_trinket;

import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;

/** Client-side methods for Elytra Trinket. */
public final class ClientTools {
    /** Disable rendering capes when wearing an Elytra in a cape trinket slot. */
    protected static void registerCapeRenderer() {
        LivingEntityFeatureRenderEvents.ALLOW_CAPE_RENDER.register(
                (AbstractClientPlayerEntity player) -> !ServerTools.isElytraTrinketEquipped(player));
    }

    /** Enable rendering Elytra when wearing an Elytra in a cape trinket slot. */
    protected static void registerRenderer() {
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register(
                (EntityType<? extends LivingEntity> entityType, LivingEntityRenderer<?, ?> entityRenderer,
                        RegistrationHelper registrationHelper, Context context) -> {
                    registrationHelper
                            .register(new ElytraTrinketFeatureRenderer<>(entityRenderer, context.getModelLoader()));
                });
    }
}
