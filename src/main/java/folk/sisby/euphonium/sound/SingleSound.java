package folk.sisby.euphonium.sound;

import folk.sisby.euphonium.EuphoniumClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class SingleSound extends MovingSoundInstance {
	private final PlayerEntity player;

	public SingleSound(PlayerEntity player, SoundEvent sound, float volume, float pitch, @Nullable BlockPos pos) {
		super(sound, EuphoniumClient.CONFIG.channel, Random.create());

		this.player = player;
		this.repeat = false;
		this.repeatDelay = 0;
		this.pitch = pitch;
		this.volume = volume;

		if (pos != null) {
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
		} else {
			this.relative = true;
		}
	}

	@Override
	public void tick() {
		if (player == null || !player.isAlive()) {
			this.setDone();
		}
	}
}
