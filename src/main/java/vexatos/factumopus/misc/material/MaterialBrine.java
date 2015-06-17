package vexatos.factumopus.misc.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLiquid;

/**
 * @author Vexatos
 */
public class MaterialBrine extends MaterialLiquid {

	public MaterialBrine() {
		super(MapColor.silverColor);
		this.setNoPushMobility();
	}
}
