package pw.lakuna.elytra_trinket;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/** The client-side entrypoint for Elytra Trinket. */
@Environment(EnvType.CLIENT)
public final class ClientEntrypoint implements ClientModInitializer {
	/** Run the client initializer. */
	@Override
	public void onInitializeClient() {
		ClientTools.registerCapeRenderer();
		ClientTools.registerRenderer();
	}
}
