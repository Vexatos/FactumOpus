package vexatos.factumopus.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.RenderBlockFluid;
import vexatos.factumopus.block.BlockBrine;

/**
 * @author Vexatos
 */
@SideOnly(Side.CLIENT)
public class RenderBlockBrine extends RenderBlockFluid {

	public int renderType;

	public RenderBlockBrine(int renderType) {
		this.renderType = renderType;
	}

	@Override
	public float getFluidHeightForRender(IBlockAccess world, int x, int y, int z, BlockFluidBase block) {
		float height = super.getFluidHeightForRender(world, x, y, z, block);
		if(height >= 1.0F || !(block instanceof BlockBrine)) {
			return height;
		}
		return height / ((BlockBrine) block).getRenderDivisor();
	}

	@Override
	public int getRenderId() {
		return this.renderType;
	}
}
