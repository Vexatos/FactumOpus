package vexatos.factumopus.tile.compressor;

import factorization.api.Charge;
import factorization.api.Coord;
import factorization.api.DeltaCoord;
import factorization.api.IChargeConductor;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import vexatos.factumopus.tile.TileEntityFactumOpus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vexatos
 */
public class TileFumeCompressor extends TileEntityFactumOpus implements IChargeConductor {

	public static final DamageSource damageAirPressure = new DamageSourceAirPressure();

	public enum Mode {
		NONE, INPUT, COMPRESSING, OUTPUT;
		public static final Mode[] VALUES = values();

		public static Mode from(int ordinal) {
			if(ordinal < 0 || ordinal >= VALUES.length) {
				return NONE;
			}
			return VALUES[ordinal];
		}
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
	private FluidStack outputStack;

	private boolean isValid;
	private int motion;

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
						airAmount = 0;
						mode = Mode.NONE;
					}
				}
			} else {
				// Valid multiblock, do work.
				if(mode == Mode.INPUT) {
					if(getFumeInputTank().getFluidAmount() == getFumeInputTank().getCapacity()) {
						mode = Mode.COMPRESSING;
						this.outputStack = createOutput(essence_stack);
						getFumeInputTank().getFluid().amount = 0;
					} else if(getEssenceInputTank().getFluidAmount() == getEssenceInputTank().getCapacity()) {
						mode = Mode.COMPRESSING;
						this.outputStack = createOutput(goo_stack);
						getEssenceInputTank().getFluid().amount = 0;
					}
				} else if(mode == Mode.COMPRESSING) {
					if(getCoord().isPowered()) {
						this.charge.update();
						return;
					}
					if(motion <= 0) {
						int chargeNeeded = 8;
						int depleted = this.charge.deplete(chargeNeeded);
						if(depleted >= chargeNeeded) {
							motion += 2;
							if(motion >= 0) {
								motion = 100;
							}
						}
					} else {
						// k = 0.071297 or 1/80 (2 log(2)+log(3)+2 log(5)) or 0.07129728093320252
						// Air: 28.97 g/mol, W = pV(low) * ln(p(low)/p(high))
						// pV = nRT = 293.15 * 8.314 * (0.8*24)
						// 46795,18272
						//int chargeNeeded = (int) Math.round(Math.max(Math.exp(0.07129728093320252 * (((double) this.airAmount / 64000D) * 80D)), 8));
						int chargeNeeded = (int) Math.round(Math.max(3.75 * (double) this.airAmount / 56000D * 80D, 8));
						int depleted = this.charge.deplete(chargeNeeded);
						if(depleted >= chargeNeeded) {
							motion -= 2;
							airAmount += 40; // 800 mB per second
							if(airAmount >= getAirRequired()) {
								getOutputTank().setFluid(outputStack.copy());
								this.outputStack = null;
								mode = Mode.OUTPUT;
								airAmount = 0;
								//} else {
								//airAmount += 800;
							}
							if(motion <= 0) {
								motion = -100;
							}
						}
					}
					sendNetworkUpdate();
				} else if(mode == Mode.OUTPUT) {
					if(getOutputTank().getFluidAmount() == 0) {
						getOutputTank().setFluid(null);
						mode = Mode.INPUT;
					}
				} else if(mode == Mode.NONE) {
					mode = getOutputTank().getFluidAmount() > 0 ? Mode.OUTPUT : Mode.INPUT;
				}
			}
			this.charge.update();
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

	public int getAirRequired() {
		//return 500;
		return essence_stack.isFluidEqual(this.outputStack) ? 56000 : 64000;
	}

	public FluidStack createOutput(FluidStack pattern) {
		FluidStack output = pattern.copy();
		output.amount = 1000;
		return output;
	}

	public void pressuriseEntities() {
		List entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord - 3, zCoord, xCoord + 1, yCoord - 1, zCoord + 1));
		for(Object o : entities) {
			if(o instanceof EntityLivingBase) {
				((EntityLivingBase) o).attackEntityFrom(damageAirPressure, (float) Math.ceil(((EntityLivingBase) o).getHealth() * ((float) airAmount / (float) getAirRequired())));
			}
		}
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

	public int getAirAmount() {
		return this.airAmount;
	}

	public int getMotion() {
		return this.motion;
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

	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		if(data.hasKey("fo:valid")) {
			this.isValid = data.getBoolean("fo:valid");
		}
		if(data.hasKey("fo:air")) {
			this.airAmount = data.getInteger("fo:air");
		}
		if(data.hasKey("fo:mode")) {
			this.mode = Mode.from(data.getInteger("fo:mode"));
		}
		if(data.hasKey("fo:motion")) {
			this.motion = data.getInteger("fo:motion");
		}
		if(data.hasKey("fo:fume")) {
			getFumeInputTank().readFromNBT(data.getCompoundTag("fo:fume"));
		}
		if(data.hasKey("fo:essence")) {
			getEssenceInputTank().readFromNBT(data.getCompoundTag("fo:essence"));
		}
		if(data.hasKey("fo:output")) {
			getOutputTank().readFromNBT(data.getCompoundTag("fo:output"));
		}
		if(data.hasKey("fo:outputstack")) {
			this.outputStack = FluidStack.loadFluidStackFromNBT(data.getCompoundTag("fo:outputstack"));
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		data.setBoolean("fo:valid", this.isValid);
		data.setInteger("fo:air", this.airAmount);
		data.setInteger("fo:mode", this.mode.ordinal());
		data.setInteger("fo:motion", this.motion);
		NBTTagCompound fumetag = new NBTTagCompound();
		getFumeInputTank().writeToNBT(fumetag);
		data.setTag("fo:fume", fumetag);
		NBTTagCompound essencetag = new NBTTagCompound();
		getEssenceInputTank().writeToNBT(essencetag);
		data.setTag("fo:essence", essencetag);
		NBTTagCompound outtag = new NBTTagCompound();
		getOutputTank().writeToNBT(outtag);
		data.setTag("fo:output", outtag);
		if(outputStack != null) {
			NBTTagCompound outstacktag = new NBTTagCompound();
			outputStack.writeToNBT(outstacktag);
			data.setTag("fo:outputstack", outstacktag);
		}
	}

	@Override
	public void readFromRemoteNBT(NBTTagCompound data) {
		super.readFromRemoteNBT(data);
		if(data.hasKey("fo:air")) {
			this.airAmount = data.getInteger("fo:air");
		}
		if(data.hasKey("fo:motion")) {
			this.motion = data.getInteger("fo:motion");
		}
	}

	@Override
	public void writeToRemoteNBT(NBTTagCompound data) {
		super.writeToRemoteNBT(data);
		data.setInteger("fo:air", this.airAmount);
		data.setInteger("fo:motion", this.motion);
	}

	@Override
	public void readData(ByteBuf data) {
		super.readData(data);
		this.motion = data.readInt();
	}

	@Override
	public void writeData(ByteBuf data) {
		super.writeData(data);
		data.writeInt(this.motion);
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

	public static class DamageSourceAirPressure extends DamageSource {

		public DamageSourceAirPressure() {
			super("factumopus.pressure");
			this.setDamageBypassesArmor();
			this.setDamageIsAbsolute();
		}
	}
}
