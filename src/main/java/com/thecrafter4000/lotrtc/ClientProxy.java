package com.thecrafter4000.lotrtc;

import com.thecrafter4000.lotrtc.manual.ManualContentRegistry;
import com.thecrafter4000.lotrtc.manual.ManualRegistry;
import com.thecrafter4000.lotrtc.smeltery.FactionSmelteryRender;
import com.thecrafter4000.lotrtc.tools.ToolRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.client.MinecraftForgeClient;
import tconstruct.client.FlexibleToolRenderer;
import tconstruct.library.tools.ToolCore;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		ManualRegistry.preInitClient();
		super.preInit(e);
	}
	
	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		RenderingRegistry.registerBlockHandler(new FactionSmelteryRender());
		ManualRegistry.initClient();
		ManualContentRegistry.init();
		
		/* Register ItemRenderer */
		FlexibleToolRenderer r = new FlexibleToolRenderer();
		for (ToolCore key : ToolRegistry.tools.keySet()) {
			MinecraftForgeClient.registerItemRenderer(key, r);
		}
	}
}
