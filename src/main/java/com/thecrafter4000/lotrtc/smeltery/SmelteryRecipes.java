package com.thecrafter4000.lotrtc.smeltery;

import com.thecrafter4000.lotrtc.LotRTCConfig;
import com.thecrafter4000.lotrtc.ModBlocks;

import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.Smeltery;
import tconstruct.smeltery.TinkerSmeltery;

public class SmelteryRecipes {

	public static void registerSmelteryStuff(){
		SmelteryRecipeHandler.addSmelteryFuel(LotrSmelteryFraction.Dwarf, FluidRegistry.LAVA, 1500, 90);
		
		/* Register Ores */
		Smeltery.addMelting(FluidType.getFluidType("Obsidian"), new ItemStack(LOTRMod.obsidianGravel), -300, TConstruct.nuggetLiquidValue * 2);
		Smeltery.addDictionaryMelting("oreSilver", FluidType.getFluidType("Silver"), 0, TConstruct.oreLiquidValue);
		SmelteryRecipeHandler.addMelting(LotrSmelteryFraction.Dwarf, LOTRMod.oreMithril, 0, 0, ModBlocks.moltenMithrilFluid, TConstruct.oreLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("MithrilLotR"), new ItemStack(LOTRMod.oreMithril), 100, (int)(TConstruct.ingotLiquidValue * 1.5D));
		SmelteryRecipeHandler.addMelting(LotrSmelteryFraction.Orc, LOTRMod.oreMorgulIron, 0, 0, ModBlocks.moltenOrcSteelFluid, TConstruct.oreLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("OrcSteel"), new ItemStack(LOTRMod.oreMorgulIron), 100, (int)(TConstruct.ingotLiquidValue * 1.5D));
		SmelteryRecipeHandler.addMelting(LotrSmelteryFraction.Orc, LOTRMod.oreMorgulIron, 1, 0, ModBlocks.moltenOrcSteelFluid, TConstruct.oreLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("OrcSteel"), new ItemStack(LOTRMod.oreMorgulIron, 1, 1), 100, (int)(TConstruct.ingotLiquidValue * 1.5D));
		
		/* Register Ingots and Blocks */
		Smeltery.addDictionaryMelting("blockSilver", FluidType.getFluidType("Silver"), 0, TConstruct.blockLiquidValue);
		Smeltery.addDictionaryMelting("ingotSilver", FluidType.getFluidType("Silver"), -50, TConstruct.ingotLiquidValue);
		Smeltery.addDictionaryMelting("nuggetSilver", FluidType.getFluidType("Silver"), -100, TConstruct.nuggetLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Sarlluin"), new ItemStack(LOTRMod.rock, 1, 4), 0, LotRTCConfig.stoneLiquidValue);		
		Smeltery.addMelting(FluidType.getFluidType("MithrilLotR"), new ItemStack(LOTRMod.blockOreStorage, 1, 4), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("MithrilLotR"), new ItemStack(LOTRMod.mithril), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("MithrilLotR"), new ItemStack(LOTRMod.mithrilNugget), -100, TConstruct.nuggetLiquidValue);
		
		Smeltery.addMelting(FluidType.getFluidType("BlueDwarvenSteel"), new ItemStack(LOTRMod.blockOreStorage, 1, 15), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("BlueDwarvenSteel"), new ItemStack(LOTRMod.blueDwarfSteel), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("DwarvenSteel"), new ItemStack(LOTRMod.blockOreStorage, 1, 7), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("DwarvenSteel"), new ItemStack(LOTRMod.dwarfSteel), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Galvorn"), new ItemStack(LOTRMod.blockOreStorage, 1, 8), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Galvorn"), new ItemStack(LOTRMod.galvorn), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("ElvenSteel"), new ItemStack(LOTRMod.blockOreStorage2, 1, 1), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("ElvenSteel"), new ItemStack(LOTRMod.elfSteel), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Edhelmir"), new ItemStack(LOTRMod.blockOreStorage, 1, 6), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Edhelmir"), new ItemStack(LOTRMod.quenditeCrystal), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("MorgulSteel"), new ItemStack(LOTRMod.blockOreStorage, 1, 12), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("MorgulSteel"), new ItemStack(LOTRMod.morgulSteel), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("OrcSteel"), new ItemStack(LOTRMod.blockOreStorage, 1, 5), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("OrcSteel"), new ItemStack(LOTRMod.orcSteel), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("UrukSteel"), new ItemStack(LOTRMod.blockOreStorage, 1, 9), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("UrukSteel"), new ItemStack(LOTRMod.urukSteel), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("BlackUrukSteel"), new ItemStack(LOTRMod.blockOreStorage2, 1, 0), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("BlackUrukSteel"), new ItemStack(LOTRMod.blackUrukSteel), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Durnaur"), new ItemStack(LOTRMod.blockOreStorage, 1, 10), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Durnaur"), new ItemStack(LOTRMod.nauriteGem), -50, TConstruct.ingotLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Gulduril"), new ItemStack(LOTRMod.blockOreStorage, 1, 11), 0, TConstruct.blockLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Gulduril"), new ItemStack(LOTRMod.guldurilCrystal), -50, TConstruct.ingotLiquidValue);
	
		/* Register bars */
		Smeltery.addMelting(FluidType.getFluidType("BlueDwarvenSteel"), new ItemStack(LOTRMod.blueDwarfBars), 0, LotRTCConfig.barLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Bronze"), new ItemStack(LOTRMod.bronzeBars), 0, LotRTCConfig.barLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("DwarvenSteel"), new ItemStack(LOTRMod.dwarfBars), 0, LotRTCConfig.barLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Gold"), new ItemStack(LOTRMod.goldBars), 0, LotRTCConfig.barLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("ElvenSteel"), new ItemStack(LOTRMod.galadhrimBars), 0, LotRTCConfig.barLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("ElvenSteel"), new ItemStack(LOTRMod.highElfBars), 0, LotRTCConfig.barLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("ElvenSteel"), new ItemStack(LOTRMod.woodElfBars), 0, LotRTCConfig.barLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("MithrilLotR"), new ItemStack(LOTRMod.mithrilBars), 0, LotRTCConfig.barLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("OrcSteel"), new ItemStack(LOTRMod.orcSteelBars), 0, LotRTCConfig.barLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Silver"), new ItemStack(LOTRMod.silverBars), 0, LotRTCConfig.barLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("UrukSteel"), new ItemStack(LOTRMod.urukBars), 0, LotRTCConfig.barLiquidValue);
	
		/* Register Treasures */
		Block[] treasures = new Block[]{LOTRMod.treasureCopper, LOTRMod.treasureGold, LOTRMod.treasureSilver};
		FluidType[]fluids = new FluidType[]{FluidType.getFluidType("Copper"), FluidType.getFluidType("Gold"), FluidType.getFluidType("Silver")};
		for(int i = 0; i < treasures.length; i++){
			for(int meta = 0; meta < 8; meta++){
				Smeltery.addMelting(fluids[i], new ItemStack(treasures[i], 1, meta), 0, (TConstruct.ingotLiquidValue*(meta+1)/2));
			}
		}
		
		/* Chandelier */
		Smeltery.addMelting(FluidType.getFluidType("Bronze"), new ItemStack(LOTRMod.chandelier, 1, 0), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Iron"), new ItemStack(LOTRMod.chandelier, 1, 1), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Silver"), new ItemStack(LOTRMod.chandelier, 1, 2), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("Gold"), new ItemStack(LOTRMod.chandelier, 1, 3), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("MithrilLotR"), new ItemStack(LOTRMod.chandelier, 1, 4), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("ElvenSteel"), new ItemStack(LOTRMod.chandelier, 1, 6), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("OrcSteel"), new ItemStack(LOTRMod.chandelier, 1, 7), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("DwarvenSteel"), new ItemStack(LOTRMod.chandelier, 1, 8), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("UrukSteel"), new ItemStack(LOTRMod.chandelier, 1, 9), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("ElvenSteel"), new ItemStack(LOTRMod.chandelier, 1, 10), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("BlueDwarvenSteel"), new ItemStack(LOTRMod.chandelier, 1, 11), -100, TConstruct.chunkLiquidValue);
		Smeltery.addMelting(FluidType.getFluidType("MorgulSteel"), new ItemStack(LOTRMod.chandelier, 1, 12), -100, TConstruct.chunkLiquidValue);
	
		/* Trimmed Blocks */
		Smeltery.addMelting(FluidType.getFluidType("Silver"), new ItemStack(LOTRMod.brick, 1, 8), 0, TConstruct.nuggetLiquidValue*4);
		Smeltery.addMelting(FluidType.getFluidType("Gold"), new ItemStack(LOTRMod.brick, 1, 9), 0, TConstruct.nuggetLiquidValue*4);
		Smeltery.addMelting(FluidType.getFluidType("MithrilLotR"), new ItemStack(LOTRMod.brick, 1, 10), 0, TConstruct.nuggetLiquidValue*4);
		Smeltery.addMelting(FluidType.getFluidType("Silver"), new ItemStack(LOTRMod.brick4, 1, 8), 0, TConstruct.nuggetLiquidValue*4);
		Smeltery.addMelting(FluidType.getFluidType("Silver"), new ItemStack(LOTRMod.brick4, 1, 9), 0, TConstruct.nuggetLiquidValue*4);
		Smeltery.addMelting(FluidType.getFluidType("Silver"), new ItemStack(LOTRMod.brick4, 1, 10), 0, TConstruct.nuggetLiquidValue*4);
		Smeltery.addMelting(FluidType.getFluidType("Gold"), new ItemStack(LOTRMod.brick4, 1, 11), 0, TConstruct.nuggetLiquidValue*4);
		Smeltery.addMelting(FluidType.getFluidType("Gold"), new ItemStack(LOTRMod.brick4, 1, 12), 0, TConstruct.nuggetLiquidValue*4);
		Smeltery.addMelting(FluidType.getFluidType("Gold"), new ItemStack(LOTRMod.brick4, 1, 13), 0, TConstruct.nuggetLiquidValue*4);
		Smeltery.addMelting(FluidType.getFluidType("Obsidian"), new ItemStack(LOTRMod.brick4, 1, 14), 0, TConstruct.nuggetLiquidValue*4);
	}

	
	
	private static void registerIngotCasting(FluidType ft, ItemStack ore){
		ItemStack pattern = new ItemStack(TinkerSmeltery.metalPattern, 1, 0);
		LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
		tableCasting.addCastingRecipe(pattern, new FluidStack(TinkerSmeltery.moltenAlubrassFluid, 144), new ItemStack(ore.getItem(), 1, ore.getItemDamage()), false, 50);
		tableCasting.addCastingRecipe(pattern, new FluidStack(TinkerSmeltery.moltenGoldFluid, 288), new ItemStack(ore.getItem(), 1, ore.getItemDamage()), false, 50);
		tableCasting.addCastingRecipe(new ItemStack(ore.getItem(), 1, ore.getItemDamage()), new FluidStack(ft.fluid, 144), pattern, 80);
	}
	
	/*      */   private static void registerIngotCasting(FluidType ft, String name)
	/*      */   {
	/*  924 */     ItemStack pattern = new ItemStack(TinkerSmeltery.metalPattern, 1, 0);
	/*  925 */     LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
	/*  926 */     for (ItemStack ore : OreDictionary.getOres(name))
	/*      */     {
	/*  928 */       tableCasting.addCastingRecipe(pattern, new FluidStack(TinkerSmeltery.moltenAlubrassFluid, 144), new ItemStack(ore.getItem(), 1, ore.getItemDamage()), false, 50);
	/*  929 */       tableCasting.addCastingRecipe(pattern, new FluidStack(TinkerSmeltery.moltenGoldFluid, 288), new ItemStack(ore.getItem(), 1, ore.getItemDamage()), false, 50);
	/*  930 */       tableCasting.addCastingRecipe(new ItemStack(ore.getItem(), 1, ore.getItemDamage()), new FluidStack(ft.fluid, 144), pattern, 80);
	/*      */     }
	/*      */   }
	/*      */   
	/*      */   private static void registerNuggetCasting(FluidType ft, String name)
	/*      */   {
	/*  936 */     ItemStack pattern = new ItemStack(TinkerSmeltery.metalPattern, 1, 27);
	/*  937 */     LiquidCasting tableCasting = TConstructRegistry.getTableCasting();
	/*  938 */     for (ItemStack ore : OreDictionary.getOres(name)) {
	/*  950 */         tableCasting.addCastingRecipe(pattern, new FluidStack(TinkerSmeltery.moltenAlubrassFluid, 144), new ItemStack(ore.getItem(), 1, ore.getItemDamage()), false, 50);
	/*  951 */         tableCasting.addCastingRecipe(pattern, new FluidStack(TinkerSmeltery.moltenGoldFluid, 288), new ItemStack(ore.getItem(), 1, ore.getItemDamage()), false, 50);
	/*  952 */         tableCasting.addCastingRecipe(new ItemStack(ore.getItem(), 1, ore.getItemDamage()), new FluidStack(ft.fluid, 16), pattern, 40);
	/*      */     }
	/*      */   }
	/*      */   
	/*      */   private static void registerBlockCasting(FluidType ft, String name) {
	/*  958 */     for (ItemStack ore : OreDictionary.getOres(name))
	/*      */     {
	/*  960 */       TConstructRegistry.getBasinCasting().addCastingRecipe(new ItemStack(ore.getItem(), 1, ore.getItemDamage()), new FluidStack(ft.fluid, 1296), 100);
	/*      */     }
	/*      */   }
}