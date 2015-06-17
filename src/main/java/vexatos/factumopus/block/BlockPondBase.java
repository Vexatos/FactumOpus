package vexatos.factumopus.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vexatos.factumopus.tile.TilePondBase;

/**
 * @author Vexatos
 */
public class BlockPondBase extends BlockFactumOpus {

	public BlockPondBase() {
		super(Material.sand);
		this.setHarvestLevel("shovel", 2);
		this.setHardness(2F);
		this.setStepSound(soundTypeSand);
		this.setBlockTextureName("factumopus:hardened_sand");
		this.setBlockName("factumopus.pond_base");
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
