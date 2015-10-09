package vexatos.factumopus.integration.forestry;

import forestry.api.genetics.IAllele;
import forestry.api.genetics.IGenome;
import forestry.api.genetics.IMutationCondition;
import net.minecraft.world.World;

/**
 * @author Vexatos
 */
public class MutationConditionNoRain implements IMutationCondition {
	protected boolean rain = false;

	public MutationConditionNoRain requireRain() {
		this.rain = true;
		return this;
	}

	public MutationConditionNoRain requireNoRain() {
		this.rain = false;
		return this;
	}

	@Override
	public float getChance(World world, int i, int i1, int i2, IAllele iAllele, IAllele iAllele1, IGenome iGenome, IGenome iGenome1) {
		return world.isRaining() == this.rain ? 1.0F : 0.0F;
	}

	@Override
	public String getDescription() {
		return this.rain ? "Can only occur during rain." : "Can only occur without rain.";
	}
}
