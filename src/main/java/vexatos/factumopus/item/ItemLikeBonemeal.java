package vexatos.factumopus.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Vexatos
 */
public class ItemLikeBonemeal extends ItemMultiple {

	public ItemLikeBonemeal(String... parts) {
		super(parts);
	}

	protected boolean[] bonemeal = null;

	public ItemLikeBonemeal setBonemeal(int... meta) {
		bonemeal = new boolean[parts.length];
		for(int i : meta) {
			bonemeal[i] = true;
		}
		return this;
	}

	public boolean isBonemeal(int meta) {
		return bonemeal != null && bonemeal[meta];
	}

	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par7, float par8, float par9, float par10) {
		if(this.isBonemeal(stack.getItemDamage()) && ItemDye.applyBonemeal(stack, world, x, y, z, player)) {
			if(!world.isRemote) {
				world.playAuxSFX(2005, x, y, z, 0);
			}
			return true;
		} else {
			return super.onItemUse(stack, player, world, x, y, z, par7, par8, par9, par10);
		}
	}
}
