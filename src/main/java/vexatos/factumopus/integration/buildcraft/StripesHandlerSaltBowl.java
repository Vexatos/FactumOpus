package vexatos.factumopus.integration.buildcraft;

import buildcraft.api.transport.IStripesActivator;
import buildcraft.api.transport.IStripesHandler;
import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
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
public class StripesHandlerSaltBowl implements IStripesHandler {

	@Override
	@Optional.Method(modid = Mods.API.BuildCraftTransport)
	public StripesHandlerType getType() {
		return StripesHandlerType.ITEM_USE;
	}

	@Override
	@Optional.Method(modid = Mods.API.BuildCraftTransport)
	public boolean shouldHandle(ItemStack stack) {
		return stack.getItem() == Items.bowl;
	}

	@Override
	@Optional.Method(modid = Mods.API.BuildCraftTransport)
	public boolean handle(World world, int x, int y, int z, ForgeDirection direction, ItemStack stack, EntityPlayer player, IStripesActivator activator) {
		Block block = world.getBlock(x, y, z);
		if(block == FactumOpus.saltLayer) {
			activator.sendItem(new ItemStack(FactumOpus.itemBowls, 1, 2), direction.getOpposite());
			world.setBlockToAir(x, y, z);
			stack.stackSize--;
			if(stack.stackSize > 0) {
				activator.sendItem(stack, direction.getOpposite());
			}
			return true;
		}
		return false;
	}
}
