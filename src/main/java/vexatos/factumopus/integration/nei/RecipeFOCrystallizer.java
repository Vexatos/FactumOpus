package vexatos.factumopus.integration.nei;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import factorization.oreprocessing.GuiCrystallizer;
import factorization.oreprocessing.TileEntityCrystallizer;
import factorization.shared.Core;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;
import vexatos.factumopus.integration.recipes.FactumOpusCrystallizerRecipe;
import vexatos.factumopus.integration.recipes.OreDictItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static codechicken.lib.gui.GuiDraw.changeTexture;
import static codechicken.lib.gui.GuiDraw.drawTexturedModalRect;

/**
 * @author neptunepink, Vexatos
 */
public class RecipeFOCrystallizer extends TemplateRecipeHandler {

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		//XXX NOTE: This is probably a lame implementation of this function.
		for(Object obj : TileEntityCrystallizer.recipes) {
			if(obj instanceof FactumOpusCrystallizerRecipe) {
				FactumOpusCrystallizerRecipe cr = (FactumOpusCrystallizerRecipe) obj;
				if(result == null || result.isItemEqual(cr.output)) {
					arecipes.add(new CachedCrystallizerRecipe(cr));
				} else {
					for(ItemStack otherOutput : cr.otherOutputs) {
						if(otherOutput == null || result.isItemEqual(otherOutput)) {
							arecipes.add(new CachedCrystallizerRecipe(cr));
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
		for(Object obj : TileEntityCrystallizer.recipes) {
			if(obj instanceof FactumOpusCrystallizerRecipe) {
				checkUsageRecipe((FactumOpusCrystallizerRecipe) obj, ingredient);
			}
		}
	}

	private void checkUsageRecipe(FactumOpusCrystallizerRecipe cr, ItemStack ingredient) {
		if(ingredient == null) {
			arecipes.add(new CachedCrystallizerRecipe(cr));
		} else {
			for(OreDictItemStack input : cr.inputs) {
				if(input != null) {
					if(input.stack != null && ingredient.isItemEqual(input.stack)) {
						arecipes.add(new CachedCrystallizerRecipe(cr));
						return;
					} else if(input.tag != null) {
						ArrayList<ItemStack> ores = OreDictionary.getOres(input.tag);
						for(ItemStack ore : ores) {
							if(ingredient.isItemEqual(ore)) {
								arecipes.add(new CachedCrystallizerRecipe(cr));
								return;
							}
						}
					}
				}
			}
		}
	}

	private static final List<SlotPosition> inputSlotPositions = Arrays.asList(
		new SlotPosition(80, 13),
		new SlotPosition(108, 29),
		new SlotPosition(108, 55),
		new SlotPosition(80, 69),
		new SlotPosition(52, 55),
		new SlotPosition(52, 29)
	);

	class CachedCrystallizerRecipe extends CachedRecipe {

		private FactumOpusCrystallizerRecipe cr;
		private ArrayList<PositionedStack> inputStacks = new ArrayList<PositionedStack>();
		private ArrayList<PositionedStack> outputStacks = new ArrayList<PositionedStack>();

		public CachedCrystallizerRecipe(FactumOpusCrystallizerRecipe cr) {
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
						inputStacks.add(new PositionedStack(inputStackList, position.xPos - 5, position.yPos + 4));
					} else if(orestack.stack != null) {
						inputStacks.add(new PositionedStack(orestack.stack, position.xPos - 5, position.yPos + 4));
					}
				}
			}
			for(int i = 0; i < cr.otherOutputs.size(); i++) {
				ItemStack stack = cr.otherOutputs.get(i);
				SlotPosition position = inputSlotPositions.get(i);
				if(position != null && stack != null) {
					outputStacks.add(new PositionedStack(stack, position.xPos - 5, position.yPos + 4));
				}
			}
		}

		@Override
		public PositionedStack getResult() {
			return cycleticks % 160 >= 80 ? new PositionedStack(cr.output, 75, 29 + 15) : null;
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
			if(cycleticks % 160 >= 80) {
				ret.addAll(getCycledIngredients(cycleticks % 20, outputStacks));
			} else {
				ret.addAll(getCycledIngredients(cycleticks % 20, inputStacks));
			}
			ret.add(new PositionedStack(Core.registry.heater_item, 0, 75));
			return ret;
		}
	}

	@Override
	public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe) {
		if(stack == null) {
			return currenttip;
		}
		if(stack.isItemEqual(Core.registry.heater_item)) {
			currenttip.add("Use this to heat the crystallizer");
		}
		return currenttip;
	}

	@Override
	public void loadTransferRects() {
		// transferRects.add(new RecipeTransferRect(new Rectangle(63, 78, 95 - 63, 16), "fo.crystallizing"));
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiCrystallizer.class;
	}

	@Override
	public String getRecipeName() {
		return "Crystallization";
	}

	@Override
	public String getGuiTexture() {
		return Core.gui_nei + "crystal.png";
	}

	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1, 1, 1, 1);
		changeTexture(getGuiTexture());
		drawTexturedModalRect(0, 15, 5, 11, 166, 65 + 30);
	}

	@Override
	public String getOverlayIdentifier() {
		return "fo.crystallizing";
	}

	@Override
	public void drawExtras(int recipe) {
		super.drawExtras(recipe);
		if(cycleticks % 160 >= 80) {
			drawProgressBar(43 - 5, 89 + 4, 0, 192, 90, 16, 1.0f, 0);
			drawProgressBar(54 - 5, 75 + 4, 176, 0, 14, 13, 80, 3);
			drawProgressBar(109 - 5, 75 + 4, 176, 0, 14, 13, 80, 3);
		} else {
			drawProgressBar(43 - 5, 89 + 4, 0, 192, 90, 16, 80, 0);
		}
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}
}
