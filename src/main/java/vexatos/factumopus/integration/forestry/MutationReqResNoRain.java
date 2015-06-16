package vexatos.factumopus.integration.forestry;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IGenome;
import forestry.apiculture.genetics.MutationReqRes;
import net.minecraft.item.ItemStack;

/**
 * @author Vexatos
 */
public class MutationReqResNoRain extends MutationReqRes {

	protected boolean requiresRain = false;
	protected boolean rain = false;

	public MutationReqResNoRain(IAllele allele0, IAllele allele1, IAllele[] template, int chance, ItemStack blockRequired) {
		super(allele0, allele1, template, chance, blockRequired);
		specialConditions.add("Requires a " + blockRequired.getDisplayName() + " below the apiary.");
	}

	public MutationReqResNoRain requireRain() {
		this.requiresRain = true;
		this.rain = true;
		specialConditions.add("Can only occur during rain.");
		return this;
	}

	public MutationReqResNoRain requireNoRain() {
		this.requiresRain = true;
		this.rain = false;
		specialConditions.add("Can only occur without rain.");
		return this;
	}

	@Override
	public float getChance(IBeeHousing housing, IAllele allele0, IAllele allele1, IGenome genome0, IGenome genome1) {
		float chance = super.getChance(housing, allele0, allele1, genome0, genome1);
		if(chance <= 0.0F) {
			return 0.0F;
		}
		if(!requiresRain) {
			return chance;
		}
		if(rain) {
			return housing.getWorld().isRaining() ? chance : 0.0F;
		} else {
			return housing.getWorld().isRaining() ? 0.0F : chance;
		}
	}
}
