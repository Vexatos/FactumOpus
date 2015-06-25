package vexatos.factumopus.tile;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenDesert;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.block.BlockBrine;

/**
 * @author Vexatos
 */
public class TilePondBase extends TileEntity {

	private int counter;
	private int percentage;
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
		if(worldObj.getTotalWorldTime() % 80 == hashCode() % 80) {
			active = shouldDry(worldObj, xCoord, yCoord + 2, zCoord);
			if(active && !hasValidFluid(worldObj, xCoord, yCoord + 1, zCoord)) {
				active = false;
				counter = 0;
				percentage = 0;
			}
			if(!active) {
				percentage = 0;
				return;
			}
			if(counter < 7200) {
				counter += 80;
			}
			if(counter >= 7200) {
				if(worldObj.rand.nextInt(100) < percentage) {
					setNextBlockInProcess(worldObj, xCoord, yCoord + 1, zCoord);
					counter = 0;
					percentage = 0;
					active = false;
				} else {
					percentage++;
				}
			}
		}
	}

	private static boolean setNextBlockInProcess(World world, int x, int y, int z) {
		if(!world.blockExists(x, y, z)) {
			return false;
		}
		Block block = world.getBlock(x, y, z);
		if(block == Blocks.water) {
			world.setBlock(x, y, z, FactumOpus.blockBrine);
			return true;
		} else if(block instanceof BlockBrine) {
			if(((BlockBrine) block).getFluid().getName().equalsIgnoreCase("factumopus.brine")) {
				world.setBlock(x, y, z, FactumOpus.blockBrineSaturated);
				return true;
			} else if(((BlockBrine) block).getFluid().getName().equalsIgnoreCase("factumopus.saturated_brine")) {
				world.setBlock(x, y, z, FactumOpus.saltLayer);
				return true;
			}
		}
		return false;
	}

	private static boolean hasValidFluid(World world, int x, int y, int z) {
		if(!world.blockExists(x, y, z)) {
			return false;
		}
		Block block = world.getBlock(x, y, z);
		if(block == Blocks.water && world.getBlockMetadata(x, y, z) == 0) {
			return !isInvalidOnAnySide(world, x, y, z, block);
		} else if(block instanceof BlockBrine && ((BlockBrine) block).isSourceBlock(world, x, y, z)) {
			return !isInvalidOnAnySide(world, x, y, z, block);
		}
		return false;
	}

	private static boolean shouldDry(World world, int x, int y, int z) {
		BiomeGenBase biome = world.getBiomeGenForCoordsBody(x, z);
		return biome != null
			&& biome.temperature > 0.95f && biome.rainfall <= 0.2f
			&& world.isDaytime()
			&& (!world.provider.hasNoSky)
			&& world.canBlockSeeTheSky(x, y, z)
			&& (biome instanceof BiomeGenDesert || (!world.isRaining() && !world.isThundering()));
	}

	public static boolean isInvalidOnAnySide(World world, int x, int y, int z, Block ownBlock) {
		return isInvalidBlock(world, x - 1, y, z, ownBlock)
			|| isInvalidBlock(world, x + 1, y, z, ownBlock)
			|| isInvalidBlock(world, x, y, z - 1, ownBlock)
			|| isInvalidBlock(world, x, y, z + 1, ownBlock);
	}

	public static boolean isInvalidBlock(World world, int x, int y, int z, Block ownBlock) {
		if(world.blockExists(x, y, z)) {
			Block block = world.getBlock(x, y, z);
			if((block instanceof BlockBrine && !((BlockBrine) block).isSourceBlock(world, x, y, z))
				|| (block == Blocks.water && world.getBlockMetadata(x, y, z) != 0)
				|| (ownBlock instanceof BlockBrine && block == Blocks.water)
				|| (ownBlock == FactumOpus.blockBrineSaturated && block == FactumOpus.blockBrine)) {
				return true;
			}
		}
		return false;
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
