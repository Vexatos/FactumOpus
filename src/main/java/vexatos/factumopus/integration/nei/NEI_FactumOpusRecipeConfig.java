package vexatos.factumopus.integration.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.recipe.TemplateRecipeHandler;

/**
 * @author neptunepink, Vexatos
 */
public class NEI_FactumOpusRecipeConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		register(new RecipeFOCrystallizer());
		register(new RecipeFOMixer());
	}

	private void register(TemplateRecipeHandler it) {
		API.registerRecipeHandler(it);
		API.registerUsageHandler(it);
	}

	@Override
	public String getName() {
		return "FactumOpus Recipes";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}
}
