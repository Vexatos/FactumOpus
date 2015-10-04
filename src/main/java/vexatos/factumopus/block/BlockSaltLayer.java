package vexatos.factumopus.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.misc.material.MaterialSalt;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Vexatos
 */
public class BlockSaltLayer extends Block {

	public static final MaterialSalt materialSalt = new MaterialSalt();

	public BlockSaltLayer() {
		super(materialSalt);
		this.setHardness(1.0F);
		this.setStepSound(soundTypeGravel);
		this.setBlockName("factumopus.salt");
		this.setBlockTextureName("factumopus:salt");
		this.setLightOpacity(3);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(!player.isSneaking()) {
			ItemStack stack = player.getCurrentEquippedItem();
			if(stack != null && stack.getItem() != null && stack.getItem() == Items.bowl) {
				ItemStack newStack = new ItemStack(FactumOpus.itemBowls, 1, 2);

				--stack.stackSize;
				if(stack.stackSize == 0) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}

				if(!player.inventory.addItemStackToInventory(newStack)) {
					player.dropPlayerItemWithRandomChoice(newStack, true);
				}

				player.swingItem();
				if(!world.isRemote) {
					world.setBlockToAir(x, y, z);
				}
				return true;
			}
		}
		return super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return AxisAlignedBB.getBoundingBox(
			(double) x + this.minX,
			(double) y + this.minY,
			(double) z + this.minZ,
			(double) x + this.maxX,
			(double) ((float) y + 0.125f),
			(double) z + this.maxZ);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public void setBlockBoundsForItemRender() {
		this.setBlockBounds(0);
	}

	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		this.setBlockBounds(world.getBlockMetadata(x, y, z));
	}

	protected void setBlockBounds(int meta) {
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125f, 1.0F);
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
		return false;
	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion) {
		return false;
	}

	@Override
	public int damageDropped(int meta) {
		return 0;
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		return null;
	}

	@Override
	public int quantityDropped(Random rand) {
		return 0;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		return new ArrayList<ItemStack>();
	}
}
