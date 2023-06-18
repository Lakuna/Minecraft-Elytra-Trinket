package pw.lakuna.elytra_trinket.mixin;

import net.minecraft.server.MinecraftServer;
import pw.lakuna.elytra_trinket.ElytraTrinket;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public final class ExampleMixin {
    @Inject(at = @At("HEAD"), method = "loadWorld")
    private void init(CallbackInfo info) {
        ElytraTrinket.logger.info("Example mixin (server).");
    }
}
