package vexatos.factumopus.integration.extrabees;

import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleFlowers;
import forestry.api.genetics.IAlleleSpecies;
import forestry.api.genetics.IMutation;
import forestry.api.recipes.RecipeManagers;
import forestry.apiculture.genetics.AlleleFlowers;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BiomeDictionary;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.integration.forestry.FlowerProviderSea;
import vexatos.factumopus.integration.forestry.MutationReqResNoRain;
import vexatos.factumopus.integration.forestry.SalineBeeSpecies;
import vexatos.factumopus.item.ItemMultiple;

/**
 * @author Vexatos
 */
public class IntegrationExtraBees {

	public static IAlleleSpecies speciesSalty;
	public static IAlleleSpecies speciesSaline;
	public static IMutation mutationSalty;
	public static IMutation mutationSaline;
	public static IAlleleFlowers sea;

	public static ItemMultiple itemPartsForestry;

	private static final String
		speciesStone = "extrabees.species.stone",
		speciesMineral = "extrabees.species.mineral",
		speciesOcean = "extrabees.species.ocean";

	public void preInit() {
		itemPartsForestry = new ItemMultiple("saline_comb");
		GameRegistry.registerItem(itemPartsForestry, "computronics.partsForestry");
	}

	public void init() {
		FactumOpus.log.info("Adding ExtraBees integration...");

		sea = new AlleleFlowers("flowersSea", new FlowerProviderSea(), true);

		speciesSalty = new SalineBeeSpecies("factumopus.speciesSalty", false, "factumopus.bees.species.salty",
			AlleleManager.alleleRegistry.getClassification("extrabees.genus.rocky"), "salinarum", 0xF1F1F1, 0x999999)
			.addProduct(new ItemStack(itemPartsForestry, 1, 0), 10).setEntityTexture("honeyBee");
		speciesSaline = new SalineBeeSpecies("factumopus.speciesSaline", false, "factumopus.bees.species.saline",
			AlleleManager.alleleRegistry.getClassification("extrabees.genus.rocky"), "salinis", 0xF1F1F1, 0x82DBFF)
			.addProduct(new ItemStack(itemPartsForestry, 1, 0), 30).setEntityTexture("tropicalBee")
			.setTemperature(EnumTemperature.WARM).setHumidity(EnumHumidity.DAMP).setHasEffect();
		mutationSalty = new MutationReqResNoRain(AlleleManager.alleleRegistry.getAllele(speciesMineral),
			AlleleManager.alleleRegistry.getAllele(speciesStone), getSaltyTemplate(), 10,
			new ItemStack(FactumOpus.pondBase, 1, 0));
		mutationSaline = new MutationReqResNoRain(speciesSalty,
			AlleleManager.alleleRegistry.getAllele(speciesOcean), getSalineTemplate(), 10,
			new ItemStack(FactumOpus.pondBase, 1, 0))
			.requireNoRain()
			.restrictBiomeType(BiomeDictionary.Type.OCEAN).enableStrictBiomeCheck().requireDay().setTemperature(0.5f, 1.0f);

		AlleleManager.alleleRegistry.getSpeciesRoot("rootBees").registerTemplate(getSaltyTemplate());
		AlleleManager.alleleRegistry.getSpeciesRoot("rootBees").registerTemplate(getSalineTemplate());
		RecipeManagers.centrifugeManager.addRecipe(80,
			new ItemStack(itemPartsForestry, 1, 0),
			new ItemStack(FactumOpus.itemMaterial, 1, 1));
	}

	public static IAllele[] getSaltyTemplate() {
		IAllele[] alleles = AlleleManager.alleleRegistry.getSpeciesRoot("rootBees").getTemplate(speciesMineral).clone();

		alleles[EnumBeeChromosome.SPECIES.ordinal()] = speciesSalty;
		alleles[EnumBeeChromosome.FLOWERING.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.floweringSlowest");
		alleles[EnumBeeChromosome.TOLERANT_FLYER.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.boolFalse");
		alleles[EnumBeeChromosome.FERTILITY.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.fertilityLow");
		alleles[EnumBeeChromosome.EFFECT.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.effectNone");
		return alleles;
	}

	public static IAllele[] getSalineTemplate() {
		IAllele[] alleles = AlleleManager.alleleRegistry.getSpeciesRoot("rootBees").getTemplate(speciesOcean).clone();

		alleles[EnumBeeChromosome.SPECIES.ordinal()] = speciesSaline;
		alleles[EnumBeeChromosome.FLOWERING.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.floweringSlowest");
		alleles[EnumBeeChromosome.TOLERANT_FLYER.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.boolFalse");
		alleles[EnumBeeChromosome.CAVE_DWELLING.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.boolFalse");
		alleles[EnumBeeChromosome.FERTILITY.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.fertilityLow");
		alleles[EnumBeeChromosome.NOCTURNAL.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.boolFalse");
		alleles[EnumBeeChromosome.TEMPERATURE_TOLERANCE.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.toleranceUp1");
		alleles[EnumBeeChromosome.FLOWER_PROVIDER.ordinal()] = sea;
		alleles[EnumBeeChromosome.EFFECT.ordinal()] = AlleleManager.alleleRegistry.getAllele("forestry.effectNone");
		return alleles;
	}
}

