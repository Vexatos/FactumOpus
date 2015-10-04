package vexatos.factumopus.fluid;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fluids.IFluidBlock;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.util.RayTracer;

import java.util.HashMap;

/**
 * @author Vexatos
 */
public class ContainerHandler {

	public HashMap<Block, Item> buckets = new HashMap<Block, Item>();

	private static boolean isFluidSourceBlock(World world, int x, int y, int z) {
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
		if(bucket != null && isFluidSourceBlock(world, target.blockX, target.blockY, target.blockZ)) {
			world.setBlockToAir(target.blockX, target.blockY, target.blockZ);
			result = new ItemStack(bucket);
		} else {
			return;
		}

		e.result = result;
		e.setResult(Event.Result.ALLOW);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.world != null && event.action == Action.RIGHT_CLICK_AIR) {
			ItemStack heldStack = event.entityPlayer.getCurrentEquippedItem();

			if(heldStack != null && heldStack.getItem() == Items.glass_bottle
				&& event.entityPlayer.boundingBox != null && event.entityPlayer.boundingBox.minY < 5f) {
				MovingObjectPosition pos = RayTracer.raytraceFromPlayer(event.world, event.entityPlayer, false);

				if(pos == null) {
					ItemStack newStack = new ItemStack(FactumOpus.itemBottles, 1, 0);

					--heldStack.stackSize;
					if(heldStack.stackSize == 0) {
						event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, null);
					}

					if(!event.entityPlayer.inventory.addItemStackToInventory(newStack)) {
						event.entityPlayer.dropPlayerItemWithRandomChoice(newStack, true);
					}

					event.entityPlayer.swingItem();
				}
			}
		}
	}
}
