package folk.sisby.euphonium.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface ISoundInstance {
	boolean isValid();

	boolean isValidPlayerCondition();

	void tick();

	void updatePlayer(PlayerEntity player);

	PlayerEntity getPlayer();

	ClientWorld getLevel();

	@Nullable
	SoundEvent getSound();

	MovingSoundInstance getSoundInstance();

	default Biome getBiome(BlockPos pos) {
		return getBiomeHolder(pos).value();
	}

	default RegistryKey<Biome> getBiomeKey(BlockPos pos) {
		var biome = getBiome(pos);
		return getLevel().getRegistryManager()
			.get(RegistryKeys.BIOME)
			.getKey(biome)
			.orElse(null);
	}

	default RegistryEntry<Biome> getBiomeHolder(BlockPos pos) {
		return getPlayer().getWorld().getBiome(pos);
	}

	default MinecraftClient getMinecraft() {
		return MinecraftClient.getInstance();
	}

        default SoundManager getSoundManager() {
                return getMinecraft().getSoundManager();
        }

        default void queueSound(MovingSoundInstance instance) {
                if (instance == null) {
                        return;
                }

                var manager = getSoundManager();
                var queueMethod = QueueSoundMethodHolder.METHOD;

                if (queueMethod != null) {
                        try {
                                queueMethod.invoke(manager, instance);
                                return;
                        } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException("Failed to invoke SoundManager queue method", e);
                        }
                }

                PlaySoundMethodHolder.invoke(manager, instance);
        }

        default boolean isPlaying() {
                return getSoundInstance() != null
                        && getSoundManager().isPlaying(getSoundInstance());
        }

	default void stop() {
		getSoundManager().stop(getSoundInstance());
	}

	default int getDelay() {
		return 0;
	}

	default float getVolume() {
		return 1.0F;
	}

	default float getPitch() {
		return 1.0F;
	}

        default double getVolumeScaling() {
                return 1.0D;
        }

        final class QueueSoundMethodHolder {
                private static final Method METHOD = findMethod();

                private static Method findMethod() {
                        try {
                                return SoundManager.class.getMethod("queueSound", MovingSoundInstance.class);
                        } catch (NoSuchMethodException e) {
                                return null;
                        }
                }
        }

        final class PlaySoundMethodHolder {
                private static final Method METHOD = findMethod();

                private static Method findMethod() {
                        try {
                                return SoundManager.class.getMethod("play", MovingSoundInstance.class);
                        } catch (NoSuchMethodException e) {
                                try {
                                        return SoundManager.class.getMethod("play", net.minecraft.client.sound.SoundInstance.class);
                                } catch (NoSuchMethodException ignored) {
                                        return null;
                                }
                        }
                }

                private static void invoke(SoundManager manager, MovingSoundInstance instance) {
                        if (METHOD == null) {
                                throw new IllegalStateException("No compatible SoundManager#play method found");
                        }

                        try {
                                METHOD.invoke(manager, instance);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException("Failed to invoke legacy SoundManager#play", e);
                        }
                }
        }
}
