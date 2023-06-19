package pw.lakuna.elytra_trinket;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class ElytraTrinket {
    public static boolean useElytraTrinket(LivingEntity entity, ItemStack stack, boolean doTick) {
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
}
