package vexatos.factumopus.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vexatos.factumopus.FactumOpus;

/**
 * @author Vexatos
 */
public class CreativeTabFactumOpus extends CreativeTabs {

	public CreativeTabFactumOpus() {
		super("tabFactumOpus");
	}

	protected ItemStack iconStack;

	@SideOnly(Side.CLIENT)
	public ItemStack getIconItemStack() {
		Item item = getTabIconItem();
		if(this.iconStack == null && item != null) {
			this.iconStack = new ItemStack(item, 1, this.func_151243_f());
		}

		return this.iconStack;
	}

	@Override
	public Item getTabIconItem() {
		return FactumOpus.itemBowls;
	}
}
