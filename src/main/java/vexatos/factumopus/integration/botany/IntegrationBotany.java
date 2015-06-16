package vexatos.factumopus.integration.botany;

import binnie.botany.gardening.Gardening;
import net.minecraft.item.ItemStack;
import vexatos.factumopus.FactumOpus;

import java.util.Map;

/**
 * @author Vexatos
 */
public class IntegrationBotany {

	public void init() {
		FactumOpus.log.info("Adding Botany integration...");
		try {
			addSaltsFeriliser(Gardening.fertiliserAcid, 3, 2);
			addSaltsFeriliser(Gardening.fertiliserNutrient, 4, 2);
			addSaltsFeriliser(Gardening.fertiliserAcid, 8, 1);
			addSaltsFeriliser(Gardening.fertiliserNutrient, 9, 1);
			FactumOpus.log.info("Successfully added Botany integration");
		} catch(Exception e) {
			FactumOpus.log.error("Unable to add Botany integration", e);
		}
	}

	private void addSaltsFeriliser(Map<ItemStack, Integer> map, int metadata, int strength) {
		try {
			map.put(new ItemStack(FactumOpus.itemSalts, 1, metadata), strength);
		} catch(Exception e) {
			FactumOpus.log.error("Unable to add part of Botany integration", e);
		}
	}
}
