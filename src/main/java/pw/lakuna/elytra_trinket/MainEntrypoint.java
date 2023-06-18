package pw.lakuna.elytra_trinket;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.item.Items;

public final class MainEntrypoint implements ModInitializer {
	@Override
	public void onInitialize() {
		EntityElytraEvents.CUSTOM.register((LivingEntity entity, boolean tickElytra) -> TrinketsApi.getTrinketComponent(entity).get().isEquipped(Items.ELYTRA));
		ElytraTrinket.logger.info("Initialized Elytra Trinket (main).");
	}
}
