package pw.lakuna.elytra_trinket;

import net.fabricmc.api.ClientModInitializer;

/** The client-side entrypoint for Elytra Trinket. */
public final class ClientEntrypoint implements ClientModInitializer {
	/** Run the client initializer. */
	@Override
	public void onInitializeClient() {
		ClientTools.registerCapeRenderer();
		ClientTools.registerRenderer();
	}
}
