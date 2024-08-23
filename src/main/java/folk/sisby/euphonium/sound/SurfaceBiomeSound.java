package folk.sisby.euphonium.sound;

import folk.sisby.euphonium.EuphoniumClient;
import folk.sisby.euphonium.helper.WorldHelper;
import net.minecraft.entity.player.PlayerEntity;

public abstract class SurfaceBiomeSound extends BiomeSound {
	protected boolean playWhenThundering;

	protected SurfaceBiomeSound(PlayerEntity player, boolean playWhenThundering) {
		super(player);
		this.playWhenThundering = playWhenThundering;
	}

	@Override
	public boolean isValidPlayerCondition() {
		if (WorldHelper.isThundering(getPlayer()) && !playWhenThundering) {
			return false;
		}
		return WorldHelper.isOutside(getPlayer());
	}

	@Override
	public double getVolumeScaling() {
		var cullDistance = EuphoniumClient.CONFIG.biomeAmbience.cullSoundAboveGround;

		if (cullDistance > 0) {
			var distanceFromGround = WorldHelper.distanceFromGround(getPlayer(), cullDistance);
			var multiplier = 1.0D - (distanceFromGround / cullDistance);
			return super.getVolumeScaling() * Math.max(0.0D, multiplier);
		} else {
			return super.getVolumeScaling();
		}
	}
}
