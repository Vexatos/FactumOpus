package vexatos.factumopus.block.compressor;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vexatos.factumopus.tile.compressor.TileCompressorWall;

/**
 * @author Vexatos
 */
public class BlockCompressorWall extends BlockCompressorBase {

	public BlockCompressorWall() {
		super();
		this.setBlockTextureName("factumopus:compressor_wall");
		this.setTopTextureName("factumopus:compressor_wall_top");
		this.setBlockName("factumopus.compressor_wall");
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileCompressorWall();
	}
}
