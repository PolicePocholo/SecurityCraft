package net.geforcemods.securitycraft.blocks.reinforced;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ReinforcedRedstoneLampBlock extends BaseReinforcedBlock
{
	public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

	public ReinforcedRedstoneLampBlock(Block.Properties properties, Block vB)
	{
		super(properties, vB);

		setDefaultState(getDefaultState().with(LIT, false));
	}
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext ctx)
	{
		return getDefaultState().with(LIT, ctx.getWorld().isBlockPowered(ctx.getPos()));
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
	{
		if(!world.isRemote)
		{
			boolean isLit = state.get(LIT);

			if(isLit != world.isBlockPowered(pos))
			{
				if(isLit)
					world.getPendingBlockTicks().scheduleTick(pos, this, 4);
				else
					world.setBlockState(pos, state.func_235896_a_(LIT), 2); //cycle
			}

		}
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand)
	{
		if(state.get(LIT) && !world.isBlockPowered(pos))
			world.setBlockState(pos, state.func_235896_a_(LIT), 2); //cycle
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(LIT);
	}
}
