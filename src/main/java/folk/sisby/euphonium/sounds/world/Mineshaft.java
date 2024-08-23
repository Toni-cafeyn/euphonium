package folk.sisby.euphonium.sounds.world;

import folk.sisby.euphonium.EuphoniumClient;
import folk.sisby.euphonium.sound.ISoundType;
import folk.sisby.euphonium.sound.RepeatedWorldSound;
import folk.sisby.euphonium.sound.SoundHandler;
import folk.sisby.euphonium.sound.WorldSound;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import net.minecraft.block.Blocks;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;

 public class Mineshaft implements ISoundType<WorldSound> {
     public static SoundEvent SOUND;

     public Mineshaft() {
         SOUND = SoundEvent.of(EuphoniumClient.id("world.mineshaft"));
     }

     public void addSounds(SoundHandler<WorldSound> handler) {
         if (!EuphoniumClient.CONFIG.worldAmbience.mineshaft) return;

         handler.getSounds().add(new RepeatedWorldSound(handler.getPlayer()) {
             @Override
             public boolean isValidSituationCondition() {
                 // Find the closest rail block in the mineshaft.  This will become the sound source.
                 Optional<BlockPos> rail = BlockPos.findClosest(player.getBlockPos(), 8, 16, pos -> {
                     var block = level.getBlockState(pos).getBlock();
                     return block == Blocks.RAIL;
                 });

                 if (rail.isPresent()) {
                     setPos(rail.get());
                     return true;
                 }

                 return false;
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

             @Override
             public int getDelay() {
                 return level.random.nextInt(300) + 300;
             }

             @Override
             public float getVolume() {
                 return 0.8F;
             }
         });
     }
 }
