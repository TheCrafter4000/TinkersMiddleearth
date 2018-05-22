package com.thecrafter4000.lotrtc.smeltery;

import com.thecrafter4000.lotrtc.TinkersMiddleearth;
import cpw.mods.fml.client.registry.RenderingRegistry;
import mantle.world.CoordTuple;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import tconstruct.client.BlockSkinRenderHelper;
import tconstruct.smeltery.model.SmelteryRender;
import tconstruct.util.ItemHelper;

public class FactionSmelteryRender extends SmelteryRender {

	public static int renderId = RenderingRegistry.getNextAvailableRenderId();
	
    @Override
    public void renderInventoryBlock (Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        if (modelID == renderId)
        {
            ItemHelper.renderStandardInvBlock(renderer, block, metadata);
        }
    }

    @Override
    public boolean renderWorldBlock (IBlockAccess world, int x, int y, int z, Block block, int modelID, RenderBlocks renderer)
    {
        if (modelID == renderId)
        {
            if (world.getBlockMetadata(x, y, z) == 0)
                return renderSmeltery(world, x, y, z, block, modelID, renderer);
            else
                renderer.renderStandardBlock(block, x, y, z);
        }
        return true;
    }
    
    public boolean renderSmeltery (IBlockAccess world, int x, int y, int z, Block block, int modelID, RenderBlocks renderer)
    {
        boolean ret = renderer.renderStandardBlock(block, x, y, z);
		FactionSmelteryLogic logic = (FactionSmelteryLogic) world.getTileEntity(x, y, z);
        if (logic.validStructure)
        {
            CoordTuple from = logic.minPos;
            CoordTuple to = logic.maxPos;

            //Melting
            if (logic.getSizeInventory() > 0)
            {
                for (int i = 0; i < logic.layers; i++)
                {
                    renderLayer2(logic, i * logic.getBlocksPerLayer(), from, to, from.y + i, renderer, world);
                }
            }

            //Liquids
            float base = 0F;
            int yBase = 0;
            int liquidBase = 0;
            for (FluidStack liquid : logic.moltenMetal)
            {
                int liquidSize = liquid.amount;
                while (liquidSize > 0)
                {
                    int cap = logic.getCapacityPerLayer();
                    int room = cap - liquidBase;
                    int countSize = liquidSize > room ? room : liquidSize;
                    liquidSize -= countSize;

                    float height = countSize > cap ? 1.0F : (float) countSize / (float) cap;
                    float renderBase = base;
                    float renderHeight = height + base;
                    base += height;
                    liquidBase += countSize;

                    if(renderHeight < 0.01)
                        renderHeight = 0.01f;

                    renderer.setRenderBounds(0, renderBase, 0, 1, renderHeight, 1);
                    Fluid fluid = liquid.getFluid();
                    for (int xi = from.x; xi <= to.x; xi++)
                        for (int zi = from.z; zi <= to.z; zi++)
                        {
                            float minX = xi == from.x ? -0.001F : 0F;
                            float minZ = zi == from.z ? -0.001F : 0F;
                            float maxX = xi == to.x ? 1.001F : 1F;
                            float maxZ = zi == to.z ? 1.001F : 1F;
                            renderer.setRenderBounds(minX, renderBase, minZ, maxX, renderHeight, maxZ);
                            if (fluid.canBePlacedInWorld())
                                BlockSkinRenderHelper.renderMetadataBlock(fluid.getBlock(), 0, xi, from.y + yBase, zi, renderer, world);
                            else
                                BlockSkinRenderHelper.renderLiquidBlock(fluid.getStillIcon(), fluid.getFlowingIcon(), xi, from.y + yBase, zi, renderer, world, false, fluid.getColor(liquid));
                        }

                    if (countSize == room)
                    {
                        base = 0F;
                        yBase++;
                        liquidBase = 0;
                    }
                    ret = true;
                }
            }
        }
        return ret;
    }

	private void renderLayer2(FactionSmelteryLogic logic, int start, CoordTuple from, CoordTuple to, int posY, RenderBlocks renderer, IBlockAccess world)
    {
        renderer.setRenderBounds(-0.001F, -0.001F, -0.001F, 1.001F, 1.001F, 1.001F);
        int i = start;
        for (int x = from.x; x <= to.x; x++)
            for (int z = from.z; z <= to.z; z++)
            {
                // safety because of changes.
                if(i > logic.getSizeInventory())
                    return;
                ItemStack input = logic.getStackInSlot(i);
                if (input != null && logic.getTempForSlot(i) > 20)
                {
					ItemStack blockToRender = SmelteryRecipeHandler.getRenderIndex(logic.faction, input);
                    if (blockToRender != null)
                    {
                        float blockHeight = input.stackSize / (float) blockToRender.stackSize;
                        renderer.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, MathHelper.clamp_float(blockHeight, 0.01F, 1.0F), 1.0F);

                        Block liquidBlock = Block.getBlockFromItem(blockToRender.getItem());
                        if(blockToRender.getItem() != null) BlockSkinRenderHelper.renderMetadataBlock(liquidBlock, blockToRender.getItemDamage(), x, posY, z, renderer, world);
                        else TinkersMiddleearth.logger.fatal("Invalid Render Mapping! Item=" + logic.getStackInSlot(i).getDisplayName());
                    }
                }
                i++;
            }
    }
    
    public int getRenderId() {
		return renderId;
	}
}
