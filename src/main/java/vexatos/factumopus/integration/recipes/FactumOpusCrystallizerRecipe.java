package vexatos.factumopus.integration.recipes;

import factorization.api.crafting.IVexatiousCrafting;
import factorization.oreprocessing.TileEntityCrystallizer;
import factorization.util.ItemUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author neptunepink, Vexatos
 */
public class FactumOpusCrystallizerRecipe implements IVexatiousCrafting<TileEntityCrystallizer> {

	public List<OreDictItemStack> inputs;
	public ItemStack output;
	public List<ItemStack> otherOutputs;
	public ItemStack solution;
	public int heat_amount = 300;
	public int cool_time;

	public FactumOpusCrystallizerRecipe(List<OreDictItemStack> inputs, ItemStack output, List<ItemStack> otherOutputs, ItemStack solution) {
		this.cool_time = TileEntityCrystallizer.default_crystallization_time;
		this.inputs = inputs;
		this.output = output;
		this.otherOutputs = otherOutputs;
		this.solution = solution;
	}

	@Override
	public boolean matches(TileEntityCrystallizer crys) {
		if(crys.output != null && !ItemUtil.couldMerge(crys.output, this.output)) {
			return false;
		}
		int freeSlots = 6;
		for(OreDictItemStack input : inputs) {
			int matcount = countMaterial(crys, input);
			if(matcount < input.stackSize) {
				return false;
			}
			if(matcount > input.stackSize) {
				--freeSlots;
			}
		}
		return otherOutputs.size() <= freeSlots;
	}

	// Had to copy this because of the package-local method
	protected int countMaterial(TileEntityCrystallizer crys, OreDictItemStack toMatch) {
		int count = 0;

		for(ItemStack stack : crys.inputs) {
			if(stack != null && ((toMatch.stack != null && ItemUtil.wildcardSimilar(toMatch.stack, stack)) ||
				(toMatch.tag != null && !toMatch.tag.isEmpty() && ItemUtil.oreDictionarySimilar(toMatch.tag, stack)))) {
				count += stack.stackSize;
			}
		}
		return count;
	}

	@Override
	public void onCraftingStart(TileEntityCrystallizer crys) {
		crys.heating_amount = this.heat_amount;
		crys.cool_time = this.cool_time;
		crys.growing_crystal = this.output.copy();
		crys.growing_crystal.stackSize = 1;
		crys.solution = solution;
	}

	@Override
	public void onCraftingComplete(TileEntityCrystallizer crys) {
		if(crys.output == null) {
			crys.output = this.output.copy();
		} else {
			crys.output.stackSize += this.output.stackSize;
			crys.output.stackSize = Math.min(crys.output.stackSize, crys.output.getMaxStackSize());
		}
		for(OreDictItemStack input : this.inputs) {
			consumeInput(crys, input);
		}
		for(ItemStack otherOutput : this.otherOutputs) {
			if(otherOutput != null) {
				insert(crys.inputs, otherOutput);
			}
		}
		crys.markDirty();
	}

	private void consumeInput(TileEntityCrystallizer crys, OreDictItemStack input) {
		int remaining = input.stackSize;
		for(int i = 0; i < crys.inputs.length; i++) {
			if(crys.inputs[i] != null && ((input.stack != null && ItemUtil.wildcardSimilar(input.stack, crys.inputs[i])) ||
				(input.tag != null && !input.tag.isEmpty() && ItemUtil.oreDictionarySimilar(input.tag, crys.inputs[i])))) {
				int newSize = crys.inputs[i].stackSize - remaining;
				int remainingSize = newSize < 0 ? Math.abs(newSize) : 0;
				crys.inputs[i].stackSize = newSize;
				crys.inputs[i] = ItemUtil.normalize(crys.inputs[i]);
				if(remainingSize <= 0) {
					return;
				}
				remaining = remainingSize;
			}
		}
	}

	private void insert(ItemStack[] inputs, ItemStack otherOutput) {
		for(ItemStack input : inputs) {
			if(input != null && ItemUtil.couldMerge(otherOutput, input)) {
				int newSize = input.stackSize + otherOutput.stackSize;
				int remainingSize = 0;
				int maxSize = input.getMaxStackSize();
				if(newSize > maxSize) {
					remainingSize = newSize - maxSize;
					newSize = maxSize;
				}
				input.stackSize = newSize;
				if(remainingSize <= 0) {
					return;
				}
				otherOutput.stackSize = remainingSize;
			}
		}
		for(int i = 0; i < inputs.length; i++) {
			if(inputs[i] == null) {
				inputs[i] = otherOutput;
				return;
			}
		}
	}

	@Override
	public boolean isUnblocked(TileEntityCrystallizer crys) {
		return crys.output.stackSize + this.output.stackSize <= crys.output.getMaxStackSize();
	}
}
