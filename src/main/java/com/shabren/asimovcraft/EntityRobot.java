package com.shabren.asimovcraft;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class EntityRobot extends TileEntity
{
	public enum Facing { NORTH, EAST, SOUTH, WEST };

	protected RoboBrain brain;
	protected World world;

	public EntityRobot( World par1 )
	{
		world = par1;
		
		brain = AsimovCraft.getBrain( world, -1 );
		brain.loadSource( "9ed589e48a94f74c9b13", player );
	}

	@Override
	public void writeToNBT( NBTTagCompound par1 )
	{
		super.writeToNBT( par1 );
	}

	@Override
	public void readFromNBT( NBTTagCompound par1 )
	{
		super.readFromNBT( par1 );
	}

}
