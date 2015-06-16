package vexatos.factumopus.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemMultiple extends ItemFactumOpus {

	protected final String mod;
	protected final String[] parts;
	protected final IIcon[] partIcons;

	public ItemMultiple(String mod, String[] parts) {
		super();
		this.mod = mod;
		this.parts = parts;
		this.partIcons = new IIcon[parts.length];
		this.setCreativeTab(CreativeTabs.tabMisc);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return this.partIcons[meta % partIcons.length];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister r) {
		super.registerIcons(r);
		for(int i = 0; i < parts.length; i++) {
			partIcons[i] = r.registerIcon(mod + ":" + parts[i]);
		}
	}

	@Override
	public String getUnlocalizedName() {
		return "item.factumopus.unknown";
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		if(stack == null) {
			return "item.factumopus.unknown";
		} else {
			return "item." + this.mod + "." + this.parts[stack.getItemDamage() % parts.length];
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unchecked")
	public void getSubItems(Item item, CreativeTabs tabs, List list) {
		for(int i = 0; i < parts.length; i++) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
