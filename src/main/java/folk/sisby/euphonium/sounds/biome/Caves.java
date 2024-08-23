package folk.sisby.euphonium.sounds.biome;

import folk.sisby.euphonium.EuphoniumClient;
import folk.sisby.euphonium.sound.BiomeSound;
import folk.sisby.euphonium.sound.ISoundType;
import folk.sisby.euphonium.sound.SoundHandler;

import org.jetbrains.annotations.Nullable;
import java.util.function.Predicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;

public class Caves implements ISoundType<BiomeSound> {
    public static SoundEvent DEEP_DARK;
    public static SoundEvent DRIPSTONE;
    public static SoundEvent LUSH;

    public static final Predicate<RegistryEntry<Biome>> VALID_BIOME =
        holder -> holder.isIn(BiomeTags.IS_BEACH);

    public Caves() {
        DEEP_DARK = SoundEvent.of(EuphoniumClient.id("biome.caves.deep_dark"));
        DRIPSTONE = SoundEvent.of(EuphoniumClient.id("biome.caves.dripstone"));
        LUSH = SoundEvent.of(EuphoniumClient.id("biome.caves.lush"));
    }

    @Override
    public void addSounds(SoundHandler<BiomeSound> handler) {
        // Deep dark caves.
        handler.getSounds().add(new BiomeSound(handler.getPlayer()) {
            @Override
            public boolean isValidBiomeCondition(RegistryEntry<Biome> holder, RegistryKey<Biome> key) {
                return key.equals(BiomeKeys.DEEP_DARK);
            }

            @Override
            public boolean isValidPlayerCondition() {
                return true;
            }

            @Nullable
            @Override
            public SoundEvent getSound() {
                return DEEP_DARK;
            }
        });

        // Dripstone caves.
        handler.getSounds().add(new BiomeSound(handler.getPlayer()) {
            @Override
            public boolean isValidBiomeCondition(RegistryEntry<Biome> holder, RegistryKey<Biome> key) {
                return key.equals(BiomeKeys.DRIPSTONE_CAVES);
            }

            @Override
            public boolean isValidPlayerCondition() {
                return true;
            }

            @Nullable
            @Override
            public SoundEvent getSound() {
                return DRIPSTONE;
            }
        });

        // Lush caves.
        handler.getSounds().add(new BiomeSound(handler.getPlayer()) {
            @Override
            public boolean isValidBiomeCondition(RegistryEntry<Biome> holder, RegistryKey<Biome> key) {
                return key.equals(BiomeKeys.LUSH_CAVES);
            }

            @Override
            public boolean isValidPlayerCondition() {
                return true;
            }

            @Nullable
            @Override
            public SoundEvent getSound() {
                return LUSH;
            }
        });
    }
}
