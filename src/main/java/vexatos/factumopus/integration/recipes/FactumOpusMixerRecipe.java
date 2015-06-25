package vexatos.factumopus.integration.recipes;

import factorization.api.crafting.IVexatiousCrafting;
import factorization.crafting.TileEntityMixer;
import factorization.util.ItemUtil;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vexatos
 */
public class FactumOpusMixerRecipe implements IVexatiousCrafting<TileEntityMixer> {

	public List<OreDictItemStack> inputs;
	public List<ItemStack> outputs;

	public FactumOpusMixerRecipe(List<OreDictItemStack> inputs, List<ItemStack> outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}

	@Override
	public boolean matches(TileEntityMixer mixer) {
		for(OreDictItemStack input : inputs) {
			int matcount = countMaterial(mixer, input);
			if(matcount < input.stackSize) {
				return false;
			}
		}
		return true;
	}

	protected int countMaterial(TileEntityMixer mixer, OreDictItemStack toMatch) {
		int count = 0;

		for(ItemStack stack : mixer.input) {
			if(stack != null && ((toMatch.stack != null && ItemUtil.wildcardSimilar(toMatch.stack, stack)) ||
				(toMatch.tag != null && !toMatch.tag.isEmpty() && ItemUtil.oreDictionarySimilar(toMatch.tag, stack)))) {
				count += stack.stackSize;
			}
		}
		return count;
	}

	@Override
	public void onCraftingStart(TileEntityMixer mixer) {

	}

	@Override
	public void onCraftingComplete(TileEntityMixer mixer) {
		for(OreDictItemStack input : this.inputs) {
			consumeInput(mixer, input);
		}
		for(ItemStack output : this.outputs) {
			if(ItemUtil.normalize(output) != null) {
				insert(mixer.outputBuffer, output.copy());
			}
		}
		mixer.markDirty();
	}

	private void consumeInput(TileEntityMixer mixer, OreDictItemStack input) {
		int remaining = input.stackSize;
		for(int i = 0; i < mixer.input.length; i++) {
			if(mixer.input[i] != null && ((input.stack != null && ItemUtil.wildcardSimilar(input.stack, mixer.input[i])) ||
				(input.tag != null && !input.tag.isEmpty() && ItemUtil.oreDictionarySimilar(input.tag, mixer.input[i])))) {
				int newSize = mixer.input[i].stackSize - remaining;
				int remainingSize = newSize < 0 ? Math.abs(newSize) : 0;
				mixer.input[i].stackSize = newSize;
				mixer.input[i] = ItemUtil.normalize(mixer.input[i]);
				if(remainingSize <= 0) {
					return;
				}
				remaining = remainingSize;
			}
		}
	}

	private void insert(ArrayList<ItemStack> slots, ItemStack stackToInsert) {
		for(ItemStack stackInSlot : slots) {
			if(ItemUtil.normalize(stackInSlot) != null && ItemUtil.couldMerge(stackToInsert, stackInSlot)) {
				int newSize = stackInSlot.stackSize + stackToInsert.stackSize;
				int remainingSize = 0;
				int maxSize = stackInSlot.getMaxStackSize();
				if(newSize > maxSize) {
					remainingSize = newSize - maxSize;
					newSize = maxSize;
				}
				stackInSlot.stackSize = newSize;
				if(remainingSize <= 0) {
					return;
				}
				stackToInsert.stackSize = remainingSize;
			}
		}
		slots.add(stackToInsert);
	}

	@Override
	public boolean isUnblocked(TileEntityMixer mixer) {
		boolean[] slots = new boolean[mixer.output.length];
		for(ItemStack output : this.outputs) {
			boolean found = false;
			for(int i = 0; i < mixer.output.length; i++) {
				if(mixer.output[i] == null && !slots[i]) {
					slots[i] = true;
					found = true;
					break;
				}
				if(mixer.output[i] != null && !slots[i] && ItemUtil.couldMerge(mixer.output[i], output)
					&& mixer.output[i].stackSize + output.stackSize <= mixer.output[i].getMaxStackSize()) {
					slots[i] = true;
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		}

		return true;
	}
}
