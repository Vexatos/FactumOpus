package vexatos.factumopus.block.compressor;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vexatos.factumopus.tile.compressor.TileFumeCompressor;

/**
 * @author Vexatos
 */
public class BlockFumeCompressor extends BlockCompressorWall {

	public BlockFumeCompressor() {
		super();
		this.setBlockName("factumopus.compressor");
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileFumeCompressor();
	}
}
