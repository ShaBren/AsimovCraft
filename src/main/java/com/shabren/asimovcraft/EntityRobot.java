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

public class EntityRobot extends EntityLiving
{
	private JythonInterpreter interpreter;
	private final Semaphore available = new Semaphore( 1, true );
	private int currentTick = 0;
	private int lastTick = 0;
	private String owner;
	private RobotEvent nextEvent;
	private static final int POS_X = 20;
	private static final int POS_Y = 21;
	private static final int POS_Z = 22;
	private static final int ROT_Y = 23;

	public EntityRobot( World par1 )
	{
		super( par1 );

		this.setSize( 1.0f, 1.0f );
		this.isImmuneToFire = true;
		this.noClip = true;

		this.dataWatcher.addObject( POS_X, Float.valueOf( 0.0f ) );
		this.dataWatcher.addObject( POS_Y, Float.valueOf( 0.0f ) );
		this.dataWatcher.addObject( POS_Z, Float.valueOf( 0.0f ) );
		this.dataWatcher.addObject( ROT_Y, Float.valueOf( 0.0f ) );

		this.dataWatcher.updateObject( POS_X, Float.valueOf( ( float )this.posX ) );
		this.dataWatcher.updateObject( POS_Y, Float.valueOf( ( float )this.posY ) );
		this.dataWatcher.updateObject( POS_Z, Float.valueOf( ( float )this.posZ ) );
		this.dataWatcher.updateObject( ROT_Y, Float.valueOf( ( float )this.rotationYaw ) );
	}

	public void loadSource( String source, EntityPlayer player )
	{
		owner = player.getCommandSenderName();

		if ( !this.worldObj.isRemote )
		{
			interpreter = new JythonInterpreter();
			interpreter.api = new RobotAPI( this );
			interpreter.setSource( source );
			interpreter.setOStream( new RobotOutputStream().setRobot( this ) );
			interpreter.start();
		}
	}

	@Override
	protected void fall( float p_70069_1_ )
	{
	}

	@Override
	protected void updateFallState( double p_70064_1_, boolean p_70064_3_ )
	{
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if ( this.worldObj.isRemote )
		{
			this.setPosition( this.dataWatcher.getWatchableObjectFloat( POS_X ), this.dataWatcher.getWatchableObjectFloat( POS_Y ), this.dataWatcher.getWatchableObjectFloat( POS_Z ) );

			this.rotationYaw = this.dataWatcher.getWatchableObjectFloat( ROT_Y );
		}
		else
		{
			if ( currentTick % 10 == 0 )
			{
				this.processPendingEvent();
			}

			currentTick++;
		}
	}

	@Override
	public void onLivingUpdate()
	{
	}

	@Override
	public void moveEntityWithHeading( float p_70612_1_, float p_70612_2_ )
	{
	}

	private void processPendingEvent()
	{
		this.worldObj.theProfiler.startSection( "robotTick" );

		if ( nextEvent != null )
		{
			nextEvent.run( this );
			lastTick = currentTick;
		}

		nextEvent = null;

		available.release();

		this.worldObj.theProfiler.endSection();
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

	public void turn( float degrees )
	{
		if ( Math.abs( degrees ) > 360 )
		{
			return;
		}

		float facing = Math.round( ( this.rotationYaw + 45 ) / 90 );

		this.rotationYaw = ( ( ( facing * 90 ) + degrees ) % 360 ) - 45;

		if ( this.rotationYaw < 0 )
		{
			this.rotationYaw = 360 + this.rotationYaw;
		}

		this.dataWatcher.updateObject( ROT_Y, Float.valueOf( ( float )this.rotationYaw ) );
	}

	public void move( double offsetX, double offsetY, double offsetZ )
	{
		int facing = ( int )Math.round( ( this.rotationYaw + 45 ) / 90 );

		switch ( facing )
		{
		case 0:
			this.goTo( posX + offsetX, posY + offsetY, posZ + offsetZ );
			break;
		case 1:
			this.goTo( posX - offsetZ, posY + offsetY, posZ + offsetX );
			break;
		case 2:
			this.goTo( posX - offsetX, posY + offsetY, posZ - offsetZ );
			break;
		case 3:
			this.goTo( posX + offsetZ, posY + offsetY, posZ - offsetX );
			break;
		}
	}

	public void goTo( double pPosX, double pPosY, double pPosZ )
	{
		this.setPosition( pPosX, pPosY, pPosZ );

		this.dataWatcher.updateObject( POS_X, Float.valueOf( ( float )this.posX ) );
		this.dataWatcher.updateObject( POS_Y, Float.valueOf( ( float )this.posY ) );
		this.dataWatcher.updateObject( POS_Z, Float.valueOf( ( float )this.posZ ) );
	}

	public void threadDied()
	{

	}

	public void sendToOwner( String string )
	{
		EntityPlayer player = this.worldObj.getPlayerEntityByName( owner );

		if ( player == null )
		{
			return;
		}

		player.addChatMessage( new ChatComponentText( string ) );
	}

	public void breakBlock( int x, int y, int z )
	{
		Block b = this.worldObj.getBlock( x, y, z );

		ArrayList< ItemStack > drops = b.getDrops( this.worldObj, x, y, z, this.worldObj.getBlockMetadata( x, y, z ), 0 );
		b.dropBlockAsItem( this.worldObj, x, y, z, this.worldObj.getBlockMetadata( x, y, z ), 0 );
		this.worldObj.removeTileEntity( x, y, z );
		this.worldObj.setBlockToAir( x, y, z );
	}
}
