package vexatos.factumopus.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.tile.TileFluidVoidGoo;

/**
 * @author Vexatos
 */
public class BlockFluidVoidGoo extends BlockFluidClassic {

	public static final Material voidgooMaterial = new MaterialLiquid(MapColor.purpleColor);
	public static final DamageSource damageVoidGoo = new DamageSourceVoidGoo();

	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	public BlockFluidVoidGoo(Fluid fluid) {
		super(fluid, voidgooMaterial);
		this.setDensity(fluid.getDensity());
		this.setBlockTextureName("factumopus:void_goo_fluid");
		this.setQuantaPerBlock(5);
	}

	@Override
	public String getUnlocalizedName() {
		return "tile.factumopus.void_goo_fluid";
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side != 0 && side != 1 ? this.icons[1] : this.icons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister r) {
		icons = new IIcon[] {
			r.registerIcon("factumopus:void_goo_fluid"),
			r.registerIcon("factumopus:void_goo_fluid_flow")
		};

		FactumOpus.setFluidTextures(r);
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return metadata == 0;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		if(metadata == 0) {
			return new TileFluidVoidGoo();
		}
		return super.createTileEntity(world, metadata);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if(entity == null) {
			return;
		}

		entity.motionY = Math.min(0.0, entity.motionY);

		if(entity.motionY < -0.05) {
			entity.motionY *= 0.05;
		}

		entity.motionX = Math.max(-0.05, Math.min(0.05, entity.motionX * 0.05));
		entity.motionY -= 0.05;
		entity.motionZ = Math.max(-0.05, Math.min(0.05, entity.motionZ * 0.05));
		entity.stepHeight = 0.0F;
		if(entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if(player.capabilities != null) {
				player.capabilities.isFlying = player.capabilities.isFlying && player.capabilities.isCreativeMode;
				if(player.capabilities.isCreativeMode) {
					return;
				}
			}
			if(world.isRemote || player.isEntityInvulnerable()) {
				return;
			}
			FoodStats foodStats = player.getFoodStats();
			if(foodStats != null && world.getTotalWorldTime() % 8 == 0) {
				if(foodStats.getSaturationLevel() > 0.0F) {
					foodStats.setFoodSaturationLevel(Math.max(foodStats.getSaturationLevel() - 1.0F, 0));
					return;
				} else if(foodStats.getFoodLevel() > 1) {
					foodStats.setFoodLevel(Math.max(foodStats.getFoodLevel() - 1, 1));
					return;
				}
			}
		}
		if(!world.isRemote && entity instanceof EntityLivingBase
			&& !entity.isEntityInvulnerable()) {
			EntityLivingBase living = (EntityLivingBase) entity;
			float health = living.getHealth();
			if((health > 22F || world.getTotalWorldTime() % 8 == 0) && health > 0.0F) {
				if(health <= 1.0F) {
					entity.attackEntityFrom(damageVoidGoo, 200f);
				} else {
					living.setHealth(Math.max(health - 1.0F, 0.9f));
				}
			}
		}
	}

	@Override
	public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
		return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.canDisplace(world, x, y, z);
	}

	@Override
	public boolean displaceIfPossible(World world, int x, int y, int z) {
		return !world.getBlock(x, y, z).getMaterial().isLiquid() && super.displaceIfPossible(world, x, y, z);
	}

	public static class DamageSourceVoidGoo extends DamageSource {

		public DamageSourceVoidGoo() {
			super("factumopus.void_goo");
			this.setDamageBypassesArmor();
			this.setDamageIsAbsolute();
		}
	}
}
