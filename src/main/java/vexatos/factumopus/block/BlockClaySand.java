package vexatos.factumopus.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.tile.TileClaySand;

/**
 * @author Vexatos
 */
public class BlockClaySand extends BlockFalling {

	public BlockClaySand() {
		super(Material.sand);
		this.setHarvestLevel("shovel", 1);
		this.setHardness(0.5F);
		this.setStepSound(soundTypeSand);
		this.setBlockTextureName("factumopus:clayey_sand");
		this.setBlockName("factumopus.clayey_sand");
		this.setCreativeTab(FactumOpus.tab);
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileClaySand();
	}
}
