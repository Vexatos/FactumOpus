package vexatos.factumopus.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.util.TooltipUtil;

import java.util.List;

/**
 * @author Vexatos
 */
public class ItemBucketFactumOpus extends ItemBucket {

	public ItemBucketFactumOpus(Block block) {
		super(block);
		this.setMaxStackSize(1);
		this.setHasSubtypes(false);
		this.setCreativeTab(FactumOpus.tab);
	}

	@Override
	public Item setUnlocalizedName(String name) {
		this.setTextureName("factumopus:" + name);
		return super.setUnlocalizedName(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean par4) {
		{
			TooltipUtil.addShiftTooltip(stack, tooltip);
		}
	}
}
