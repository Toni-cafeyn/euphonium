package folk.sisby.euphonium.sounds.biome;

import folk.sisby.euphonium.EuphoniumClient;
import folk.sisby.euphonium.sound.BiomeSound;
import folk.sisby.euphonium.sound.ISoundType;
import folk.sisby.euphonium.sound.SoundHandler;
import folk.sisby.euphonium.sound.SurfaceBiomeSound;
import folk.sisby.euphonium.helper.WorldHelper;

import org.jetbrains.annotations.Nullable;
import java.util.function.Predicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.biome.Biome;

public class Taiga implements ISoundType<BiomeSound> {
    public static SoundEvent DAY_SOUND;
    public static SoundEvent NIGHT_SOUND;
    public static final Predicate<RegistryEntry<Biome>> VALID_BIOME =
        holder -> holder.isIn(BiomeTags.IS_TAIGA);

    public Taiga() {
        DAY_SOUND = SoundEvent.of(EuphoniumClient.id("biome.taiga.day"));
        NIGHT_SOUND = SoundEvent.of(EuphoniumClient.id("biome.taiga.night"));
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
