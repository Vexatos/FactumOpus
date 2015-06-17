package vexatos.factumopus.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
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
		this.setHardness(1.2F);
		this.setStepSound(soundTypeSand);
		this.setBlockName("factumopus.salt");
		this.setBlockTextureName("factumopus:salt");
		this.setLightOpacity(0);
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
	public boolean canDropFromExplosion(Explosion p_149659_1_) {
		return false;
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return 0;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return null;
	}

	@Override
	public int quantityDropped(Random p_149745_1_) {
		return 0;
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		return new ArrayList<ItemStack>();
	}
}
