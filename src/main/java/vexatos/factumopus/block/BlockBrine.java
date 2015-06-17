package vexatos.factumopus.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import vexatos.factumopus.misc.material.MaterialBrine;

/**
 * @author Vexatos
 */
public class BlockBrine extends BlockFluidClassic {

	public static final MaterialLiquid brineMaterial = new MaterialBrine();

	public BlockBrine(Fluid fluid) {
		// TODO Resolve this
		super(fluid, Material.water);
		this.setDensity(fluid.getDensity());
		this.setBlockTextureName("factumopus:brine_still");
		this.setBlockName("factumopus.brine");
		renderType = super.getRenderType();
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
	public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int meta) {
		return super.isBlockSolid(world, x, y, z, meta);
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		return block != null
			&& !block.getMaterial().isLiquid()
			&& !(block instanceof BlockSaltLayer)
			&& super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		Block block = world.getBlock(x, y, z);
		return block != null
			&& !block.getMaterial().isLiquid()
			&& !(block instanceof BlockSaltLayer)
			&& super.displaceIfPossible(world, x, y, z);
	}

	public float getRenderDivisor() {
		return 1.5f;
	}

	protected int renderType;

	public void setRenderType(int renderType) {
		this.renderType = renderType;
	}

	@Override
	public int getRenderType() {
		return renderType;
	}

	@Override
	public MapColor getMapColor(int meta) {
		return brineMaterial.getMaterialMapColor();
	}
}
