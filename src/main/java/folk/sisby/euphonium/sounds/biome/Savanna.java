package folk.sisby.euphonium.sounds.biome;

import folk.sisby.euphonium.EuphoniumClient;
import folk.sisby.euphonium.helper.SoundHelper;
import folk.sisby.euphonium.helper.WorldHelper;
import folk.sisby.euphonium.sound.BiomeSound;
import folk.sisby.euphonium.sound.ISoundType;
import folk.sisby.euphonium.sound.SoundHandler;
import folk.sisby.euphonium.sound.SurfaceBiomeSound;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class Savanna implements ISoundType<BiomeSound> {
	public static final Predicate<RegistryEntry<Biome>> VALID_BIOME =
		holder -> holder.isIn(ConventionalBiomeTags.SAVANNA);
	public static SoundEvent DAY_SOUND;
	public static SoundEvent NIGHT_SOUND;

	public Savanna() {
		DAY_SOUND = SoundHelper.sound(EuphoniumClient.id("biome.savanna.day"));
		NIGHT_SOUND = SoundHelper.sound(EuphoniumClient.id("biome.savanna.night"));
	}

	@Override
	public void addSounds(SoundHandler<BiomeSound> handler) {
		// Day sound.
		handler.getSounds().add(new SurfaceBiomeSound(handler.getPlayer(), false) {
			@Nullable
			@Override
			public SoundEvent getSound() {
				return DAY_SOUND;
			}

			@Override
			public boolean isValidPlayerCondition() {
				return super.isValidPlayerCondition() && WorldHelper.isDay(player);
			}

			@Override
			public boolean isValidBiomeCondition(RegistryEntry<Biome> holder, RegistryKey<Biome> key) {
				return VALID_BIOME.test(holder);
			}
		});

		// Night sound.
		handler.getSounds().add(new SurfaceBiomeSound(handler.getPlayer(), false) {
			@Nullable
			@Override
			public SoundEvent getSound() {
				return NIGHT_SOUND;
			}

			@Override
			public boolean isValidPlayerCondition() {
				return super.isValidPlayerCondition() && WorldHelper.isNight(player);
			}

			@Override
			public boolean isValidBiomeCondition(RegistryEntry<Biome> holder, RegistryKey<Biome> key) {
				return VALID_BIOME.test(holder);
			}
		});
	}
}
