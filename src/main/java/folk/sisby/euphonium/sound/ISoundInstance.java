package folk.sisby.euphonium.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
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

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

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
                        .getOrThrow(RegistryKeys.BIOME)
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
                SoundInstance soundInstance = instance;

                if (QueueSoundMethodHolder.invoke(manager, soundInstance)) {
                        return;
                }

                PlaySoundMethodHolder.invoke(manager, soundInstance);
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
                        Method method = SoundManagerMethodFinder.find("queueSound", SoundInstance.class);

                        if (method == null) {
                                method = SoundManagerMethodFinder.find("queueSound", MovingSoundInstance.class);
                        }

                        return method;
                }

                private static boolean invoke(SoundManager manager, SoundInstance instance) {
                        if (METHOD == null) {
                                return false;
                        }

                        try {
                                METHOD.invoke(manager, instance);
                                return true;
                        } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException("Failed to invoke SoundManager queue method", e);
                        }
                }
        }

        final class PlaySoundMethodHolder {
                private static final Method METHOD = findMethod();

                private static Method findMethod() {
                        Class<?>[] returnTypes = SoundManagerMethodFinder.getPlayReturnTypes();
                        Method method = SoundManagerMethodFinder.findReturning(
                                "play",
                                new Class<?>[]{SoundInstance.class},
                                returnTypes
                        );

                        if (method == null) {
                                method = SoundManagerMethodFinder.findReturning(
                                        "play",
                                        new Class<?>[]{MovingSoundInstance.class},
                                        returnTypes
                                );
                        }

                        return method;
                }

                private static void invoke(SoundManager manager, SoundInstance instance) {
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

        final class SoundManagerMethodFinder {
                private static final MappingResolver RESOLVER = FabricLoader.getInstance().getMappingResolver();
                private static final String SOUND_MANAGER_CLASS = "net.minecraft.client.sound.SoundManager";
                private static final Class<?> PLAY_RESULT_CLASS = resolvePlayResultClass();
                private static final Class<?>[] PLAY_RETURN_TYPES = buildPlayReturnTypes(PLAY_RESULT_CLASS);

                static {
                        Class<?>[] returnTypes = getPlayReturnTypes();

                        assert findReturning(
                                "play",
                                new Class<?>[]{SoundInstance.class},
                                returnTypes
                        ) != null : "SoundManager#play(SoundInstance) not found under current mappings";
                }

                private static Method find(String namedName, Class<?>... parameterTypes) {
                        return findReturning(namedName, parameterTypes, void.class);
                }

                static Method findReturning(String namedName, Class<?>[] parameterTypes, Class<?>... returnTypes) {
                        for (Class<?> returnType : returnTypes) {
                                Method method = findExact(namedName, returnType, parameterTypes);

                                if (method != null) {
                                        return method;
                                }
                        }

                        return null;
                }

                private static Method findExact(String namedName, Class<?> returnType, Class<?>... parameterTypes) {
                        String descriptor = getNamedMethodDescriptor(returnType, parameterTypes);
                        String runtimeName = RESOLVER.mapMethodName("named", SOUND_MANAGER_CLASS, namedName, descriptor);

                        try {
                                Method method = SoundManager.class.getDeclaredMethod(runtimeName, parameterTypes);
                                method.setAccessible(true);
                                return method;
                        } catch (NoSuchMethodException e) {
                                return null;
                        }
                }

                static Class<?> getNamedClassOrNull(String namedClassName) {
                        try {
                                String runtimeName = RESOLVER.mapClassName("named", namedClassName);
                                ClassLoader classLoader = SoundManager.class.getClassLoader();
                                return Class.forName(runtimeName.replace('/', '.'), false, classLoader);
                        } catch (ClassNotFoundException | IllegalArgumentException e) {
                                return null;
                        }
                }

                static Class<?>[] getPlayReturnTypes() {
                        return PLAY_RETURN_TYPES.clone();
                }

                private static Class<?> resolvePlayResultClass() {
                        Class<?> playResultClass = getNamedClassOrNull(
                                "net.minecraft.client.sound.SoundSystem$PlayResult"
                        );

                        if (playResultClass == null) {
                                playResultClass = getNamedClassOrNull(
                                        "net.minecraft.client.sound.SoundEngine$PlayResult"
                                );
                        }

                        return playResultClass;
                }

                private static Class<?>[] buildPlayReturnTypes(@Nullable Class<?> playResultClass) {
                        if (playResultClass == null) {
                                return new Class<?>[]{void.class};
                        }

                        return new Class<?>[]{void.class, playResultClass};
                }

                private static String getNamedMethodDescriptor(Class<?> returnType, Class<?>... parameterTypes) {
                        StringBuilder builder = new StringBuilder("(");

                        for (Class<?> parameterType : parameterTypes) {
                                builder.append(getNamedDescriptor(parameterType));
                        }

                        builder.append(')');
                        builder.append(getNamedDescriptor(returnType));
                        return builder.toString();
                }

                private static String getNamedDescriptor(Class<?> type) {
                        if (type == void.class) {
                                return "V";
                        }

                        if (type.isPrimitive()) {
                                if (type == boolean.class) return "Z";
                                if (type == byte.class) return "B";
                                if (type == char.class) return "C";
                                if (type == short.class) return "S";
                                if (type == int.class) return "I";
                                if (type == long.class) return "J";
                                if (type == float.class) return "F";
                                if (type == double.class) return "D";
                        }

                        if (type.isArray()) {
                                return "[" + getNamedDescriptor(type.getComponentType());
                        }

                        String runtimeName = type.getName();
                        String namedName = RESOLVER.unmapClassName("named", runtimeName);
                        return "L" + namedName.replace('.', '/') + ";";
                }
        }
}
