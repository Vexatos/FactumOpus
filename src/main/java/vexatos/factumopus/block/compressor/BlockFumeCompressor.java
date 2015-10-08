package vexatos.factumopus.block.compressor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vexatos.factumopus.tile.compressor.TileFumeCompressor;

/**
 * @author Vexatos
 */
public class BlockFumeCompressor extends BlockCompressorWall {

	public BlockFumeCompressor() {
		super();
		this.setBlockName("factumopus.compressor");
		this.setTopTextureName("factumopus:compressor_top");
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileFumeCompressor();
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side) {
		return side != 1;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return isBlockSolid(world, x, y, z, side.ordinal());
	}

	@SideOnly(Side.CLIENT)
	public IIcon wireIcon;

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister r) {
		super.registerBlockIcons(r);
		textureBottom = r.registerIcon("factumopus:compressor_valve_top");
		wireIcon = r.registerIcon("factorization:charge/wire");
	}

	protected int renderType;

	public void setRenderType(int renderType) {
		this.renderType = renderType;
	}

	@Override
	public int getRenderType() {
		return renderType;
	}
}
