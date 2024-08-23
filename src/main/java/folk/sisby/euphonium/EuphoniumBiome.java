package folk.sisby.euphonium;

import folk.sisby.euphonium.sound.BiomeSound;
import folk.sisby.euphonium.sound.ISoundType;
import folk.sisby.euphonium.sound.SoundHandler;
import folk.sisby.euphonium.sounds.biome.Badlands;
import folk.sisby.euphonium.sounds.biome.Beach;
import folk.sisby.euphonium.sounds.biome.Caves;
import folk.sisby.euphonium.sounds.biome.Desert;
import folk.sisby.euphonium.sounds.biome.Forest;
import folk.sisby.euphonium.sounds.biome.Icy;
import folk.sisby.euphonium.sounds.biome.Jungle;
import folk.sisby.euphonium.sounds.biome.Mountains;
import folk.sisby.euphonium.sounds.biome.Ocean;
import folk.sisby.euphonium.sounds.biome.Plains;
import folk.sisby.euphonium.sounds.biome.River;
import folk.sisby.euphonium.sounds.biome.Savanna;
import folk.sisby.euphonium.sounds.biome.Swamp;
import folk.sisby.euphonium.sounds.biome.Taiga;
import folk.sisby.euphonium.sounds.biome.TheEnd;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EuphoniumBiome {
	private static final ISoundType<BiomeSound> BADLANDS = new Badlands();
	private static final ISoundType<BiomeSound> BEACH = new Beach();
	private static final ISoundType<BiomeSound> CAVES = new Caves();
	private static final ISoundType<BiomeSound> DESERT = new Desert();
	private static final ISoundType<BiomeSound> FOREST = new Forest();
	private static final ISoundType<BiomeSound> ICY = new Icy();
	private static final ISoundType<BiomeSound> JUNGLE = new Jungle();
	private static final ISoundType<BiomeSound> MOUNTAINS = new Mountains();
	private static final ISoundType<BiomeSound> OCEAN = new Ocean();
	private static final ISoundType<BiomeSound> PLAINS = new Plains();
	private static final ISoundType<BiomeSound> RIVER = new River();
	private static final ISoundType<BiomeSound> SAVANNA = new Savanna();
	private static final ISoundType<BiomeSound> SWAMP = new Swamp();
	private static final ISoundType<BiomeSound> TAIGA = new Taiga();
	private static final ISoundType<BiomeSound> THE_END = new TheEnd();
	public static List<Identifier> VALID_DIMENSIONS = new ArrayList<>();
	private static Handler handler;

	public static void init() {
		if (EuphoniumClient.CONFIG.biomeAmbienceEnabled) {
			ClientEntityEvents.ENTITY_LOAD.register(EuphoniumBiome::handleClientEntityJoin);
			ClientEntityEvents.ENTITY_UNLOAD.register(EuphoniumBiome::handleClientEntityLeave);
			ClientTickEvents.END_CLIENT_TICK.register(EuphoniumBiome::handleClientTick);
			EuphoniumClient.CONFIG.biomeAmbience.dimensions.forEach(dim -> VALID_DIMENSIONS.add(new Identifier(dim)));
		}
	}

	private static void handleClientTick(MinecraftClient client) {
		if (handler != null && !client.isPaused()) {
			handler.tick();
		}
	}

	private static void handleClientEntityLeave(Entity entity, World level) {
		if (entity instanceof ClientPlayerEntity && handler != null) {
			handler.stop();
		}
	}

	private static void handleClientEntityJoin(Entity entity, World level) {
		if (entity instanceof ClientPlayerEntity player) {
			trySetupSoundHandler(player);
		}
	}

	private static void trySetupSoundHandler(PlayerEntity player) {
		if (!(player instanceof ClientPlayerEntity)) return;

		if (handler == null) {
			handler = new Handler(player);
		}

		handler.updatePlayer(player);
	}

	public static class Handler extends SoundHandler<BiomeSound> {
		public Handler(@NotNull PlayerEntity player) {
			super(player);

			BADLANDS.addSounds(this);
			BEACH.addSounds(this);
			CAVES.addSounds(this);
			DESERT.addSounds(this);
			FOREST.addSounds(this);
			ICY.addSounds(this);
			JUNGLE.addSounds(this);
			MOUNTAINS.addSounds(this);
			OCEAN.addSounds(this);
			PLAINS.addSounds(this);
			RIVER.addSounds(this);
			SAVANNA.addSounds(this);
			SWAMP.addSounds(this);
			TAIGA.addSounds(this);
			THE_END.addSounds(this);
		}
	}
}
