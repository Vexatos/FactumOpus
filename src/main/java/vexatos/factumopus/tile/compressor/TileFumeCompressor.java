package vexatos.factumopus.tile.compressor;

import factorization.api.Charge;
import factorization.api.Coord;
import factorization.api.IChargeConductor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * @author Vexatos
 */
public class TileFumeCompressor extends TileEntity implements IFluidHandler, IChargeConductor {

	public static enum Mode {
		NONE, INPUT, COMPRESSING, OUTPUT
	}

	public static FluidStack water_stack, fume_stack, essence_stack, goo_stack;

	public static void setupFluidStacks() {
		if(water_stack == null) {
			water_stack = new FluidStack(FluidRegistry.WATER, 0);
			fume_stack = new FluidStack(FluidRegistry.getFluid("factumopus.voidfumes"), 0);
			essence_stack = new FluidStack(FluidRegistry.getFluid("factumopus.voidessence"), 0);
			goo_stack = new FluidStack(FluidRegistry.getFluid("factumopus.voidgoo"), 0);
		}

	}

	private final FluidTank fumeTank;
	private final FluidTank essenceTank;
	private final FluidTank outputTank;
	private int airAmount = 0;
	private Mode mode = Mode.NONE;

	private boolean isValid;

	public TileFumeCompressor() {
		this.fumeTank = new FluidTank(new FluidStack(FluidRegistry.getFluid("factumopus.voidfumes"), 0), 8000);
		this.essenceTank = new FluidTank(new FluidStack(FluidRegistry.getFluid("factumopus.voidessence"), 0), 3000);
		this.outputTank = new FluidTank(1000);
	}

	public Mode getMode() {
		return mode;
	}

	public FluidTank getFumeInputTank() {
		return fumeTank;
	}

	public FluidTank getEssenceInputTank() {
		return fumeTank;
	}

	public FluidTank getOutputTank() {
		return fumeTank;
	}

	public void onMultiblockDeconstructed() {
		isValid = false;
	}

	private Charge charge = new Charge(this);

	@Override
	public Charge getCharge() {
		return this.charge;
	}

	@Override
	public Coord getCoord() {
		return new Coord(this);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(getMode() == TileFumeCompressor.Mode.INPUT) {
			int fumeAmt = getFumeInputTank().getFluidAmount();
			int essenceAmt = getEssenceInputTank().getFluidAmount();
			if(fumeAmt == 0 && essenceAmt == 0) {
				if(resource.isFluidEqual(TileFumeCompressor.fume_stack)) {
					return getFumeInputTank().fill(resource, doFill);
				} else if(resource.isFluidEqual(TileFumeCompressor.essence_stack)) {
					return getEssenceInputTank().fill(resource, doFill);
				}
			} else if(fumeAmt != 0 && resource.isFluidEqual(TileFumeCompressor.fume_stack)) {
				return getFumeInputTank().fill(resource, doFill);
			} else if(resource.isFluidEqual(TileFumeCompressor.essence_stack)) {
				return getEssenceInputTank().fill(resource, doFill);
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if(from == ForgeDirection.DOWN || from == ForgeDirection.UP) {
			return null;
		}
		return new FluidTankInfo[] { fumeTank.getInfo(), essenceTank.getInfo(), outputTank.getInfo() };
	}

	@Override
	public String getInfo() {
		// TODO Make this
		return null;
	}
}
