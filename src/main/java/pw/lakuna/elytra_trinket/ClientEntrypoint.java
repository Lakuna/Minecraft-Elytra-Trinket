package pw.lakuna.elytra_trinket;

import net.fabricmc.api.ClientModInitializer;

public class ClientEntrypoint implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ElytraTrinket.logger.info("Initialized Elytra Trinket (client).");
	}
}
