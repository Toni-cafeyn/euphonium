package folk.sisby.euphonium.sounds.biome;

import folk.sisby.euphonium.EuphoniumClient;
import folk.sisby.euphonium.helper.SoundHelper;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import folk.sisby.euphonium.sound.BiomeSound;
import folk.sisby.euphonium.sound.ISoundType;
import folk.sisby.euphonium.sound.SoundHandler;
import folk.sisby.euphonium.sound.SurfaceBiomeSound;

import org.jetbrains.annotations.Nullable;
import java.util.function.BiPredicate;

public class River implements ISoundType<BiomeSound> {
    public static SoundEvent SOUND;
    public static BiPredicate<RegistryEntry<Biome>, RegistryKey<Biome>> VALID_BIOME =
        (holder, key) -> key.equals(BiomeKeys.RIVER) || holder.isIn(ConventionalBiomeTags.RIVER);

    public River() {
        SOUND = SoundHelper.sound(EuphoniumClient.id("biome.river"));
    }

    @Override
    public void addSounds(SoundHandler<BiomeSound> handler) {
        handler.getSounds().add(new SurfaceBiomeSound(handler.getPlayer(), true) {
            @Nullable
            @Override
            public SoundEvent getSound() {
                return SOUND;
            }

            @Override
            public boolean isValidBiomeCondition(RegistryEntry<Biome> holder, RegistryKey<Biome> key) {
                return VALID_BIOME.test(holder, key);
            }
        });
    }
}
