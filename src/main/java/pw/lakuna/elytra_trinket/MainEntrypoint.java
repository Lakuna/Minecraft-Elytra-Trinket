package pw.lakuna.elytra_trinket;

import net.fabricmc.api.ModInitializer;

/** The server- and client-side entrypoint for Elytra Trinket. */
public final class MainEntrypoint implements ModInitializer {
	@Override
	public void onInitialize() {
		ElytraTrinket.registerFlight();
	}
}
