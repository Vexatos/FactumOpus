package vexatos.factumopus.block;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import vexatos.factumopus.FactumOpus;

/**
 * @author Vexatos
 */
public class BlockFluidLikeWater extends BlockFluidClassic {

	public BlockFluidLikeWater(Fluid fluid, Material material) {
		super(fluid, material);
	}

	protected static String waterHandlingMethod = null;

	static {
		String methodname;
		try {
			waterHandlingMethod = ReflectionHelper.findMethod(World.class, null, new String[] { "handleMaterialAcceleration", "func_72918_a" },
				AxisAlignedBB.class, Material.class, Entity.class).getName();
		} catch(Exception ue) {
			waterHandlingMethod = null;
		} finally {
			if(waterHandlingMethod == null) {
				try {
					methodname = "func_72918_a";
					World.class.getDeclaredMethod(methodname, AxisAlignedBB.class, Material.class, Entity.class);
					waterHandlingMethod = methodname;
				} catch(NoSuchMethodException e) {
					FactumOpus.log.info("[Water Physics] Deobfuscated Environment detected");
					try {
						methodname = "handleMaterialAcceleration";
						World.class.getDeclaredMethod(methodname, AxisAlignedBB.class, Material.class, Entity.class);
						waterHandlingMethod = methodname;
					} catch(NoSuchMethodException e2) {
						FactumOpus.log.error("[Water Physics] Could not find water physics method. Brine will not have water physics!");
						waterHandlingMethod = null;
					}
				}
			}
		}
	}

	@Override
	public Material getMaterial() {
		// Giant hack to make water physics work
		StackTraceElement[] stackTrace = new Exception().getStackTrace();
		int checklength = Math.min(stackTrace.length, 5);
		for(int i = 0; i < checklength; ++i) {
			if(stackTrace[i] != null && stackTrace[i].getMethodName().equals(waterHandlingMethod)) {
				return Material.water;
			}
		}
		return super.getMaterial();
	}

	// Copied from superclass and slightly modified
	@Override
	public Vec3 getFlowVector(IBlockAccess world, int x, int y, int z) {
		Vec3 vec = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
		int decay = quantaPerBlock - getQuantaValue(world, x, y, z);

		for(int side = 0; side < 4; ++side) {
			int x2 = x;
			int z2 = z;

			switch(side) {
				case 0:
					--x2;
					break;
				case 1:
					--z2;
					break;
				case 2:
					++x2;
					break;
				case 3:
					++z2;
					break;
			}

			int otherDecay = quantaPerBlock - getQuantaValue(world, x2, y, z2);
			if(otherDecay >= quantaPerBlock) {
				Block block = world.getBlock(x2, y, z2);
				Material material = block.getMaterial();
				if(shouldFlowInto(block, material)) {
					otherDecay = quantaPerBlock - getQuantaValue(world, x2, y - 1, z2);
					if(otherDecay >= 0) {
						int power = otherDecay - (decay - quantaPerBlock);
						vec = vec.addVector((x2 - x) * power, 0, (z2 - z) * power);
					}
				}
			} else if(otherDecay >= 0) {
				int power = otherDecay - decay;
				vec = vec.addVector((x2 - x) * power, 0, (z2 - z) * power);
			}
		}

		if(world.getBlock(x, y + 1, z) == this) {
			boolean flag =
				isBlockSolid(world, x, y, z - 1, 2) ||
					isBlockSolid(world, x, y, z + 1, 3) ||
					isBlockSolid(world, x - 1, y, z, 4) ||
					isBlockSolid(world, x + 1, y, z, 5) ||
					isBlockSolid(world, x, y + 1, z - 1, 2) ||
					isBlockSolid(world, x, y + 1, z + 1, 3) ||
					isBlockSolid(world, x - 1, y + 1, z, 4) ||
					isBlockSolid(world, x + 1, y + 1, z, 5);

			if(flag) {
				vec = vec.normalize().addVector(0.0D, -6.0D, 0.0D);
			}
		}
		vec = vec.normalize();
		return vec;
	}

	public boolean shouldFlowInto(Block block, Material material) {
		return !material.blocksMovement() && !material.isLiquid();
	}
}
