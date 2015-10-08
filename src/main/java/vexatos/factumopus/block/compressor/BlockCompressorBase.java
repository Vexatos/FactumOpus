package vexatos.factumopus.block.compressor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vexatos.factumopus.block.BlockFactumOpus;

/**
 * @author Vexatos
 */
public abstract class BlockCompressorBase extends BlockFactumOpus {

	public BlockCompressorBase() {
		super(Material.iron);
		this.setHarvestLevel("pickaxe", 2);
		this.setHardness(5.0F);
		this.setResistance(15.0F);
		this.setStepSound(soundTypeMetal);
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	public abstract TileEntity createTileEntity(World world, int metadata);

	@SideOnly(Side.CLIENT)
	protected IIcon textureTop = null;
	@SideOnly(Side.CLIENT)
	protected IIcon textureBottom = null;
	protected String textureTopName;

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		if(textureTop != null) {
			if(side == 0) {
				return textureBottom;
			} else if(side == 1) {
				return textureTop;
			}
		}
		return super.getIcon(side, meta);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister r) {
		super.registerBlockIcons(r);
		textureTop = r.registerIcon(textureTopName);
		textureBottom = textureTop;
	}

	protected void setTopTextureName(String textureName) {
		this.textureTopName = textureName;
	}

	@Override
	public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
		return false;
	}
}
