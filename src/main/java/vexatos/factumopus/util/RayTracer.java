package vexatos.factumopus.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * @author Vexatos
 */
public class RayTracer {

	public static MovingObjectPosition raytraceFromPlayer(World world, EntityPlayer player, boolean par3) {
		return raytraceFromPlayer(world, player, par3, 5.0D);
	}

	/**
	 * From ItemBucket, slightly modified
	 */
	public static MovingObjectPosition raytraceFromPlayer(World world, EntityPlayer player, boolean par3, double range) {
		float f = 1.0F;
		float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
		float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
		double xPos = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
		double yPos = player.prevPosY + (player.posY - player.prevPosY) * (double) f
			+ (double) (world.isRemote ?
			player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight());
		double zPos = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
		Vec3 vec3 = Vec3.createVectorHelper(xPos, yPos, zPos);
		float f3 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-pitch * 0.017453292F);
		float f6 = MathHelper.sin(-pitch * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = range;
		if(player instanceof EntityPlayerMP) {
			d3 = Math.min(d3, ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance());
		}
		Vec3 vec31 = vec3.addVector((double) f7 * d3, (double) f6 * d3, (double) f8 * d3);
		return world.func_147447_a(vec3, vec31, par3, !par3, false);
	}
}
