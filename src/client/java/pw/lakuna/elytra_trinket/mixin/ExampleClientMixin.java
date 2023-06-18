package pw.lakuna.elytra_trinket.mixin;

import net.minecraft.client.MinecraftClient;
import pw.lakuna.elytra_trinket.ElytraTrinket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public final class ExampleClientMixin {
    @Inject(at = @At("HEAD"), method = "run")
    private void run(CallbackInfo info) {
        ElytraTrinket.logger.info("Example mixin (client).");
    }
}
