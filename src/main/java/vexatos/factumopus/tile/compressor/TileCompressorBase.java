package vexatos.factumopus.tile.compressor;

import factorization.api.Coord;
import net.minecraft.nbt.NBTTagCompound;
import vexatos.factumopus.tile.TileEntityFactumOpus;

/**
 * @author Vexatos
 */
public abstract class TileCompressorBase extends TileEntityFactumOpus {

	@Override
	public boolean canUpdate() {
		return false;
	}

	private final Coord master = Coord.ZERO.copy();

	public void setMaster(TileFumeCompressor tile) {
		if(tile == null) {
			getMasterCoord().set(Coord.ZERO);
		} else {
			getMasterCoord().set(tile);
		}
	}

	public Coord getMasterCoord() {
		if(master.w() == null) {
			master.setWorld(this.worldObj);
		}
		return this.master;
	}

	public TileFumeCompressor getMaster() {
		TileFumeCompressor tile = getMasterCoord().getTE(TileFumeCompressor.class);
		if(tile == null) {
			setMaster(null);
		}
		return tile != null && tile.isValidMultiblock() ? tile : null;
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if(worldObj != null && !worldObj.isRemote) {
			TileFumeCompressor te = getMasterCoord().getTE(TileFumeCompressor.class);
			if(te != null) {
				te.onMultiblockDeconstructed(xCoord, yCoord, zCoord);
			} else {
				getMasterCoord().set(Coord.ZERO);
			}
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if(worldObj != null && !worldObj.isRemote) {
			TileFumeCompressor te = getMasterCoord().getTE(TileFumeCompressor.class);
			if(te != null) {
				te.onMultiblockDeconstructed(xCoord, yCoord, zCoord);
			} else {
				getMasterCoord().set(Coord.ZERO);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		getMasterCoord().readFromNBT("factumopus:", tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		getMasterCoord().writeToNBT("factumopus:", tag);
	}
}
