package vexatos.factumopus.tile.compressor;

import cpw.mods.fml.common.Optional;
import factorization.api.Charge;
import factorization.api.Coord;
import factorization.api.DeltaCoord;
import factorization.api.IChargeConductor;
import factorization.shared.Core;
import micdoodle8.mods.galacticraft.api.world.IGalacticraftWorldProvider;
import micdoodle8.mods.galacticraft.api.world.OxygenHooks;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import vexatos.factumopus.reference.Mods;
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
	private int motion = 100;

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
				if(!hasAir()) {
					this.noAirOnTop = true;
					this.charge.update();
					return;
				} else if(noAirOnTop) {
					noAirOnTop = false;
				}
				final int oldMotion = motion;
				// Valid multiblock, do work.
				if(mode == Mode.INPUT) {
					if(motion <= 0) {
						if(tryDeplete(8)) {
							motion += 2;
							if(motion >= 0) {
								motion = 100;
							}
						}
					} else if(motion < 100) {
						motion = motion - 100;
					}
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
						if(tryDeplete(8)) {
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
						if(tryDeplete(chargeNeeded)) {
							if(motion >= 100) {
								click(false);
							}
							motion -= 2;
							airAmount += 40; // 800 mB per second
							if(motion <= 0) {
								motion = -100;
								pressuriseEntities();
								click(true);
							}
							if(airAmount >= getAirRequired()) {
								getOutputTank().setFluid(outputStack.copy());
								this.outputStack = null;
								mode = Mode.OUTPUT;
								airAmount = 0;
								if(motion > 0) {
									motion = motion - 100;
								}
								this.worldObj.playSoundEffect(xCoord + 0.5F, yCoord - 0.5F, zCoord + 0.5F, "factumopus:air_leak", 0.5F, 0.5F);
								//} else {
								//airAmount += 800;
							}
						}
					}
				} else if(mode == Mode.OUTPUT) {
					if(motion <= 0) {
						if(tryDeplete(8)) {
							motion += 2;
							if(motion >= 0) {
								motion = 100;
							}
						}
					} else if(motion < 100) {
						motion = motion - 100;
					}
					if(getOutputTank().getFluidAmount() == 0) {
						getOutputTank().setFluid(null);
						mode = Mode.INPUT;
					}
				} else if(mode == Mode.NONE) {
					mode = getOutputTank().getFluidAmount() > 0 ? Mode.OUTPUT : Mode.INPUT;
				}
				if(motion != oldMotion) {
					this.worldObj.addBlockEvent(xCoord, yCoord, zCoord, this.getBlockType(), 0, motion + 100);
				}
			}
			this.charge.update();
		}
	}

	private boolean hasAir() {
		if(Mods.hasVersion(Mods.API.Galacticraft, "[1.1,)")) {
			if(!hasAir_GC()) {
				return false;
			}
		}
		return worldObj.isAirBlock(xCoord, yCoord + 1, zCoord);
	}

	protected boolean noAirOnTop = false;
	protected boolean gcAirLast = true;
	protected boolean gcAirChecked = false;

	@Optional.Method(modid = Mods.API.Galacticraft)
	private boolean hasAir_GC() {
		if(!gcAirChecked || worldObj.getTotalWorldTime() % 5 == 0) {
			if(!gcAirChecked) {
				gcAirChecked = true;
			}
			WorldProvider provider = worldObj.provider;
			if(!(provider instanceof IGalacticraftWorldProvider)){
				gcAirLast = true;
				return true;
			}
			gcAirLast = ((IGalacticraftWorldProvider) provider).hasBreathableAtmosphere()
				|| OxygenHooks.isAABBInBreathableAirBlock(worldObj,
				AxisAlignedBB.getBoundingBox(xCoord + 0.1, yCoord + 0.5, zCoord + 0.1, xCoord + 0.9, yCoord + 1.5, zCoord + 0.9));
		}
		return gcAirLast;
	}

	private void click(boolean open) {
		worldObj.playSoundEffect((double) xCoord + 0.5D, (double) yCoord + 0.5D, (double) zCoord + 0.5D, "random.click", 0.1F, open ? 1.5F : 0.7F);
	}

	private boolean notEnoughCharge = false;

	private boolean tryDeplete(int chargeNeeded) {
		int depleted = this.charge.deplete(chargeNeeded);
		if(depleted >= chargeNeeded) {
			this.notEnoughCharge = false;
			return true;
		} else {
			this.notEnoughCharge = true;
			return false;
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

	public void onMultiblockDeconstructed(int x, int y, int z) {
		isValid = false;
		if(this.worldObj != null && !this.worldObj.isRemote && this.airAmount > 0) {
			this.worldObj.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, "factumopus:air_leak", 1F, 1F);
		}
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
	public boolean receiveClientEvent(int id, int val) {
		switch(id) {
			case 0: {
				this.motion = val - 100;
				return true;
			}
			default: {
				return super.receiveClientEvent(id, val);
			}
		}
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
		if(!this.isValid) {
			return "Not a valid structure!";
		}
		String result = "";
		if(this.notEnoughCharge) {
			result += "\nNot enough Charge!";
		}
		if(this.noAirOnTop) {
			result += "\nNo access to air on top!";
		}
		if(Core.dev_environ) {
			result += "\n\u00a7oState: " + this.getMode()
				+ "\n\u00a7oFume Tank: " + this.fumeTank.getFluidAmount()
				+ "\n\u00a7oEssence Tank: " + this.essenceTank.getFluidAmount()
				+ "\n\u00a7oOutput Tank: " + this.outputTank.getFluidAmount()
				+ "\n\u00a7oAir: " + this.airAmount;
		}

		return result;
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
