package vexatos.factumopus.block.compressor;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vexatos.factumopus.block.BlockFactumOpus;
import vexatos.factumopus.tile.TilePondBase;

/**
 * @author Vexatos
 */
public class BlockCompressorWall extends BlockFactumOpus {

	protected BlockCompressorWall() {
		super(Material.iron);
		this.setHarvestLevel("pickaxe", 2);
		this.setHardness(5.0F);
		this.setResistance(15.0F);
		this.setStepSound(soundTypeMetal);
		this.setBlockTextureName("factumopus:compressor_wall");
		this.setBlockName("factumopus.compressor_wall");
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TilePondBase();
	}
}
