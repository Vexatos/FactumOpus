package vexatos.factumopus.fluid;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.HashMap;

/**
 * @author Vexatos
 */
public class FluidContainerHandler {

	public HashMap<Block, Item> buckets = new HashMap<Block, Item>();

	private static boolean isFluidBlock(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		if(block instanceof IFluidBlock) {
			return ((IFluidBlock) block).canDrain(world, x, y, z);
		} else {
			return world.getBlockMetadata(x, y, z) == 0;
		}
	}

	@SubscribeEvent
	public void onFillBucket(FillBucketEvent e) {
		World world = e.world;
		MovingObjectPosition target = e.target;
		Block block = world.getBlock(target.blockX, target.blockY, target.blockZ);

		Item bucket = buckets.get(block);
		ItemStack result;
		if(bucket != null && isFluidBlock(world, target.blockX, target.blockY, target.blockZ)) {
			world.setBlockToAir(target.blockX, target.blockY, target.blockZ);
			result = new ItemStack(bucket);
		} else {
			return;
		}

		e.result = result;
		e.setResult(Event.Result.ALLOW);
	}

	/*@SubscribeEvent
	public void onRightClick() {
		// TODO Make Bottles work
	}*/
}
