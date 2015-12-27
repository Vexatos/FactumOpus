package vexatos.factumopus.integration.recipes;

import cpw.mods.fml.common.registry.GameRegistry;
import factorization.api.crafting.CraftingManagerGeneric;
import factorization.crafting.TileEntityMixer;
import factorization.oreprocessing.ItemOreProcessing.OreType;
import factorization.oreprocessing.TileEntityCrystallizer;
import factorization.oreprocessing.TileEntityGrinder;
import factorization.oreprocessing.TileEntitySlagFurnace;
import factorization.shared.Core;
import factorization.weird.TileEntityDayBarrel;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.reference.Mods;

import java.util.Arrays;

/**
 * @author Vexatos
 */
public class ModRecipes {

	public void registerRecipes() {
		GameRegistry.addShapelessRecipe(new ItemStack(FactumOpus.itemBowls, 1, 2),
			new ItemStack(FactumOpus.itemMaterial, 1, 1), new ItemStack(FactumOpus.itemMaterial, 1, 1),
			new ItemStack(FactumOpus.itemMaterial, 1, 1), new ItemStack(FactumOpus.itemMaterial, 1, 1),
			Items.bowl);
		GameRegistry.addShapedRecipe(new ItemStack(FactumOpus.itemBowls, 1, 0),
			"v", "b", 'v', new ItemStack(FactumOpus.itemMaterial, 1, 0), 'b', new ItemStack(FactumOpus.itemBowls, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(FactumOpus.itemBottles, 1, 2),
			new ItemStack(FactumOpus.itemSulfurTrioxide, 1, 0), Items.glass_bottle);
		GameRegistry.addShapelessRecipe(new ItemStack(FactumOpus.itemSulfurTrioxide, 1, 0),
			new ItemStack(FactumOpus.itemBottles, 1, 2));

		GameRegistry.addShapelessRecipe(new ItemStack(FactumOpus.itemSalts, 4, 3),
			new ItemStack(FactumOpus.itemSalts, 1, 0));
		GameRegistry.addShapelessRecipe(new ItemStack(FactumOpus.itemSalts, 4, 4),
			new ItemStack(FactumOpus.itemSalts, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(FactumOpus.itemSalts, 4, 8),
			new ItemStack(FactumOpus.itemSalts, 1, 5));
		GameRegistry.addShapelessRecipe(new ItemStack(FactumOpus.itemSalts, 4, 9),
			new ItemStack(FactumOpus.itemSalts, 1, 7));

		GameRegistry.addSmelting(new ItemStack(FactumOpus.itemSalts, 1, 0),
			new ItemStack(FactumOpus.itemSalts, 1, 1), 0.1F);
		GameRegistry.addSmelting(new ItemStack(FactumOpus.itemSalts, 1, 5),
			new ItemStack(FactumOpus.itemSalts, 1, 6), 0.1F);
		GameRegistry.addSmelting(new ItemStack(FactumOpus.itemSalts, 1, 1),
			new ItemStack(FactumOpus.itemSalts, 1, 2), 0.1F);
		GameRegistry.addSmelting(new ItemStack(FactumOpus.itemSalts, 1, 6),
			new ItemStack(FactumOpus.itemSalts, 1, 7), 0.1F);

		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(FactumOpus.clayeySand), "sand", Items.clay_ball));

		registerFactorizationRecipes();
		String tag = "fo:airSealant";
		if(!OreDictionary.doesOreNameExist(tag)) {
			tag = "slimeball";
		}

		registerRailcraftComressorRecipes(tag);
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FactumOpus.blockCompressorWall, 1, 0),
			"plp", "ltl", "plp", 'p', tag, 't', "blockIron", 'l', Items.clay_ball));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FactumOpus.blockCompressorValve, 1, 0),
			"pip", "ltl", "pmp",
			'p', Blocks.iron_bars,
			't', FactumOpus.blockCompressorWall,
			'l', "ingotIron",
			'i', Blocks.heavy_weighted_pressure_plate,
			'm', Core.registry.motor));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FactumOpus.blockFumeCompressor, 1, 0),
			"plp", "cmc", "wtw",
			'p', tag,
			't', FactumOpus.blockCompressorValve,
			'l', FactumOpus.blockCompressorWall,
			'w', "ingotLead",
			'm', Core.registry.motor,
			'c', Core.registry.insulated_coil));
	}

	private void registerRailcraftComressorRecipes(String tag) {
		ItemStack tankWall = GameRegistry.findItemStack(Mods.Railcraft, "machine.beta.tank.steel.wall", 1);
		ItemStack tankValve = GameRegistry.findItemStack(Mods.Railcraft, "machine.beta.tank.steel.valve", 1);
		if(tankWall != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FactumOpus.blockCompressorWall, 1, 0),
				"pip", "ptp", "pmp",
				'p', tag,
				't', tankWall,
				'i', Blocks.heavy_weighted_pressure_plate,
				'm', Items.clay_ball));
		}
		if(tankValve != null) {
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(FactumOpus.blockCompressorValve, 1, 0),
				" p ", "ptp", "pmp",
				'p', tag,
				't', tankValve,
				'm', Core.registry.motor));
		}
	}

	public void registerFactorizationRecipes() {
		CraftingManagerGeneric<TileEntityCrystallizer> crystallizerRecipes = CraftingManagerGeneric.get(TileEntityCrystallizer.class);
		CraftingManagerGeneric<TileEntitySlagFurnace> slagFurnaceRecipes = CraftingManagerGeneric.get(TileEntitySlagFurnace.class);
		CraftingManagerGeneric<TileEntityMixer> mixerRecipes = CraftingManagerGeneric.get(TileEntityMixer.class);

		// Sulfur Trioxide related recipes
		slagFurnaceRecipes.add(new ColdBiomeSlagFurnaceRecipe(
			new ItemStack(FactumOpus.itemSalts, 1, 1),
			1.0F, new ItemStack(FactumOpus.itemSalts, 1, 2),
			1.0F, new ItemStack(FactumOpus.itemSulfurTrioxide, 1, 0)));
		slagFurnaceRecipes.add(new ColdBiomeSlagFurnaceRecipe(
			new ItemStack(FactumOpus.itemSalts, 1, 6),
			1.0F, new ItemStack(FactumOpus.itemSalts, 1, 7),
			1.0F, new ItemStack(FactumOpus.itemSulfurTrioxide, 1, 0)));
		{
			ItemStack doubleSulfuricAcid = Core.registry.sulfuric_acid.copy();
			doubleSulfuricAcid.stackSize = 2;
			mixerRecipes.add(new FactumOpusMixerRecipe(
				Arrays.asList(
					new OreDictItemStack(new ItemStack(FactumOpus.itemSulfurTrioxide, 1, 0)),
					new OreDictItemStack(new ItemStack(Items.potionitem, 1, 0)),
					new OreDictItemStack(Core.registry.sulfuric_acid.copy())),
				Arrays.asList(doubleSulfuricAcid.copy())));
			mixerRecipes.add(new FactumOpusMixerRecipe(
				Arrays.asList(
					new OreDictItemStack(new ItemStack(FactumOpus.itemBottles, 1, 2)),
					new OreDictItemStack(new ItemStack(Items.potionitem, 1, 0)),
					new OreDictItemStack(Core.registry.sulfuric_acid.copy())),
				Arrays.asList(
					doubleSulfuricAcid.copy(),
					new ItemStack(Items.glass_bottle, 1, 0))));
		}
		if(OreDictionary.getOres("dustSaltpeter").size() > 0) {
			crystallizerRecipes.add(new FactumOpusCrystallizerRecipe(
				Arrays.asList(new OreDictItemStack(Core.registry.sulfuric_acid.copy()),
					new OreDictItemStack("dustSaltpeter", 4)),
				new ItemStack(FactumOpus.itemSalts, 1, 0),
				Arrays.asList(new ItemStack(FactumOpus.itemAcidBottles, 1, 0), new ItemStack(FactumOpus.itemSalts, 3, 5)),
				Core.registry.sulfuric_acid.copy()));
			TileEntityGrinder.addRecipe("oreSaltpeter", OreDictionary.getOres("dustSaltpeter").get(0), 1.5f);
		}
		crystallizerRecipes.add(new FactumOpusCrystallizerRecipe(
			Arrays.asList(new OreDictItemStack(Core.registry.sulfuric_acid.copy()),
				new OreDictItemStack(new ItemStack(FactumOpus.itemBowls, 1, 2))),
			new ItemStack(FactumOpus.itemSalts, 1, 5),
			Arrays.asList(new ItemStack(FactumOpus.itemAcidBottles, 1, 1), new ItemStack(Items.bowl, 1, 0)),
			Core.registry.sulfuric_acid.copy()));
		mixerRecipes.add(new FactumOpusMixerRecipe(
			Arrays.asList(
				new OreDictItemStack(new ItemStack(FactumOpus.itemAcidBottles, 3, 1)),
				new OreDictItemStack(new ItemStack(FactumOpus.itemAcidBottles, 1, 0))),
			Arrays.asList(new ItemStack(FactumOpus.itemAcidBottles, 4, 2))));
		crystallizerRecipes.add(new FactumOpusCrystallizerRecipe(
			Arrays.asList(
				new OreDictItemStack(new ItemStack(FactumOpus.itemAcidBottles, 1, 2)),
				new OreDictItemStack(new ItemStack(Items.bowl, 1, 0)),
				new OreDictItemStack(new ItemStack(Items.iron_ingot), "ingotIron", 1)),
			new ItemStack(FactumOpus.itemBowls, 1, 1),
			Arrays.asList(new ItemStack(Items.glass_bottle, 1, 0)),
			Core.registry.aqua_regia.copy()));
		crystallizerRecipes.add(new FactumOpusCrystallizerRecipe(
			Arrays.asList(
				new OreDictItemStack(new ItemStack(FactumOpus.itemAcidBottles, 1, 2)),
				new OreDictItemStack(new ItemStack(FactumOpus.itemBowls, 1, 0))),
			Core.registry.ore_crystal.makeStack(OreType.DARKIRON),
			Arrays.asList(
				new ItemStack(FactumOpus.itemAcidBottles, 1, 2),
				new ItemStack(Items.bowl, 1, 0)),
			Core.registry.aqua_regia.copy()));
		TileEntityGrinder.addRecipe(FactumOpus.blockVoidGooSolid, new ItemStack(FactumOpus.itemMaterial, 1, 0), 14.5f);
		{
			//TileEntityDayBarrel.makeRecipe(new ItemStack(FactumOpus.blockVoidGooSolid, 1, 0), "blockSilver");
			ItemStack log = new ItemStack(FactumOpus.blockVoidGooSolid, 1, 0);
			ItemStack barrel = TileEntityDayBarrel.makeBarrel(TileEntityDayBarrel.Type.NORMAL,
				log, Core.registry.silver_block_item.copy());
			TileEntityDayBarrel.barrel_items.add(1, barrel);
			GameRegistry.addRecipe(new ShapedOreRecipe(barrel, "W-W", "W W", "WWW", 'W', log, '-', "blockSilver"));
		}
	}
}
