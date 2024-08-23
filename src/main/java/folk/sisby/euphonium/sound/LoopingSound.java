package folk.sisby.euphonium.sound;

import folk.sisby.euphonium.EuphoniumClient;
import java.util.function.Predicate;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.random.Random;

public class LoopingSound extends MovingSoundInstance {
    public static int FADE_TIME = 140;

    private final PlayerEntity player;
    private int longTicks;
    private final Predicate<PlayerEntity> predicate;
    public float maxVolume;

    public LoopingSound(PlayerEntity player, SoundEvent sound, float volume, float pitch, Predicate<PlayerEntity> predicate) {
        super(sound, EuphoniumClient.CONFIG.channel, Random.create());

        this.maxVolume = volume;
        this.player = player;
        this.repeat = true;
        this.repeatDelay = 0;
        this.volume = 0.01F;
        this.pitch = pitch;
        this.relative = true;
        this.predicate = predicate;
        this.longTicks = -50;
    }

    @Override
    public void tick() {
        if (this.player.isAlive()) {

            if (predicate.test(this.player)) {
                ++this.longTicks;
            } else {
                this.longTicks -= 1;
            }

            this.longTicks = Math.min(this.longTicks, FADE_TIME);
            this.volume = Math.max(0.0F, Math.min((float) this.longTicks / FADE_TIME, 1.0F)) * maxVolume;

            boolean donePlaying = this.isDone();

            if (!donePlaying && this.volume == 0.0F && this.longTicks < -100)
                this.setDone();

        } else {
            this.setDone();
        }
    }
}
