package pw.lakuna.elytra_trinket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/** Server- and client-side methods for Elytra Trinket. */
public final class ServerTools {
	/**
	 * Make the given entity fly if the given Elytra is usable.
	 * 
	 * @param entity The entity.
	 * @param stack  The Elytra.
	 * @param doTick Whether or not the Elytra should be checked on this tick.
	 * @returns Whether or not the entity was made to fly.
	 */
	private static boolean useElytraTrinket(LivingEntity entity, ItemStack stack, boolean doTick) {
		if (stack.isEmpty() || stack.shouldBreak() || !(entity instanceof PlayerEntity playerEntity)) {
			return false;
		}

		if (!doTick) {
			return true;
		}

		int nextRoll = entity.getGlidingTicks() + 1;
		World world = entity.getWorld();
		if (!world.isClient && nextRoll % 10 == 0) {
			if ((nextRoll / 10) % 2 == 0) {
				stack.damage(1, playerEntity);
			}

			entity.emitGameEvent(GameEvent.ELYTRA_GLIDE);
		}

		return true;
	}

	/** Enable flight when wearing an Elytra in a cape trinket slot. */
	protected static void registerFlight() {
		EntityElytraEvents.CUSTOM.register((entity, tickElytra) -> {
			// If an equipped Elytra is usable, fly.
			for (ItemStack stack : ServerTools.getEquippedElytraTrinkets(entity)) {
				if (ServerTools.useElytraTrinket(entity, stack, tickElytra)) {
					return true;
				}
			}

			// No usable Elytra is in a cape trinket slot.
			return false;
		});
	}

	/**
	 * Get a list of equipped Elytra trinkets.
	 * 
	 * @param entity The entity that has the Elytra equipped.
	 * @returns A list of equipped Elytra trinkets.
	 */
	public static List<ItemStack> getEquippedElytraTrinkets(LivingEntity entity) {
		List<ItemStack> out = new ArrayList<ItemStack>();

		// Return an empty list if the trinket component isn't present.
		Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(entity);
		if (!optional.isPresent()) {
			return out;
		}

		// Check each trinket slot with an Elytra.
		TrinketComponent trinketComponent = optional.get();
		for (Pair<SlotReference, ItemStack> pair : trinketComponent.getEquipped(Items.ELYTRA)) {
			// If the Elytra is in a cape slot, add it to the output.
			if (pair.getLeft().inventory().getSlotType().getName().equals("cape")) {
				out.add(pair.getRight());
			}
		}

		return out;
	}

	/**
	 * Determine whether or not the given entity is wearing an Elytra in a trinket
	 * slot.
	 * 
	 * @param entity The entity to check.
	 * @returns Whether or not the entity is wearing an Elytra in a trinket slot.
	 */
	public static boolean isElytraTrinketEquipped(LivingEntity entity) {
		return ServerTools.getEquippedElytraTrinkets(entity).size() > 0;
	}
}
