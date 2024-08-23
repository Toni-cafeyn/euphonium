package folk.sisby.euphonium.sounds.world;

import folk.sisby.euphonium.EuphoniumClient;
import folk.sisby.euphonium.EuphoniumWorld;
import folk.sisby.euphonium.sound.ISoundType;
import folk.sisby.euphonium.sound.LoopedWorldSound;
import folk.sisby.euphonium.sound.SoundHandler;
import folk.sisby.euphonium.sound.WorldSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.biome.BiomeKeys;
import org.jetbrains.annotations.Nullable;

public class CaveDepth implements ISoundType<WorldSound> {
	public static SoundEvent SOUND;

	public CaveDepth() {
		SOUND = SoundEvent.of(EuphoniumClient.id("world.deep_cave"));
	}

	@Override
	public void addSounds(SoundHandler<WorldSound> handler) {
		if (!EuphoniumClient.CONFIG.worldAmbience.caveDepth) return;

		handler.getSounds().add(new LoopedWorldSound(handler.getPlayer()) {
			@Override
			public boolean isValidSituationCondition() {
				var pos = player.getBlockPos();

				// Don't play this if the player is in the Deep Dark, the combined sounds are too intense.
				var key = getBiomeKey(pos);
				if (key == BiomeKeys.DEEP_DARK) {
					return false;
				}

				if (!EuphoniumWorld.VALID_CAVE_DIMENSIONS.contains(level.getRegistryKey().getValue())) {
					return false;
				}

				var light = level.getLightLevel(pos);
				var bottom = level.getBottomY() < 0 ? 0 : 32;
				return !level.isSkyVisibleAllowingSea(pos)
					&& pos.getY() <= bottom
					&& light < EuphoniumClient.CONFIG.worldAmbience.caveLightLevel;
			}

			@Override
			public boolean isValidPlayerCondition() {
				return !player.isSubmergedInWater();
			}

			@Nullable
			@Override
			public SoundEvent getSound() {
				return SOUND;
			}
		});
	}
}
