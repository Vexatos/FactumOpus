package vexatos.factumopus.integration.forestry;

import forestry.api.genetics.IClassification;
import forestry.apiculture.genetics.AlleleBeeSpecies;
import forestry.apiculture.genetics.IJubilanceProvider;

/**
 * @author Vexatos
 */
public class SalineBeeSpecies extends AlleleBeeSpecies {

	public SalineBeeSpecies(String uid, boolean dominant, String name, IClassification branch, String binomial, int primaryColor, int secondaryColor) {
		super(uid, dominant, name, branch, binomial, primaryColor, secondaryColor);
	}

	public SalineBeeSpecies(String uid, boolean dominant, String name, IClassification branch, String binomial, int primaryColor, int secondaryColor, IJubilanceProvider jubilanceProvider) {
		super(uid, dominant, name, branch, binomial, primaryColor, secondaryColor, jubilanceProvider);
	}

	public SalineBeeSpecies(String uid, boolean dominant, String name, IClassification branch, int primaryColor, int secondaryColor) {
		super(uid, dominant, name, branch, primaryColor, secondaryColor);
	}

	@Override
	public String getAuthority() {
		return "Vexatos";
	}

	@Override
	public String getUID() {
		return this.uid;
	}

}
