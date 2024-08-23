package folk.sisby.euphonium;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EuphoniumClient implements ClientModInitializer {
    public static final String ID = "euphonium";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	public static EuphoniumConfig CONFIG = EuphoniumConfig.createToml(FabricLoader.getInstance().getConfigDir(), "", ID,  EuphoniumConfig.class);

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}

	@Override
	public void onInitializeClient() {
		EuphoniumBiome.init();
		EuphoniumWorld.init();
		EuphoniumClient.LOGGER.info("[Euphonium] Initialized! Using sound channel: {}", CONFIG.channel.getName());
	}
}
