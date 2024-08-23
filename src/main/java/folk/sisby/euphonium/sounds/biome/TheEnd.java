package folk.sisby.euphonium.sounds.biome;

import folk.sisby.euphonium.EuphoniumClient;
import folk.sisby.euphonium.helper.SoundHelper;
import folk.sisby.euphonium.sound.BiomeSound;
import folk.sisby.euphonium.sound.ISoundType;
import folk.sisby.euphonium.sound.SoundHandler;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TheEnd implements ISoundType<BiomeSound> {
	public static SoundEvent SOUND;
	public static Predicate<RegistryEntry<Biome>> VALID_BIOME =
		holder -> holder.isIn(ConventionalBiomeTags.IN_THE_END);

	public TheEnd() {
		SOUND = SoundHelper.sound(EuphoniumClient.id("biome.the_end"));
	}

	@Override
	public void addSounds(SoundHandler<BiomeSound> handler) {
		handler.getSounds().add(new BiomeSound(handler.getPlayer()) {
			@Override
			public boolean isValidBiomeCondition(RegistryEntry<Biome> holder, RegistryKey<Biome> key) {
				return VALID_BIOME.test(holder);
			}

			@Override
			public boolean isValidPlayerCondition() {
				return true;
			}

			@Nullable
			@Override
			public SoundEvent getSound() {
				return SOUND;
			}
		});
	}
}
