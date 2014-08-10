package com.shabren.asimovcraft;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import cpw.mods.fml.common.SidedProxy;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RoboBrain
{
	private JythonInterpreter interpreter;
	private final Semaphore available = new Semaphore( 1, true );
	private int currentTick = 0;
	private int lastTick = 0;
	private RobotEvent nextEvent;

	public World world;
	public String owner;
	public String lastSource;
	public int id;

	public RoboBrain( World par1, int pId )
	{
		this.world = par1;
		
		id = pId;
	}

	@SuppressWarnings( "resource" )
	public void loadSource( String source, EntityPlayer player )
	{
		owner = player.getCommandSenderName();
		lastSource = source;

		interpreter = new JythonInterpreter();
		interpreter.api = new RobotAPI( this );
		interpreter.setSource( source );
		interpreter.setOStream( new RobotOutputStream().setRobot( this ) );
		interpreter.start();
	}

	public void onUpdate()
	{
		if ( currentTick % 10 == 0 )
		{
			this.processPendingEvent();
		}

		currentTick++;
	}

	private void processPendingEvent()
	{
		if ( nextEvent != null )
		{
			nextEvent.run( this );
			lastTick = currentTick;
		}

		nextEvent = null;

		available.release();
	}

	public void queueEvent( RobotEvent event )
	{
		try
		{
			available.acquire();
		}
		catch ( Exception e )
		{
			this.threadDied();
		}

		nextEvent = event;
	}

	public void turn( double degrees )
	{
	}

	public void move( double offsetX, double offsetY, double offsetZ )
	{
	}

	public void goTo( double pPosX, double pPosY, double pPosZ )
	{
	}

	public double getX()
	{
		return 0.0;
	}

	public double getY()
	{
		return 0.0;
	}

	public double getZ()
	{
		return 0.0;
	}

	public void threadDied()
	{

	}

	public void sendToOwner( String string )
	{
		EntityPlayer player = this.world.getPlayerEntityByName( owner );

		if ( player == null )
		{
			return;
		}

		player.addChatMessage( new ChatComponentText( string ) );
	}

	public void breakBlock( int x, int y, int z )
	{
		Block b = this.world.getBlock( x, y, z );

		ArrayList< ItemStack > drops = b.getDrops( this.world, x, y, z, this.world.getBlockMetadata( x, y, z ), 0 );
		b.dropBlockAsItem( this.world, x, y, z, this.world.getBlockMetadata( x, y, z ), 0 );
		this.world.removeTileEntity( x, y, z );
		this.world.setBlockToAir( x, y, z );
	}
}
