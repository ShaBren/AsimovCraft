package com.shabren.asimovcraft.controller;

import com.shabren.asimovcraft.AsimovCraft;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class BlockController extends BlockContainer
{
	public BlockController()
	{
		super( Material.iron );
	}

	@Override
	public TileEntity createNewTileEntity( World p_149915_1_, int p_149915_2_ )
	{
		return new TileController();
	}

	public boolean onBlockActivated( World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_ )
	{
		if ( world.isRemote )
		{
			return true;
		}

		AsimovCraft.logger.info( "Controller activated..." );

		TileController controller = ( TileController )world.getTileEntity( x, y, z );

		if ( controller != null )
		{
			AsimovCraft.logger.info( "Found the TileEntity..." );

			if ( controller.playerCanUse( player ) )
			{
				AsimovCraft.logger.info( "Attempting to open the GUI..." );
				player.openGui( AsimovCraft.instance, 20, world, x, y, z );
			}
		}

		return true;
	}
}
