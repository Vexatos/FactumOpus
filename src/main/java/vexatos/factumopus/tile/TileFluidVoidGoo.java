package vexatos.factumopus.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.block.BlockFluidVoidGoo;

/**
 * @author Vexatos
 */
public class TileFluidVoidGoo extends TileEntity {

	private int counter;
	private int percentage;
	private boolean active = false;

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(worldObj.isRemote || !active) {
			return;
		}
		int mod = counter > 0 ? 4 : 20;
		if(worldObj.getTotalWorldTime() % mod == hashCode() % mod) {
			if(worldObj.blockExists(xCoord, yCoord - 1, zCoord)
				&& BlockFluidVoidGoo.isGlowstone(worldObj.getBlock(xCoord, yCoord - 1, zCoord), xCoord, yCoord - 1, zCoord)) {
				if(!BlockFluidVoidGoo.isMoving(worldObj, xCoord, yCoord, zCoord)) {
					if(counter < 150) {
						counter++;
					}
					if(counter >= 150) {
						if(worldObj.rand.nextInt(100) < percentage) {
							worldObj.setBlock(xCoord, yCoord, zCoord, FactumOpus.blockVoidGooSolid);
							counter = 0;
							percentage = 0;
							active = false;
						} else {
							percentage++;
						}
					}
					return;
				}
			}
			counter = 0;
			percentage = 0;
			active = false;
		}
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
		active = tag.getBoolean("fo:active");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("fo:counter", counter);
		tag.setInteger("fo:percent", percentage);
		tag.setBoolean("fo:active", active);
	}
}
