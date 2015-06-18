package vexatos.factumopus.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenDesert;
import vexatos.factumopus.FactumOpus;

/**
 * @author Vexatos
 */
public class TileClaySand extends TileEntity {

	private int counter;
	private int percentage;
	private int ticksUntilCheck;
	private boolean active;

	@Override
	public boolean canUpdate() {
		return true;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if(worldObj.isRemote) {
			return;
		}
		ticksUntilCheck -= 1;
		if(ticksUntilCheck <= 0) {
			ticksUntilCheck = 100;
			active = shouldHarden();
		}
		if(!active) {
			percentage = 0;
			return;
		}
		if(worldObj.getTotalWorldTime() % 80 == hashCode() % 80) {
			if(counter < 7200) {
				counter += 80;
			}
			if(counter >= 7200) {
				if(worldObj.rand.nextInt(100) < percentage) {
					worldObj.setBlock(xCoord, yCoord, zCoord, FactumOpus.pondBase);
					counter = 0;
					percentage = 0;
					active = false;
				} else {
					percentage++;
				}
			}
		}
	}

	private boolean shouldHarden() {
		BiomeGenBase biome = worldObj.getBiomeGenForCoordsBody(xCoord, zCoord);
		return biome != null
			&& !(biome.temperature <= 0.95f || biome.rainfall > 0.2f)
			&& worldObj.isDaytime()
			&& (!worldObj.provider.hasNoSky)
			&& worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord)
			&& (biome instanceof BiomeGenDesert || (!worldObj.isRaining() && !worldObj.isThundering()));
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		counter = tag.getInteger("fo:counter");
		percentage = tag.getInteger("fo:percentage");
		active = tag.getBoolean("fo:active");
		ticksUntilCheck = tag.getInteger("fo:check");
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("fo:counter", counter);
		tag.setInteger("fo:percent", percentage);
		tag.setBoolean("fo:active", active);
		tag.setInteger("fo:check", ticksUntilCheck);
	}
}
