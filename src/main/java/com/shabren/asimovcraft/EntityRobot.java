package com.shabren.asimovcraft;

import java.util.concurrent.Semaphore;

import cpw.mods.fml.common.SidedProxy;
import net.minecraft.world.World;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;

public class EntityRobot extends EntityCreature
{
	private JythonInterpreter interpreter = null;
	private final Semaphore available = new Semaphore( 1, true );
	boolean pendingMove = false;

	public EntityRobot( World par1 )
	{
		super( par1 );

		if ( !par1.isRemote )
		{
			interpreter = new JythonInterpreter();
			interpreter.robot = this;
			interpreter.init();
			interpreter.start();
		}
	}

	public void onUpdate()
	{
		super.onUpdate();

		if ( pendingMove )
		{
			this.setPositionAndUpdate( posX, posY, posZ + 1 );
		}

		available.release();
	}

	public void goTo( double posX, double posY, double posZ )
	{
		try
		{
			available.acquire();
		}
		catch ( InterruptedException e )
		{
			this.threadDied();
		}

		pendingMove = true;
	}

	public void threadDied()
	{

	}
}
