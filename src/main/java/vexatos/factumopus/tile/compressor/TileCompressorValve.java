package vexatos.factumopus.tile.compressor;

import factorization.api.IMeterInfo;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * @author Vexatos
 */
public class TileCompressorValve extends TileCompressorBase implements IFluidHandler, IMeterInfo {

	public enum Type {
		IN, OUT
	}

	private Type type = Type.IN;

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		TileFumeCompressor tile = getMaster();
		return tile != null && tile.yCoord - this.yCoord >= 4 ? Type.OUT : Type.IN;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(resource == null || !canFill(from, resource.getFluid())) {
			return 0;
		}
		TileFumeCompressor tile = getMaster();
		return tile != null ? tile.fill(from, resource, doFill) : 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if(resource == null || !canDrain(from, resource.getFluid())) {
			return null;
		}
		TileFumeCompressor tile = getMaster();
		return tile != null ? tile.drain(from, resource, doDrain) : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if(!canDrain(from, null)) {
			return null;
		}
		TileFumeCompressor tile = getMaster();
		return tile != null ? tile.drain(from, maxDrain, doDrain) : null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		TileFumeCompressor tile = getMaster();
		return getType() == Type.IN;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		//TileFumeCompressor tile = getMaster();
		return from == ForgeDirection.DOWN && getType() == Type.OUT;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		TileFumeCompressor tile = getMaster();
		return tile != null ? tile.getTankInfo(from) : null;
	}

	@Override
	public String getInfo() {
		// TODO Make this
		TileFumeCompressor tile = getMaster();
		return tile != null ? tile.getInfo() : "Not a valid structure!";
	}
}
