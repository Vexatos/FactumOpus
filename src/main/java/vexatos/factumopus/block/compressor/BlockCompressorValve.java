package vexatos.factumopus.block.compressor;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vexatos.factumopus.tile.compressor.TileCompressorValve;

/**
 * @author Vexatos
 */
public class BlockCompressorValve extends BlockCompressorWall {
	public BlockCompressorValve() {
		super();
		this.setBlockTextureName("factumopus:compressor_valve");
		this.setBlockName("factumopus.compressor_valve");
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileCompressorValve();
	}
}
