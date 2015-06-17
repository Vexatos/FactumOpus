package vexatos.factumopus.misc.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLogic;

/**
 * @author Vexatos
 */
public class MaterialSalt extends MaterialLogic {

	public MaterialSalt() {
		super(MapColor.snowColor);
		this.setReplaceable();
		this.setNoPushMobility();
		this.setRequiresTool();
	}

	@Override
	public boolean isOpaque() {
		return false;
	}
}
