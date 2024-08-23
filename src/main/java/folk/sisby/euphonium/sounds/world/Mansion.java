package folk.sisby.euphonium.sounds.world;

import folk.sisby.euphonium.EuphoniumClient;
import folk.sisby.euphonium.helper.SoundHelper;
import folk.sisby.euphonium.helper.WorldHelper;
import folk.sisby.euphonium.sound.ISoundType;
import folk.sisby.euphonium.sound.RepeatedWorldSound;
import folk.sisby.euphonium.sound.SoundHandler;
import folk.sisby.euphonium.sound.WorldSound;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Mansion implements ISoundType<WorldSound> {
	public static SoundEvent SOUND;

	public Mansion() {
		SOUND = SoundHelper.sound(EuphoniumClient.id("world.mansion"));
	}

	public void addSounds(SoundHandler<WorldSound> handler) {
		if (!EuphoniumClient.CONFIG.worldAmbience.mansion) return;

		handler.getSounds().add(new RepeatedWorldSound(handler.getPlayer()) {
			@Override
			public boolean isValidSituationCondition() {
				var bb = new Box(player.getBlockPos()).expand(10);
				List<HostileEntity> monsters = level.getNonSpectatingEntities(HostileEntity.class, bb);

				var optBlock1 = BlockPos.findClosest(player.getBlockPos(), 8, 8, pos -> {
					Block block = level.getBlockState(pos).getBlock();
					return block == Blocks.DARK_OAK_PLANKS;
				});

				var optBlock2 = BlockPos.findClosest(player.getBlockPos(), 8, 8, pos -> {
					Block block = level.getBlockState(pos).getBlock();
					return block == Blocks.BIRCH_PLANKS;
				});

				if (optBlock1.isPresent() && optBlock2.isPresent()) {
					// Get a hostile mob's location as the source of the sound.
					var optMonster = monsters.stream().findAny();
					if (optMonster.isPresent()) {
						setPos(optMonster.get().getBlockPos());
						return true;
					}
				}

				return false;
			}

			@Override
			public boolean isValidPlayerCondition() {
				return !WorldHelper.isBelowSeaLevel(player);
			}

			@Nullable
			@Override
			public SoundEvent getSound() {
				return SOUND;
			}

			@Override
			public int getDelay() {
				return level.random.nextInt(200) + 400;
			}

			@Override
			public float getVolume() {
				return 0.82F;
			}
		});
	}
}
