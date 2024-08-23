package folk.sisby.euphonium.sounds.biome;

import folk.sisby.euphonium.EuphoniumClient;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import folk.sisby.euphonium.sound.BiomeSound;
import folk.sisby.euphonium.sound.ISoundType;
import folk.sisby.euphonium.sound.SoundHandler;
import folk.sisby.euphonium.sound.SurfaceBiomeSound;
import folk.sisby.euphonium.helper.WorldHelper;

import org.jetbrains.annotations.Nullable;
import java.util.function.BiPredicate;

public class Plains implements ISoundType<BiomeSound> {
    public static SoundEvent DAY_SOUND;
    public static SoundEvent NIGHT_SOUND;
    public static final BiPredicate<RegistryEntry<Biome>, RegistryKey<Biome>> VALID_BIOME =
        (holder, key) -> key.equals(BiomeKeys.PLAINS) || holder.isIn(ConventionalBiomeTags.PLAINS);

    public Plains() {
        DAY_SOUND = SoundEvent.of(EuphoniumClient.id("biome.plains.day"));
        NIGHT_SOUND = SoundEvent.of(EuphoniumClient.id("biome.plains.night"));
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
                return VALID_BIOME.test(holder, key);
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
                return VALID_BIOME.test(holder, key);
            }
        });
    }
}
