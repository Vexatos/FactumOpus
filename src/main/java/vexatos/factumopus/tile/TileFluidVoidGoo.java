package vexatos.factumopus.tile;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.block.BlockFluidVoidGoo;

import java.util.ArrayList;

/**
 * @author Vexatos
 */
public class TileFluidVoidGoo extends TileEntity {

	private int counter;
	private int percentage;

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(!worldObj.isRemote && worldObj.getTotalWorldTime() % 4 == hashCode() % 4
			&& worldObj.blockExists(xCoord, yCoord - 1, zCoord)
			&& isGlowstone(worldObj.getBlock(xCoord, yCoord - 1, zCoord), xCoord, yCoord - 1, zCoord)) {
			if(!isMoving()) {
				if(counter < 600) {
					counter += 4;
				}
				if(counter >= 600) {
					if(worldObj.rand.nextInt(100) < percentage) {
						worldObj.setBlock(xCoord, yCoord, zCoord, FactumOpus.blockVoidGooSolid);
					} else {
						percentage++;
					}
				}
				return;
			}
		}
		counter = 0;
		percentage = 0;
	}

	private boolean isGlowstone(Block block, int x, int y, int z) {
		if(block == null || block == Blocks.air) {
			return false;
		}
		if(block == Blocks.glowstone) {
			return true;
		}
		ArrayList<ItemStack> glowstoneTypes = OreDictionary.getOres("glowstone");
		for(ItemStack type : glowstoneTypes) {
			if(type.getItem() instanceof ItemBlock && ((ItemBlock) type.getItem()).field_150939_a == block) {
				return true;
			}
		}
		return false;
	}

	public boolean isMoving() {
		return isNonSourceVoidGooFluidBlock(worldObj, xCoord - 1, yCoord, zCoord)
			|| isNonSourceVoidGooFluidBlock(worldObj, xCoord + 1, yCoord, zCoord)
			|| isNonSourceVoidGooFluidBlock(worldObj, xCoord, yCoord, zCoord - 1)
			|| isNonSourceVoidGooFluidBlock(worldObj, xCoord, yCoord, zCoord + 1);
	}

	private boolean isNonSourceVoidGooFluidBlock(World world, int x, int y, int z) {
		if(world.blockExists(x, y, z)) {
			Block block = world.getBlock(x, y, z);
			if(block instanceof BlockFluidVoidGoo
				&& !((BlockFluidVoidGoo) block).isSourceBlock(world, x, y, z)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public BlockFluidVoidGoo getBlockType() {
		return FactumOpus.blockVoidGooFluid;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		counter = tag.getInteger("fo:counter");
		percentage = tag.getInteger("fo:percentage");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("fo:counter", counter);
		tag.setInteger("fo:percent", percentage);
	}
}
