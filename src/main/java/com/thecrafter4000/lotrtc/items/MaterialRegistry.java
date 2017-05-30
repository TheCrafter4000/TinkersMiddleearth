package com.thecrafter4000.lotrtc.items;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;

import com.thecrafter4000.lotrtc.TinkersMiddleearth;
import com.thecrafter4000.lotrtc.TinkersMEConfig;
import com.thecrafter4000.lotrtc.TinkersMEConfig.LotRMaterialID;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import tconstruct.library.tools.ToolMaterial;

public class MaterialRegistry {

	public static final int MAPCAPAZITY = 10;
	
	public static Map<Integer, Fluid>  mapfluids = new HashMap<Integer, Fluid>(MAPCAPAZITY);
	public static Map<Integer, String> mapIdName = new HashMap<Integer, String>(MAPCAPAZITY);
	public static Map<String, Integer> mapNameId = new HashMap<String, Integer>(MAPCAPAZITY);
	public static Map<Integer, ToolMaterial> mapTool = new HashMap<Integer, ToolMaterial>(MAPCAPAZITY);
	
	private MaterialRegistry() {}
	
	public static final void setup(){
		registerMaterial(LotRMaterialID.MithrilLotR, "MithrilLotR", TinkersMEBlocks.moltenMithrilFluid);
		registerMaterial(LotRMaterialID.DwarvenSteel, "DwarvenSteel", TinkersMEBlocks.moltenDwarvenSteelFluid);
		registerMaterial(LotRMaterialID.BlueDwarvenSteel, "BlueDwarvenSteel", TinkersMEBlocks.moltenBlueDwarvenSteelFluid);
		registerMaterial(LotRMaterialID.ElvenSteel, "ElvenSteel", TinkersMEBlocks.moltenElvenSteelFluid);
		registerMaterial(LotRMaterialID.OrcSteel, "OrcSteel", TinkersMEBlocks.moltenOrcSteelFluid);
		registerMaterial(LotRMaterialID.UrukSteel, "UrukSteel", TinkersMEBlocks.moltenUrukSteelFluid);
		registerMaterial(LotRMaterialID.BlackUrukSteel, "BlackUrukSteel", TinkersMEBlocks.moltenBlackUrukSteelFluid);
//		registerMaterial(LotRMaterialID.Mallorn, "Mallorn");
	}
	
	private static void registerMaterial(int materialid, String materialName, Fluid fluid, ToolMaterial material){
		mapIdName.put(materialid, materialName);
		mapNameId.put(materialName, materialid);
		mapfluids.put(materialid, fluid);
		mapTool.put(materialid, material);
	}
	
	private static void registerMaterial(int materialid, String materialName, Fluid fluid){
		registerMaterial(materialid, materialName, fluid, TinkersMEConfig.getToolMaterial(materialName));
	}	
	
	private static void registerMaterial(int materialid, String materialName, ToolMaterial material){
		registerMaterial(materialid, materialName, null, material);
	}
	
	private static void registerMaterial(int materialid, String materialName){
		registerMaterial(materialid, materialName, (Fluid)null);
	}
}
