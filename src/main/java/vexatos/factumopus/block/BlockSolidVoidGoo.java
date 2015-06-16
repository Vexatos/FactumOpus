package vexatos.factumopus.block;

import net.minecraft.item.Item;
import vexatos.factumopus.FactumOpus;

import java.util.Random;

/**
 * @author Vexatos
 */
public class BlockSolidVoidGoo extends BlockFactumOpus {

	public BlockSolidVoidGoo() {
		super(BlockFluidVoidGoo.voidgooMaterial);
		this.setHarvestLevel("shovel", 2);
		this.setResistance(2000.0F);
		this.setHardness(12f);
		this.setStepSound(slimeySound);
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
	public int quantityDropped(Random p_149745_1_) {
		return 4;
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
