package vexatos.factumopus.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;

/**
 * @author Vexatos
 */
public class BlockBrineSmall extends BlockBrine {

	public BlockBrineSmall(Fluid fluid) {
		super(fluid);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side != 0 && side != 1 ? this.icons[1] : this.icons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister r) {
		icons = new IIcon[] {
			r.registerIcon("factumopus:brine_small_still"),
			r.registerIcon("factumopus:brine_small_flow")
		};
	}

	@Override
	public int getMaxRenderHeightMeta() {
		return 4;
	}
}
