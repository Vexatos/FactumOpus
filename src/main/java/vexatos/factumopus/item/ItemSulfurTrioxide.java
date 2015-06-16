package vexatos.factumopus.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Arrays;
import java.util.List;

/**
 * @author Vexatos
 */
public class ItemSulfurTrioxide extends ItemFactumOpus {

	public ItemSulfurTrioxide() {
		this.setTextureName("factumopus:sulfur_trioxide");
		this.setUnlocalizedName("factumopus.sulfur_trioxide");
		this.setHasSubtypes(false);
		this.setMaxDamage(0);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return super.hasCustomEntity(stack);
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return super.createEntity(world, location, itemstack);
	}

	@Override
	public boolean onEntityItemUpdate(EntityItem entity) {
		super.onEntityItemUpdate(entity);
		if(!entity.worldObj.isRemote && shouldExplode(entity)) {
			entity.setDead();
			entity.worldObj.createExplosion(entity, entity.posX, entity.posY, entity.posZ, 3F, true);
			return true;
		}
		return false;
	}

	private static final List<String> waterTypes = Arrays.asList("water", "saltwater", "saltWater", "Saltwater", "SaltWater");

	private static boolean shouldExplode(EntityItem entity) {
		int minX = MathHelper.floor_double(entity.boundingBox.minX + 0.001D);
		int minY = MathHelper.floor_double(entity.boundingBox.minY + 0.001D);
		int minZ = MathHelper.floor_double(entity.boundingBox.minZ + 0.001D);
		int maxX = MathHelper.floor_double(entity.boundingBox.maxX - 0.001D);
		int maxY = MathHelper.floor_double(entity.boundingBox.maxY - 0.001D);
		int maxZ = MathHelper.floor_double(entity.boundingBox.maxZ - 0.001D);

		if(entity.worldObj.checkChunksExist(minX, minY, minZ, maxX, maxY, maxZ)) {
			for(int x = minX; x <= maxX; ++x) {
				for(int y = minY; y <= maxY; ++y) {
					for(int z = minZ; z <= maxZ; ++z) {
						Block block = entity.worldObj.getBlock(x, y, z);
						if(block != null) {
							Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
							if(fluid != null && FluidRegistry.isFluidRegistered(fluid)) {
								if(waterTypes.contains(fluid.getName())) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
}