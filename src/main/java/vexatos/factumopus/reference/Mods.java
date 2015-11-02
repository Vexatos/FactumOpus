package vexatos.factumopus.reference;

import com.google.common.collect.Iterables;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;

import java.util.HashMap;

/**
 * @author Vexatos
 */
public class Mods {

	//The mod itself
	public static final String
		FactumOpus = "FactumOpus",
		FactumOpus_NAME = "Factum Opus";

	public static final String
		Botany = "Botany",
		BuildCraftCore = "BuildCraft|Core",
		BuildCraftTransport = "BuildCraft|Transport",
		ExtraBees = "ExtraBees",
		Factorization = "factorization",
		Forestry = "Forestry",
		Mystcraft = "Mystcraft",
		Railcraft = "Railcraft",
		RFTools = "rftools";

	//Other APIs
	public static class API {

		public static final String
			BuildCraftTransport = "BuildCraftAPI|transport",
			Galacticraft = "Galacticraft API";

		public static boolean hasAPI(String name) {
			return ModAPIManager.INSTANCE.hasAPI(name);
		}
	}

	public static boolean isLoaded(String name) {
		return Loader.isModLoaded(name);
	}

	// Mod versions

	private static HashMap<String, ArtifactVersion> modVersionList;

	public static ArtifactVersion getVersion(String name) {
		if(modVersionList == null) {
			modVersionList = new HashMap<String, ArtifactVersion>();

			for(ModContainer api : Iterables.concat(Loader.instance().getActiveModList(), ModAPIManager.INSTANCE.getAPIList())) {
				modVersionList.put(api.getModId(), api.getProcessedVersion());
			}
		}

		if(modVersionList.containsKey(name)) {
			return modVersionList.get(name);
		}
		throw new IllegalArgumentException("Mod/API '" + name + "' does not exist!");
	}

	public static boolean hasVersion(String name, String version) {
		if(isLoaded(name) || API.hasAPI(name)) {
			ArtifactVersion v1 = VersionParser.parseVersionReference(name + "@" + version);
			ArtifactVersion v2 = getVersion(name);
			return v1.containsVersion(v2);
		}
		return false;
	}
}
