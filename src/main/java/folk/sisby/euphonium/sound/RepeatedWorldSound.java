package folk.sisby.euphonium.sound;

import folk.sisby.euphonium.EuphoniumClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

import java.util.ConcurrentModificationException;

public abstract class RepeatedWorldSound extends WorldSound {
	protected SingleSound soundInstance;
	protected BlockPos pos;
	protected int soundTicks = 100; // set something high here so it doesn't autoplay when player logs in

	public RepeatedWorldSound(PlayerEntity player) {
		super(player);
	}

	@Override
	public MovingSoundInstance getSoundInstance() {
		return soundInstance;
	}

	@Override
	public void tick() {
		if (--soundTicks >= 0)
			return;

		soundTicks = getDelay();
		isValid = isValid();

		if (isValid) {
			soundInstance = new SingleSound(getPlayer(), getSound(), (float) (getVolume() * getVolumeScaling()), getPitch(), getPos());
			var manager = getSoundManager();

			try {
				if (!manager.isPlaying(soundInstance)) {
					manager.play(soundInstance);
				}
			} catch (ConcurrentModificationException e) {
				EuphoniumClient.LOGGER.debug("{}: Exception in manager.play", this.getClass());
			}
		}
	}

	@Override
	public int getDelay() {
		return level.random.nextInt(200) + 200;
	}

	@Nullable
	public BlockPos getPos() {
		return pos;
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
	}

}
