package pw.lakuna.elytra_trinket;

import net.fabricmc.api.ModInitializer;

public class MainEntrypoint implements ModInitializer {
	@Override
	public void onInitialize() {
		ElytraTrinket.logger.info("Initialized Elytra Trinket (main).");
	}
}
