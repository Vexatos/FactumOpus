package vexatos.factumopus.integration.recipes;

import net.minecraft.item.ItemStack;

/**
 * @author Vexatos
 */
public class OreDictItemStack {

	public final ItemStack stack;
	public final String tag;
	public int stackSize;

	public OreDictItemStack(ItemStack stack, String tag, int stackSize) {
		this.stack = stack;
		this.stackSize = stackSize;
		this.tag = tag;
	}

	public OreDictItemStack(ItemStack stack) {
		this(stack, null, stack.stackSize);
	}

	public OreDictItemStack(String tag, int stackSize) {
		this(null, tag, stackSize);
	}
}
