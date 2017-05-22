package com.thecrafter4000.lotrtc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.SidedProxy;
import tconstruct.smeltery.blocks.SmelteryBlock;

@Mod(version=LotRTCIntegrator.VERSION, name=LotRTCIntegrator.NAME, modid=LotRTCIntegrator.MODID)
public class LotRTCIntegrator {

	public static final String MODID = "lotrtc";
	public static final String VERSION = "lotrtc";
	public static final String NAME = "LotRTCIntegrator";
	@Instance public static LotRTCIntegrator instance;
	
	@SidedProxy(clientSide="com.thecrafter4000.lotrtc.ClientProxy", serverSide="com.thecrafter4000.lotrtc.ServerProxy")
	public static CommonProxy proxy;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent e) {
		proxy.preInit(e);
    }

	@EventHandler
    public void init(FMLInitializationEvent e) {
		proxy.init(e);
    }

	@EventHandler
    public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
    }

}