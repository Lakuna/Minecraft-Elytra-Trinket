package pw.lakuna.elytra_trinket;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

/** Server- and client-side methods for Elytra Trinket. */
public final class ElytraTrinket {
    /**
     * Makes the given entity fly if the given Elytra is usable.
     * @param entity The entity.
     * @param stack The Elytra.
     * @param doTick Whether the Elytra should be checked on this tick.
     * @returns Whether the entity was made to fly.
     */
    private static boolean useElytraTrinket(LivingEntity entity, ItemStack stack, boolean doTick) {
        // This method is almost functionally identical to `FabricElytraItem.useCustomElytra`.
        if (!ElytraItem.isUsable(stack)) {
            return false;
        }

        if (!doTick) {
            return true;
        }

        int nextRoll = entity.getRoll() + 1;
        World world = entity.getWorld();
        if (!world.isClient && nextRoll % 10 == 0) {
            if ((nextRoll / 10) % 2 == 0) {
                stack.damage(1, entity, null);
            }

            entity.emitGameEvent(GameEvent.ELYTRA_GLIDE);
        }

        return true;
    }

    /** Enables flight when wearing an Elytra in a cape trinket slot. */
	protected static void registerFlight() {
		EntityElytraEvents.CUSTOM.register((LivingEntity entity, boolean tickElytra) -> {
			// If an equipped Elytra is usable, fly.
			for (ItemStack stack : ElytraTrinket.getEquipped(entity)) {
				if (ElytraTrinket.useElytraTrinket(entity, stack, tickElytra)) {
					return true;
				}
			}

			// No usable Elytra is in a cape trinket slot.
			return false;
		});
	}

    /**
     * Gets a list of equipped Elytra trinkets.
     * @param entity The entity that has the Elytra equipped.
     * @returns A list of equipped Elytra trinkets.
     */
    public static List<ItemStack> getEquipped(LivingEntity entity) {
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
     * Returns whether the given entity is wearing an Elytra in a trinket slot.
     * @param entity The entity to check.
     * @returns Whether the entity is wearing an Elytra in a trinket slot.
     */
    public static boolean isEquipped(LivingEntity entity) {
        return ElytraTrinket.getEquipped(entity).size() > 0;
    }
}
