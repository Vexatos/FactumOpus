package vexatos.factumopus.integration.recipes;

import factorization.oreprocessing.TileEntitySlagFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;

/**
 * @author Vexatos
 */
public class ColdBiomeSlagFurnaceRecipe extends TileEntitySlagFurnace.SmeltingResult {

	public ColdBiomeSlagFurnaceRecipe(ItemStack input, float prob1, ItemStack output1, float prob2, ItemStack output2) {
		super(input, prob1, output1, prob2, output2);
	}

	@Override
	public boolean isUnblocked(TileEntitySlagFurnace machine) {
		return super.isUnblocked(machine);
	}

	@Override
	public void onCraftingComplete(TileEntitySlagFurnace machine) {
		BiomeGenBase biome = machine.getWorldObj().getBiomeGenForCoordsBody(machine.xCoord, machine.zCoord);
		if(biome != null && biome.temperature < 0.295f && super.isUnblocked(machine)) {
			super.onCraftingComplete(machine);
		} else {
			float prob2 = this.prob2;
			this.prob2 = -1f;
			super.onCraftingComplete(machine);
			this.prob2 = prob2;
		}
	}
}
