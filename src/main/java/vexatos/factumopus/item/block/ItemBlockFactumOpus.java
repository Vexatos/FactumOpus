package vexatos.factumopus.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vexatos.factumopus.util.TooltipUtil;

import java.util.List;

public class ItemBlockFactumOpus extends ItemBlock {

	public ItemBlockFactumOpus(Block block) {
		super(block);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean wat) {
		TooltipUtil.addShiftTooltip(stack, list);
	}
}
