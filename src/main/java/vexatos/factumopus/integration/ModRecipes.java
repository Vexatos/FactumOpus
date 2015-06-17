package vexatos.factumopus.integration;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import vexatos.factumopus.FactumOpus;

/**
 * @author Vexatos
 */
public class ModRecipes {

	public static void registerRecipes() {
		GameRegistry.addShapelessRecipe(new ItemStack(FactumOpus.itemBowls, 1, 2),
			new ItemStack(FactumOpus.itemMaterial, 1, 1), new ItemStack(FactumOpus.itemMaterial, 1, 1),
			new ItemStack(FactumOpus.itemMaterial, 1, 1), new ItemStack(FactumOpus.itemMaterial, 1, 1),
			Items.bowl);
		GameRegistry.addShapelessRecipe(new ItemStack(FactumOpus.itemBottles, 1, 1),
			new ItemStack(FactumOpus.itemSulfurTrioxide, 1, 0), Items.glass_bottle);
		GameRegistry.addShapelessRecipe(new ItemStack(FactumOpus.itemSulfurTrioxide, 1, 0),
			new ItemStack(FactumOpus.itemBottles, 1, 1));

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
	}
}
