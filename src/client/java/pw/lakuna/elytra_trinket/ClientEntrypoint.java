package pw.lakuna.elytra_trinket;

import net.fabricmc.api.ClientModInitializer;

/** The client-side entrypoint for Elytra Trinket. */
public final class ClientEntrypoint implements ClientModInitializer {
	/** Runs when the client is initialized. */
	@Override
	public void onInitializeClient() {
		ElytraTrinketClient.registerCapeRenderer();
		ElytraTrinketClient.registerRenderer();
	}
}
