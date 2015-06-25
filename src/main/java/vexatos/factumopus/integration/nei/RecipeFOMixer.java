package vexatos.factumopus.integration.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import factorization.crafting.GuiMixer;
import factorization.crafting.TileEntityMixer;
import factorization.shared.Core;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vexatos.factumopus.integration.recipes.FactumOpusMixerRecipe;
import vexatos.factumopus.integration.recipes.OreDictItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author neptunepink, Vexatos
 */
public class RecipeFOMixer extends TemplateRecipeHandler {

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		//XXX NOTE: This is probably a lame implementation of this function.
		for(Object obj : TileEntityMixer.recipes) {
			if(obj instanceof FactumOpusMixerRecipe) {
				FactumOpusMixerRecipe cr = (FactumOpusMixerRecipe) obj;
				if(result == null) {
					arecipes.add(new CachedMixerRecipe(cr));
				} else {
					for(ItemStack otherOutput : cr.outputs) {
						if(otherOutput == null || result.isItemEqual(otherOutput)) {
							arecipes.add(new CachedMixerRecipe(cr));
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if(outputId.equals("fo.crystallizing")) {
			loadCraftingRecipes(null);
			return;
		}
		super.loadCraftingRecipes(outputId, results);
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		//XXX NOTE: This is probably a lame implementation of this function.
		for(Object obj : TileEntityMixer.recipes) {
			if(obj instanceof FactumOpusMixerRecipe) {
				checkUsageRecipe((FactumOpusMixerRecipe) obj, ingredient);
			}
		}
	}

	private void checkUsageRecipe(FactumOpusMixerRecipe cr, ItemStack ingredient) {
		if(ingredient == null) {
			arecipes.add(new CachedMixerRecipe(cr));
		} else {
			for(OreDictItemStack input : cr.inputs) {
				if(input != null) {
					if(input.stack != null && ingredient.isItemEqual(input.stack)) {
						arecipes.add(new CachedMixerRecipe(cr));
						return;
					} else if(input.tag != null) {
						ArrayList<ItemStack> ores = OreDictionary.getOres(input.tag);
						for(ItemStack ore : ores) {
							if(ingredient.isItemEqual(ore)) {
								arecipes.add(new CachedMixerRecipe(cr));
								return;
							}
						}
					}
				}
			}
		}
	}

	private static final List<SlotPosition> inputSlotPositions = Arrays.asList(
		new SlotPosition(33 + 18, 14),
		new SlotPosition(33, 14),
		new SlotPosition(33, 14 + 18),
		new SlotPosition(33 + 18, 14)
	);
	private static final List<SlotPosition> outputSlotPositions = Arrays.asList(
		new SlotPosition(107 + 18, 14),
		new SlotPosition(107, 14),
		new SlotPosition(107, 14 + 18),
		new SlotPosition(107 + 18, 14)
	);

	class CachedMixerRecipe extends CachedRecipe {

		private FactumOpusMixerRecipe cr;
		private ArrayList<PositionedStack> inputStacks = new ArrayList<PositionedStack>();
		private ArrayList<PositionedStack> outputStacks = new ArrayList<PositionedStack>();

		public CachedMixerRecipe(FactumOpusMixerRecipe cr) {
			this.cr = cr;
			for(int i = 0; i < cr.inputs.size(); i++) {
				OreDictItemStack orestack = cr.inputs.get(i);
				SlotPosition position = inputSlotPositions.get(i);
				if(position != null) {
					if(orestack.tag != null) {
						ArrayList<ItemStack> ores = OreDictionary.getOres(orestack.tag);
						ArrayList<ItemStack> inputStackList = new ArrayList<ItemStack>();
						for(ItemStack ore : ores) {
							ItemStack copy = ore.copy();
							copy.stackSize = orestack.stackSize;
							inputStackList.add(copy);
						}
						inputStacks.add(new PositionedStack(inputStackList, position.xPos, position.yPos));
					} else if(orestack.stack != null) {
						inputStacks.add(new PositionedStack(orestack.stack, position.xPos, position.yPos));
					}
				}
			}
			for(int i = 0; i < cr.outputs.size(); i++) {
				ItemStack stack = cr.outputs.get(i);
				SlotPosition position = outputSlotPositions.get(i);
				if(position != null && stack != null) {
					outputStacks.add(new PositionedStack(stack, position.xPos, position.yPos));
				}
			}
		}

		@Override
		public PositionedStack getResult() {
			return null;
		}

		@Override
		public PositionedStack getIngredient() {
			return null;
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return super.getIngredients();
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			ArrayList<PositionedStack> ret = new ArrayList<PositionedStack>();
			ret.addAll(getCycledIngredients(cycleticks % 20, inputStacks));
			ret.addAll(getCycledIngredients(cycleticks % 20, outputStacks));
			return ret;
		}
	}

	@Override
	public void loadTransferRects() {
		// this.transferRects.add(new RecipeTransferRect(new Rectangle(74, 23, 24, 18), "fo.mixing"));
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiMixer.class;
	}

	@Override
	public String getRecipeName() {
		return "Mixing";
	}

	@Override
	public String getGuiTexture() {
		return Core.gui_nei + "mixer.png";
	}

	@Override
	public String getOverlayIdentifier() {
		return "fo.mixing";
	}

	@Override
	public void drawExtras(int recipe) {
		super.drawExtras(recipe);
		drawProgressBar(74, 24, 176, 14, 24, 17, 80, 0);
	}
}
