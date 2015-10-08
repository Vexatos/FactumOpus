package vexatos.factumopus.client;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.block.BlockFactumOpus;
import vexatos.factumopus.client.model.CompressorPumpModel;
import vexatos.factumopus.tile.compressor.TileFumeCompressor;

/**
 * @author Vexatos
 */
public class RenderFumeCompressor extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {
	private final int renderID;
	private static final Block renderBlock = new BlockFactumOpus(Material.iron);
	private static final double zfight = 0.0025D;

	public RenderFumeCompressor(int renderID) {
		this.renderID = renderID;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks rb) {
		GL11.glPushMatrix();

		block = FactumOpus.blockFumeCompressor;
		rb.setRenderBounds(0F, 0F, 0F, 1F, 1F - zfight, 1F);
		Tessellator tessellator = Tessellator.instance;
		tessellator.addTranslation(-0.5F, -0.5F, -0.5F);

		tessellator.startDrawingQuads();

		//Frame
		tessellator.setNormal(0.0F, -1.0F, 0.0F);
		rb.renderFaceYNeg(block, 0.0F, 0.0F, 0.0F, block.getIcon(0, 0));
		tessellator.setNormal(0.0F, 1.0F, 0F);
		rb.renderFaceYPos(block, 0F, 0F, 0F, block.getIcon(1, 0));
		tessellator.setNormal(0F, 0F, -1F);
		rb.renderFaceZNeg(block, 0F, 0F, 0F, block.getIcon(2, 0));
		tessellator.setNormal(0F, 0F, 1F);
		rb.renderFaceZPos(block, 0F, 0F, 0F, block.getIcon(3, 0));
		tessellator.setNormal(-1F, 0F, 0F);
		rb.renderFaceXNeg(block, 0F, 0F, 0F, block.getIcon(4, 0));
		tessellator.setNormal(1F, 0F, 0F);
		rb.renderFaceXPos(block, 0F, 0F, 0F, block.getIcon(5, 0));

		//Frame inside
		float d = 2 / 16F;
		IIcon side = FactumOpus.blockFumeCompressor.getIcon(2, 0);
		IIcon bottom = FactumOpus.blockFumeCompressor.getIcon(0, 0);
		rb.setRenderBounds(d, d, d, 1F - d + 2F * zfight, 1F, 1F - d + 2F * zfight);

		tessellator.setNormal(0F, 0F, 0F);
		rb.renderFaceXNeg(block, 0F + 1F - 2F * d + zfight, 0F, 0F - zfight, side);
		rb.renderFaceZNeg(block, 0F - zfight, 0F, 0F + 1F - 2F * d + zfight, side);
		rb.renderFaceXPos(block, 0F - 1F + 2F * d - zfight - zfight, 0F, 0F - zfight, side);
		rb.renderFaceZPos(block, 0F - zfight, 0F, 0F - 1F + 2F * d - zfight - zfight, side);
		rb.renderFaceYPos(block, 0F - zfight, 0F - 1F + 2F * d, 0F - zfight, bottom);

		//Frame wire
		block = renderBlock;
		IIcon wireIcon = FactumOpus.blockFumeCompressor.wireIcon;
		float f = 1 / 16F;

		block.setBlockBounds(0, 0, 0, f, 1F - d, f);
		rb.setRenderBounds(-zfight, -zfight, -zfight, f + zfight, (1F - d) / ((1 + zfight) * (1 + zfight)), f + zfight);

		float xMax = 1F - d - f;
		float zMax = 1F - d - f;

		rb.renderFaceXPos(block, d, d, d, wireIcon);
		rb.renderFaceZPos(block, d, d, d, wireIcon);
		rb.renderFaceYPos(block, d, d, d, wireIcon);

		rb.renderFaceXPos(block, d, d, zMax, wireIcon);
		rb.renderFaceZNeg(block, d, d, zMax, wireIcon);
		rb.renderFaceYPos(block, d, d, zMax, wireIcon);

		rb.renderFaceXNeg(block, xMax, d, d, wireIcon);
		rb.renderFaceZPos(block, xMax, d, d, wireIcon);
		rb.renderFaceYPos(block, xMax, d, d, wireIcon);

		rb.renderFaceXNeg(block, xMax, d, zMax, wireIcon);
		rb.renderFaceZNeg(block, xMax, d, zMax, wireIcon);
		rb.renderFaceYPos(block, xMax, d, zMax, wireIcon);

		tessellator.draw();
		tessellator.addTranslation(0.5F, 0.5F, 0.5F);

		//GL11.glEnable(GL11.GL_CULL_FACE);
		//GL11.glPopAttrib();

		//Pump
		GL11.glTranslatef(-8 / 16F, 4 / 16F, -3 / 16F);
		GL11.glRotated(180, 1, 0, 0);
		GL11.glTranslatef(0, -3 / 16F, -6 / 16F);
		Minecraft mc = Minecraft.getMinecraft();
		if(mc != null && mc.renderEngine != null) {
			mc.renderEngine.bindTexture(pumpTexture);
			pump.Main.render(factor);
			pump.Center.render(factor);
		}
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block myblock, int modelId, RenderBlocks rb) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(!(tileEntity instanceof TileFumeCompressor)) {
			return false;
		}
		//zfight = 0.005000114440918;
		//double zfight2 = (1 / 4096D);
		IIcon side = FactumOpus.blockFumeCompressor.getIcon(2, 0);
		IIcon bottom = FactumOpus.blockFumeCompressor.getIcon(0, 0);

		Block block = FactumOpus.blockFumeCompressor;
		rb.setRenderBounds(0F, 0F, 0F, 1F, 1F - zfight, 1F);
		rb.renderStandardBlock(block, x, y, z);
		float d = 2 / 16F;
		boolean origAO = rb.enableAO;
		rb.setRenderBounds(d, d, d, 1F - d + 2F * zfight, 1F, 1F - d + 2F * zfight);
		rb.enableAO = false;
		rb.renderFaceXNeg(block, x + 1F - 2F * d + zfight, y, z - zfight, side);
		rb.renderFaceZNeg(block, x - zfight, y, z + 1F - 2F * d + zfight, side);
		rb.renderFaceXPos(block, x - 1F + 2F * d - zfight - zfight, y, z - zfight, side);
		rb.renderFaceZPos(block, x - zfight, y, z - 1F + 2F * d - zfight - zfight, side);
		rb.renderFaceYPos(block, x - zfight, y - 1F + 2F * d, z - zfight, bottom);

		block = renderBlock;
		IIcon wireIcon = FactumOpus.blockFumeCompressor.wireIcon;
		float f = 1 / 16F;

		block.setBlockBounds(0, 0, 0, f, 1F - d, f);
		rb.setRenderBounds(-zfight, -zfight, -zfight, f + zfight, (1F - d) / ((1 + zfight) * (1 + zfight)), f + zfight);

		float xMin = x + d;
		float xMax = x + 1f - d - f;
		float zMin = z + d;
		float zMax = z + 1f - d - f;
		float yMin = y + d;

		rb.renderFaceXPos(block, xMin, yMin, zMin, wireIcon);
		rb.renderFaceZPos(block, xMin, yMin, zMin, wireIcon);
		rb.renderFaceYPos(block, xMin, yMin, zMin, wireIcon);

		rb.renderFaceXPos(block, xMin, yMin, zMax, wireIcon);
		rb.renderFaceZNeg(block, xMin, yMin, zMax, wireIcon);
		rb.renderFaceYPos(block, xMin, yMin, zMax, wireIcon);

		rb.renderFaceXNeg(block, xMax, yMin, zMin, wireIcon);
		rb.renderFaceZPos(block, xMax, yMin, zMin, wireIcon);
		rb.renderFaceYPos(block, xMax, yMin, zMin, wireIcon);

		rb.renderFaceXNeg(block, xMax, yMin, zMax, wireIcon);
		rb.renderFaceZNeg(block, xMax, yMin, zMax, wireIcon);
		rb.renderFaceYPos(block, xMax, yMin, zMax, wireIcon);

		rb.enableAO = origAO;
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return this.renderID;
	}

	private final CompressorPumpModel pump = new CompressorPumpModel();
	private final ResourceLocation pumpTexture = new ResourceLocation("factumopus:textures/blocks/compressor_pump.png");
	private static final float factor = 1 / 16F;

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float f) {
		TileFumeCompressor compressor = (TileFumeCompressor) tile;
		//GL11.glTranslatef((float) x, (float) y + 1F, (float) z + 1F);
		int motion = compressor.getMotion();
		boolean downwards = motion < 100;
		if(motion < 0) {
			motion += 100;
			downwards = false;
		}

		GL11.glPushMatrix();

		GL11.glTranslatef((float) x, (float) y + (4 / 16f) + (motion / 100f) * (8 / 16f), (float) z + (5 / 16F));
		GL11.glRotated(180, 1, 0, 0);
		GL11.glTranslatef(0, -3 / 16F, -6 / 16F);
		Minecraft mc = Minecraft.getMinecraft();
		if(mc != null && mc.renderEngine != null) {
			mc.renderEngine.bindTexture(pumpTexture);
			pump.Main.render(factor);
			pump.Center.render(factor);
			if(downwards) {
				pump.Closed.render(factor);
			}
		}

		GL11.glPopMatrix();
	}
}
