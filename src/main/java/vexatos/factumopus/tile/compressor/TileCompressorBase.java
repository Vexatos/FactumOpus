package vexatos.factumopus.tile.compressor;

import factorization.api.Coord;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * @author Vexatos
 */
public class TileCompressorBase extends TileEntity {

	@Override
	public boolean canUpdate() {
		return false;
	}

	protected final Coord master = Coord.ZERO.copy();

	public void setMaster(TileFumeCompressor tile) {
		if(tile == null) {
			master.set(Coord.ZERO);
		} else {
			master.set(tile);
		}
	}

	public Coord getMaster() {
		return this.master;
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if(master.blockExists()) {
			TileFumeCompressor te = master.getTE(TileFumeCompressor.class);
			if(te != null) {
				te.onMultiblockDeconstructed();
			} else {
				master.set(Coord.ZERO);
			}
		}
	}

	@Override
	public void invalidate() {
		super.invalidate();
		if(master.blockExists()) {
			TileFumeCompressor te = master.getTE(TileFumeCompressor.class);
			if(te != null) {
				te.onMultiblockDeconstructed();
			} else {
				master.set(Coord.ZERO);
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		master.readFromNBT("factumopus:", tag);
		master.setWorld(this.worldObj);
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		master.writeToNBT("factumopus:", tag);
	}
}
