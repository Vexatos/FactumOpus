package vexatos.factumopus.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import vexatos.factumopus.FactumOpus;
import vexatos.factumopus.misc.material.MaterialFactumOpus;

import java.util.Random;

/**
 * @author Vexatos
 */
public class BlockSolidVoidGoo extends BlockFactumOpus {

	public static final Material voidgooSolidMaterial = new MaterialFactumOpus(MapColor.purpleColor).setRequiresTool();

	public BlockSolidVoidGoo() {
		super(voidgooSolidMaterial);
		this.setHarvestLevel("shovel", 2);
		this.setResistance(2000.0F);
		this.setHardness(12f);
		this.setStepSound(slimeySound);
		this.setBlockTextureName("factumopus:void_goo");
		this.setBlockName("factumopus.void_goo_solid");
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return FactumOpus.itemMaterial;
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return 0;
	}

	@Override
	public int quantityDropped(Random rand) {
		return 12;
	}

	@Override
	public int quantityDroppedWithBonus(int meta, Random rand) {
		return quantityDropped(rand) + rand.nextInt(5);
	}

	@Override
	protected boolean canSilkHarvest() {
		return true;
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
		return true;
	}

	protected static final SlimeySound slimeySound = new SlimeySound();

	public static class SlimeySound extends SoundType {

		public SlimeySound() {
			super("mob.slime", 1.0f, 1.0f);
		}

		@Override
		public String getBreakSound() {
			return this.soundName + ".big";
		}

		@Override
		public String getStepResourcePath() {
			return this.soundName + ".small";
		}
	}
}
