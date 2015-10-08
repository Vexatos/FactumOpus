package vexatos.factumopus.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import vexatos.factumopus.FactumOpus;

/**
 * @author Vexatos
 */
public class BlockFactumOpus extends Block {

	public BlockFactumOpus(Material material) {
		super(material);
		this.setCreativeTab(FactumOpus.tab);
	}

	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int id, int val) {
		super.onBlockEventReceived(world, x, y, z, id, val);
		TileEntity tileentity = world.getTileEntity(x, y, z);
		return tileentity != null && tileentity.receiveClientEvent(id, val);
	}
}
