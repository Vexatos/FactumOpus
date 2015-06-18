package vexatos.factumopus.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
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
		Block otherBlock = world.getBlock(x, y, z);
		if(otherBlock instanceof BlockLiquid && otherBlock.getMaterial() == Material.water) {
			return getLiquidHeight(world, x, y, z, Material.water);
		}
		if(height >= 1.0F || !(block instanceof BlockBrine)) {
			return height;
		}
		if(otherBlock instanceof BlockBrine) {
			return height / ((BlockBrine) otherBlock).getRenderDivisor();
		}
		return height / ((BlockBrine) block).getRenderDivisor();
	}

	// Copied from RenderBlocks.getLiquidHeight
	public static float getLiquidHeight(IBlockAccess world, int x, int y, int z, Material material) {
		int l = 0;
		float f = 0.0F;

		for(int i1 = 0; i1 < 4; ++i1) {
			int j1 = x - (i1 & 1);
			int k1 = z - (i1 >> 1 & 1);

			if(world.getBlock(j1, y + 1, k1).getMaterial() == material) {
				return 1.0F;
			}

			Material material1 = world.getBlock(j1, y, k1).getMaterial();

			if(material1 == material) {
				int l1 = world.getBlockMetadata(j1, y, k1);

				if(l1 >= 8 || l1 == 0) {
					f += BlockLiquid.getLiquidHeightPercent(l1) * 10.0F;
					l += 10;
				}

				f += BlockLiquid.getLiquidHeightPercent(l1);
				++l;
			} else if(!material1.isSolid()) {
				++f;
				++l;
			}
		}

		return 1.0F - f / (float) l;
	}

	@Override
	public int getRenderId() {
		return this.renderType;
	}
}
