package vexatos.factumopus.integration.buildcraft;

import buildcraft.api.transport.IStripesActivator;
import buildcraft.api.transport.IStripesHandler;
import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.reference.Mods;

/**
 * @author Vexatos
 */
@Optional.Interface(iface = "buildcraft.api.transport.IStripesHandler", modid = Mods.API.BuildCraftTransport)
public class StripesHandlerVoidFumes implements IStripesHandler {

	@Override
	@Optional.Method(modid = Mods.API.BuildCraftTransport)
	public StripesHandlerType getType() {
		return StripesHandlerType.ITEM_USE;
	}

	@Override
	@Optional.Method(modid = Mods.API.BuildCraftTransport)
	public boolean shouldHandle(ItemStack stack) {
		return stack.getItem() == Items.glass_bottle;
	}

	private static final ItemStack voidbottle = new ItemStack(FactumOpus.itemBowls, 1, 3);

	@Override
	@Optional.Method(modid = Mods.API.BuildCraftTransport)
	public boolean handle(World world, int x, int y, int z, ForgeDirection direction, ItemStack stack, EntityPlayer player, IStripesActivator activator) {
		Block block = world.getBlock(x, y, z);
		if(y < 5 && block == Blocks.air) {
			activator.sendItem(voidbottle.copy(), direction.getOpposite());
			stack.stackSize--;
			if(stack.stackSize > 0) {
				activator.sendItem(stack, direction.getOpposite());
			}
			return true;
		}
		return false;
	}
}
