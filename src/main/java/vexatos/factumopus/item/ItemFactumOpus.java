package vexatos.factumopus.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.util.TooltipUtil;

import java.util.List;

public class ItemFactumOpus extends Item {

	public ItemFactumOpus() {
		this.setCreativeTab(FactumOpus.tab);
	}

	//Mostly stolen from Sangar
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean par4) {
		{
			TooltipUtil.addShiftTooltip(stack, tooltip);
		}
	}
}
