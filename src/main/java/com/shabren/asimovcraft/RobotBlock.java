package com.shabren.asimovcraft;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class RobotBlock extends Block
{
	public RobotBlock()
	{
		super( Material.iron );
	}
	
	public TileEntity createTileEntity( World world, int metadata )
	{
	   return new EntityRobot( world );
	}
}
