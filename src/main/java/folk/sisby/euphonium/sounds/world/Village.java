 package folk.sisby.euphonium.sounds.world;

 import folk.sisby.euphonium.EuphoniumClient;
 import folk.sisby.euphonium.helper.SoundHelper;
 import folk.sisby.euphonium.helper.WorldHelper;
 import folk.sisby.euphonium.sound.ISoundType;
 import folk.sisby.euphonium.sound.SoundHandler;
 import folk.sisby.euphonium.sound.SurfaceWorldSound;
 import folk.sisby.euphonium.sound.WorldSound;
import org.jetbrains.annotations.Nullable;

 import java.util.List;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Box;

 public class Village implements ISoundType<WorldSound> {
     public static SoundEvent SOUND;

     public Village() {
         SOUND = SoundHelper.sound(EuphoniumClient.id("world.village"));
     }

     public void addSounds(SoundHandler<WorldSound> handler) {
         if (!EuphoniumClient.CONFIG.worldAmbience.village) return;

         handler.getSounds().add(new SurfaceWorldSound(handler.getPlayer()) {
             @Override
             public boolean isValidSituationCondition() {
                 Box bb = new Box(player.getBlockPos()).expand(32);
                 List<VillagerEntity> villagers = level.getNonSpectatingEntities(VillagerEntity.class, bb);

                 if (villagers.size() >= 2) {
                     VillagerEntity villager = villagers.get(player.getRandom().nextInt(villagers.size()));
                     setPos(villager.getBlockPos());
                     return true;
                 }

                 return false;
             }

             @Override
             public boolean isValidPlayerCondition() {
                 return super.isValidPlayerCondition()
                     && !WorldHelper.isNight(player);
             }

             @Nullable
             @Override
             public SoundEvent getSound() {
                 return SOUND;
             }

             @Override
             public int getDelay() {
                 return level.random.nextInt(400) + 320;
             }

             @Override
             public float getVolume() {
                 return 0.82F;
             }
         });
     }
 }
