package pw.lakuna.elytra_trinket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

/** Server- and client-side methods for Elytra Trinket. */
public final class ServerTools {
	/**
	 * Determine whether or not the given item stack contains a usable Elytra.
	 * 
	 * @param stack The item stack.
	 * @return Whether or not the given item stack contains a usable Elytra.
	 */
	private static boolean isUsableElytra(ItemStack stack) {
		return stack != null && !stack.isEmpty() && !stack.isBroken() && !stack.nextDamageWillBreak()
				&& stack.is(Items.ELYTRA);
	}

	/**
	 * Make the given entity fly if the given Elytra is usable.
	 * 
	 * @param entity The entity.
	 * @param stack  The Elytra.
	 * @param doTick Whether or not the Elytra should be checked on this tick.
	 * @returns Whether or not the entity was made to fly.
	 */
	private static boolean useElytraTrinket(LivingEntity entity, ItemStack stack, boolean doTick) {
		if (!ServerTools.isUsableElytra(stack) || !(entity instanceof Player player)) {
			return false;
		}

		if (!doTick) {
			return true;
		}

		int fallFlyingTicks = entity.getFallFlyingTicks() + 1;
		Level level = entity.level();
		if (!level.isClientSide() && fallFlyingTicks % 10 == 0) {
			if ((fallFlyingTicks / 10) % 2 == 0) {
				stack.hurtWithoutBreaking(1, player);
			}

			entity.gameEvent(GameEvent.ELYTRA_GLIDE);
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
		List<ItemStack> equippedElytraTrinkets = new ArrayList<ItemStack>();

		// Return an empty list if the trinket component isn't present.
		Optional<TrinketComponent> optionalTrinketComponent = TrinketsApi.getTrinketComponent(entity);
		if (!optionalTrinketComponent.isPresent()) {
			return equippedElytraTrinkets;
		}

		TrinketComponent trinketComponent = optionalTrinketComponent.get();

		// Check each trinket slot with an Elytra.
		for (Tuple<SlotReference, ItemStack> tuple : trinketComponent.getEquipped(Items.ELYTRA)) {
			// Skip slots that can't hold Elytra.
			if (!tuple.getA().inventory().getSlotType().getName().equals("cape")) {
				continue;
			}

			// Skip empty stacks.
			ItemStack stack = tuple.getB();
			if (stack == null || stack.isEmpty()) {
				continue;
			}

			equippedElytraTrinkets.add(stack);
		}

		return equippedElytraTrinkets;
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
