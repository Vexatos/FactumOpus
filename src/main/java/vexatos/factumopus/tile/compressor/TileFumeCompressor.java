package vexatos.factumopus.tile.compressor;

import factorization.api.Charge;
import factorization.api.Coord;
import factorization.api.DeltaCoord;
import factorization.api.IChargeConductor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vexatos
 */
public class TileFumeCompressor extends TileEntity implements IChargeConductor {

	public enum Mode {
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

	private static final List<DeltaCoord> requiredValves;
	private static final List<DeltaCoord> requiredWalls;
	private static final List<DeltaCoord> requiredAny;
	private static final List<DeltaCoord> requiredAir;

	static {
		requiredValves = Arrays.asList(new DeltaCoord(0, -1, 0), new DeltaCoord(0, -4, 0));
		requiredWalls = new ArrayList<DeltaCoord>(24);
		for(int y = -1; y >= -4; y -= 3) {
			for(int x = -1; x <= 1; ++x) {
				for(int z = -1; z <= 1; ++z) {
					if(x == 0 && z == 0) {
						continue;
					}
					requiredWalls.add(new DeltaCoord(x, y, z));
				}
			}
		}
		for(int y = -2; y >= -3; --y) {
			requiredWalls.add(new DeltaCoord(-1, y, -1));
			requiredWalls.add(new DeltaCoord(-1, y, 1));
			requiredWalls.add(new DeltaCoord(1, y, -1));
			requiredWalls.add(new DeltaCoord(1, y, 1));
		}
		requiredAny = new ArrayList<DeltaCoord>(8);
		for(int y = -2; y >= -3; --y) {
			requiredAny.add(new DeltaCoord(0, y, -1));
			requiredAny.add(new DeltaCoord(0, y, 1));
			requiredAny.add(new DeltaCoord(-1, y, 0));
			requiredAny.add(new DeltaCoord(1, y, 0));
		}
		requiredAir = Arrays.asList(new DeltaCoord(0, -2, 0), new DeltaCoord(0, -3, 0));
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

	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			if(!isValidMultiblock()) {
				// Check Multiblock
				if(worldObj.getTotalWorldTime() % 20 == hashCode() % 20) {
					if(!checkMultiblock()) {
						isValid = false;
						getFumeInputTank().getFluid().amount = 0;
						getEssenceInputTank().getFluid().amount = 0;
						airAmount = 0;
						mode = Mode.NONE;
					}
				}
			} else {
				// Valid multiblock, do work.
				if(mode == Mode.INPUT) {
					if(getFumeInputTank().getFluidAmount() == getFumeInputTank().getCapacity()) {
						mode = Mode.COMPRESSING;
						getFumeInputTank().getFluid().amount = 0;
					} else if(getEssenceInputTank().getFluidAmount() == getEssenceInputTank().getCapacity()) {
						mode = Mode.COMPRESSING;
						getEssenceInputTank().getFluid().amount = 0;
					}
				} else if(mode == Mode.COMPRESSING) {
					if(airAmount >= 500) { //TODO change this
						FluidStack output = essence_stack.copy();
						output.amount = 1000;
						getOutputTank().setFluid(output);
						mode = Mode.OUTPUT;
						airAmount = 0;
					} else {
						++airAmount; // TODO make this scale and consume charge
					}
				} else if(mode == Mode.OUTPUT) {
					if(getOutputTank().getFluidAmount() == 0) {
						getOutputTank().setFluid(null);
						mode = Mode.INPUT;
					}
				} else if(mode == Mode.NONE) {
					mode = getOutputTank().getFluidAmount() > 0 ? Mode.OUTPUT : Mode.INPUT;
				}
			}
		}
	}

	private boolean checkMultiblock() {
		final ArrayList<TileCompressorBase> toNotify = new ArrayList<TileCompressorBase>(24);
		final Coord cthis = new Coord(this);
		for(DeltaCoord requiredValveDelta : requiredValves) {
			TileCompressorValve te = cthis.add(requiredValveDelta).getTE(TileCompressorValve.class);
			if(te == null || te.getMaster() != null) {
				return false;
			}
			toNotify.add(te);
		}
		for(DeltaCoord requiredWallDelta : requiredWalls) {
			TileCompressorWall te = cthis.add(requiredWallDelta).getTE(TileCompressorWall.class);
			if(te == null || te.getMaster() != null) {
				return false;
			}
			toNotify.add(te);
		}
		for(DeltaCoord requiredAirDelta : requiredAir) {
			Coord requiredAirCoord = cthis.add(requiredAirDelta);
			if(!requiredAirCoord.blockExists() || !requiredAirCoord.isAir()) {
				return false;
			}
		}
		for(DeltaCoord requiredAnyDelta : requiredAny) {
			TileCompressorBase te = cthis.add(requiredAnyDelta).getTE(TileCompressorBase.class);
			if(te == null || te.getMaster() != null) {
				return false;
			}
			toNotify.add(te);
		}
		for(TileCompressorBase tile : toNotify) {
			tile.setMaster(this);
		}
		isValid = true;
		return true;
	}

	public Mode getMode() {
		return mode;
	}

	public FluidTank getFumeInputTank() {
		return fumeTank;
	}

	public FluidTank getEssenceInputTank() {
		return essenceTank;
	}

	public FluidTank getOutputTank() {
		return outputTank;
	}

	public void onMultiblockDeconstructed() {
		isValid = false;
	}

	public boolean isValidMultiblock() {
		return this.isValid;
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

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if(from == ForgeDirection.DOWN && getMode() == Mode.OUTPUT && resource.isFluidEqual(getOutputTank().getFluid())) {
			return getOutputTank().drain(resource.amount, doDrain);
		}
		return null;
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if(from == ForgeDirection.DOWN && getMode() == Mode.OUTPUT) {
			return getOutputTank().drain(maxDrain, doDrain);
		}
		return null;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if(from == ForgeDirection.DOWN || from == ForgeDirection.UP) {
			return null;
		}
		return new FluidTankInfo[] { fumeTank.getInfo(), essenceTank.getInfo(), outputTank.getInfo() };
	}

	@Override
	public String getInfo() {
		// TODO Make this
		return "Valid: " + this.isValid
			+ "\nFume Tank: " + this.fumeTank.getFluidAmount()
			+ "\nEssence Tank: " + this.essenceTank.getFluidAmount()
			+ "\nOutput Tank: " + this.outputTank.getFluidAmount()
			+ "\nAir: " + this.airAmount
			+ "\nMode: " + this.getMode();
	}

	@Override
	public boolean canUpdate() {
		return true;
	}
}
