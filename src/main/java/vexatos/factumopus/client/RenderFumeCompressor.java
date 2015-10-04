package vexatos.factumopus.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import factorization.crafting.TileEntityMixerRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import vexatos.factumopus.tile.compressor.TileFumeCompressor;

/**
 * @author Vexatos
 */
public class RenderFumeCompressor extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
	private final int renderID;

	public RenderFumeCompressor(int renderID) {
		this.renderID = renderID;
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
		TileFumeCompressor compressor = (TileFumeCompressor) tile;
		GL11.glPushMatrix();
		//GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
		int motion = compressor.getMotion();
		if(motion < 0) {
			motion += 100;
		}
		GL11.glTranslatef((float) x, (float) y + 0.5f + (motion / 100f) * 0.75f, (float) z + 1.0F);
		TileEntityMixerRenderer.renderWithRotation(360 * f);
		GL11.glPopMatrix();
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(tileEntity instanceof TileFumeCompressor) {
			return false;
		}
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return this.renderID;
	}
}
