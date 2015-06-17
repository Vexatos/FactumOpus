package vexatos.factumopus.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

/**
 * @author Vexatos
 */
public class BlockBrine extends BlockFluidFinite {

	public BlockBrine(Fluid fluid) {
		super(fluid, Material.water);
		this.setDensity(fluid.getDensity());
		this.setBlockTextureName("factumopus:brine_still");
		this.setBlockName("factumopus.brine");
	}

	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	@Override
	public IIcon getIcon(int side, int meta) {
		return side != 0 && side != 1 ? this.icons[1] : this.icons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister r) {
		icons = new IIcon[] {
			r.registerIcon("factumopus:brine_still"),
			r.registerIcon("factumopus:brine_flow")
		};
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.displaceIfPossible(world, x, y, z);
	}
}
