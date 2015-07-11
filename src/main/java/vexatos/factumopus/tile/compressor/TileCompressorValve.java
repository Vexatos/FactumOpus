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

	public static enum Type {
		IN, OUT
	}

	private Type type = Type.IN;

	public void setType(Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		TileFumeCompressor tile = master.getTE(TileFumeCompressor.class);
		return tile != null ? tile.fill(from, resource, doFill) : 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		TileFumeCompressor tile = master.getTE(TileFumeCompressor.class);
		return tile != null ? tile.drain(from, resource, doDrain) : null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		TileFumeCompressor tile = master.getTE(TileFumeCompressor.class);
		return tile != null ? tile.drain(from, maxDrain, doDrain) : null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		TileFumeCompressor tile = master.getTE(TileFumeCompressor.class);
		if(tile != null && tile.getMode() == TileFumeCompressor.Mode.INPUT && this.type == Type.IN) {
			int fumeAmt = tile.getFumeInputTank().getFluidAmount();
			int essenceAmt = tile.getEssenceInputTank().getFluidAmount();
			if(fumeAmt == 0 && essenceAmt == 0) {
				return fluid == TileFumeCompressor.fume_stack.getFluid()
					|| fluid == TileFumeCompressor.essence_stack.getFluid();
			} else if(fumeAmt != 0) {
				return fluid == TileFumeCompressor.fume_stack.getFluid();
			} else {
				return fluid == TileFumeCompressor.essence_stack.getFluid();
			}
		}
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		TileFumeCompressor tile = master.getTE(TileFumeCompressor.class);
		return from == ForgeDirection.DOWN && tile != null
			&& tile.getMode() == TileFumeCompressor.Mode.OUTPUT
			&& this.type == Type.OUT;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		TileFumeCompressor tile = master.getTE(TileFumeCompressor.class);
		return tile != null ? tile.getTankInfo(from) : null;
	}

	@Override
	public String getInfo() {
		// TODO Make this
		return null;
	}
}
