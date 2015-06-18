package vexatos.factumopus.misc.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;

/**
 * @author Vexatos
 */
public class MaterialSolidLiquid extends MaterialLiquid {

	public MaterialSolidLiquid(MapColor color) {
		super(color);
		this.setNoPushMobility();
	}

	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean blocksMovement() {
		return true;
	}

	@Override
	public boolean isOpaque() {
		return false;
	}
}
