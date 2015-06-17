package vexatos.factumopus.misc.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

/**
 * @author Vexatos
 */
public class MaterialFactumOpus extends Material {

	public MaterialFactumOpus(MapColor color) {
		super(color);
	}

	@Override
	public Material setRequiresTool() {
		return super.setRequiresTool();
	}

	@Override
	public Material setNoPushMobility() {
		return super.setNoPushMobility();
	}
}
