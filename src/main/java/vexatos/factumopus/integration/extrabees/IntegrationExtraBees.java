package vexatos.factumopus.integration.extrabees;

import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.apiculture.FlowerManager;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IAlleleBeeSpeciesCustom;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleFlowers;
import forestry.api.genetics.IMutation;
import forestry.api.recipes.RecipeManagers;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BiomeDictionary;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.integration.forestry.FlowerProviderSea;
import vexatos.factumopus.integration.forestry.MutationConditionNoRain;
import vexatos.factumopus.item.ItemMultiple;
import vexatos.factumopus.reference.Mods;

import java.util.HashMap;

/**
 * @author Vexatos
 */
public class IntegrationExtraBees {

	public static IAlleleBeeSpeciesCustom speciesSalty;
	public static IAlleleBeeSpeciesCustom speciesSaline;
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

		FlowerProviderSea providerSea = new FlowerProviderSea();
		sea = AlleleManager.alleleFactory.createFlowers(Mods.FactumOpus, "flowers", "sea", providerSea, true);
		FlowerManager.flowerRegistry.registerAcceptableFlowerRule(providerSea, providerSea.getFlowerType());

		speciesSalty = BeeManager.beeFactory.createSpecies("factumopus.speciesSalty", false, "Vexatos",
			"factumopus.bees.species.salty", "factumopus.bees.species.salty.description",
			AlleleManager.alleleRegistry.getClassification("extrabees.genus.rocky"), "salinarum", 0xF1F1F1, 0x999999)
			.addProduct(new ItemStack(itemPartsForestry, 1, 0), 0.1F);
		speciesSaline = BeeManager.beeFactory.createSpecies("factumopus.speciesSaline", false, "Vexatos",
			"factumopus.bees.species.saline", "factumopus.bees.species.saline.description",
			AlleleManager.alleleRegistry.getClassification("extrabees.genus.rocky"), "salinis", 0xF1F1F1, 0x82DBFF)
			.addProduct(new ItemStack(itemPartsForestry, 1, 0), 0.3F);
		speciesSaline.setTemperature(EnumTemperature.WARM).setHumidity(EnumHumidity.DAMP).setHasEffect();
		mutationSalty = BeeManager.beeMutationFactory.createMutation(
			(IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele(speciesMineral),
			(IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele(speciesStone), getSaltyTemplate(), 10).requireResource(FactumOpus.pondBase, 0);
		mutationSaline = BeeManager.beeMutationFactory.createMutation(
			speciesSalty,
			(IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele(speciesOcean), getSalineTemplate(), 10).requireResource(FactumOpus.pondBase, 0)
			.addMutationCondition(new MutationConditionNoRain().requireNoRain())
			.restrictBiomeType(BiomeDictionary.Type.OCEAN).requireDay().restrictTemperature(EnumTemperature.WARM, EnumTemperature.HOT);

		BeeManager.beeRoot.registerTemplate(getSaltyTemplate());
		BeeManager.beeRoot.registerTemplate(getSalineTemplate());
		HashMap<ItemStack, Float> salineRecipe = new HashMap<ItemStack, Float>();
		salineRecipe.put(new ItemStack(FactumOpus.itemMaterial, 1, 1), 1.0f);
		RecipeManagers.centrifugeManager.addRecipe(80,
			new ItemStack(itemPartsForestry, 1, 0),
			salineRecipe);
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

