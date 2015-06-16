package vexatos.factumopus.item;

import factorization.shared.Sound;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

/**
 * @author Vexatos
 */
public class ItemAcidBottles extends ItemMultiple {

	public ItemAcidBottles() {
		super("nitric_acid", "hydrochloric_acid", "aqua_regia_vera");
		this.setMaxStackSize(16);
	}

	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	public void registerIcons(IIconRegister r) {
	}

	public IIcon getIconFromDamageForRenderPass(int damage, int renderPass) {
		return Items.potionitem.getIconFromDamageForRenderPass(damage, renderPass);
	}

	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		if(renderPass == 0) {
			switch(stack.getItemDamage()) {
				case 0: {
					return 0xAFFCFF;
				}
				case 1: {
					return 0xE2B7FF;
				}
				case 2: {
					return 0xFFD800;
				}
			}
		}
		return super.getColorFromItemStack(stack, renderPass);
	}

	private float getDamageFromItemStack(ItemStack stack) {
		switch(stack.getItemDamage()) {
			case 0: {
				return 15.0F;
			}
			case 1: {
				return 15.0F;
			}
			case 2: {
				return 20.0F;
			}
		}
		return 0;
	}

	// Taken from Factorization because I cannot extend ItemAcidBottle

	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.drink;
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		return stack;
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
		--stack.stackSize;
		Sound.acidBurn.playAt(world, player.posX, player.posY, player.posZ);
		if(world.isRemote) {
			return stack;
		} else {
			player.attackEntityFrom(acidDrinker, this.getDamageFromItemStack(stack));
			player.getFoodStats().addStats(-20, 0.0F);
			return stack;
		}
	}

	public static DamageSource acidDrinker = new AcidDamage();

	public static class AcidDamage extends DamageSource {

		protected AcidDamage() {
			super("factumopus.acidDrinker");
			this.setDamageBypassesArmor();
			this.setDamageIsAbsolute();
		}
	}
}
