package pw.lakuna.elytra_trinket;

import java.util.Optional;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.LivingEntity;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;

public final class MainEntrypoint implements ModInitializer {
	@Override
	public void onInitialize() {
		EntityElytraEvents.CUSTOM.register((LivingEntity entity, boolean tickElytra) -> {
			// Skip if the trinket component isn't present.
			Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(entity);
			if (!optional.isPresent()) {
				return false;
			}

			// Check each trinket slot with an Elytra.
			TrinketComponent trinketComponent = optional.get();
			for (Pair<SlotReference, ItemStack> pair : trinketComponent.getEquipped(Items.ELYTRA)) {
				// If a cape trinket slot contains a usable Elytra, fly.
				if (pair.getLeft().inventory().getSlotType().getName().equals("cape")
					&& ElytraTrinket.useElytraTrinket(entity, pair.getRight(), tickElytra)) {
					return true;
				}
			}

			// No usable Elytra is in a cape trinket slot.
			return false;
		});
	}
}
