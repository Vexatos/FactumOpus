package vexatos.factumopus.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import vexatos.factumopus.FactumOpus;

/**
 * @author Vexatos
 */
public class BlockFactumOpus extends Block {

	public BlockFactumOpus(Material material) {
		super(material);
		this.setCreativeTab(FactumOpus.tab);
	}
}
