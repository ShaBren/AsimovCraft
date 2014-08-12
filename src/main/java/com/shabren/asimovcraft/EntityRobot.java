package com.shabren.asimovcraft;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import jnr.ffi.annotations.Synchronized;
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
	public static class Facing
	{
		public static final int NORTH = 0;
		public static final int EAST = 1;
		public static final int SOUTH = 2;
		public static final int WEST = 3;
	};

	private static final int FACING = 20;

	private String owner;
	private String lastSource;
	private String name;
	private RobotEvent nextEvent;
	private JythonInterpreter interpreter;
	private final Semaphore available = new Semaphore( 1, true );
	private int currentTick = 0;
	private int lastTick = 0;

	public int facing = Facing.NORTH;

	public EntityRobot( World par1 )
	{
		this( par1, "" );
	}

	public EntityRobot( World par1, String player )
	{
		super( par1 );

		this.setSize( 1.0f, 1.0f );
		this.isImmuneToFire = true;
		this.noClip = true;
		this.owner = player;
		this.name = Long.toHexString( this.worldObj.getTotalWorldTime() );

		this.dataWatcher.addObject( FACING, Integer.valueOf( 0 ) );
		this.dataWatcher.updateObject( FACING, Integer.valueOf( facing ) );
	}

	public void loadSource( String source )
	{
		lastSource = source;

		if ( !this.worldObj.isRemote )
		{
			interpreter = new JythonInterpreter();
			interpreter.api = new RobotAPI( this );
			interpreter.setSource( source );
			interpreter.setOStream( new RobotOutputStream().setRobot( this ) );
			interpreter.setPriority( Thread.MIN_PRIORITY );
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
			this.renderYawOffset = 0;
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
		if ( this.newPosRotationIncrements > 0 )
		{
			double d0 = this.posX + ( this.newPosX - this.posX ) / ( double )this.newPosRotationIncrements;
			double d1 = this.posY + ( this.newPosY - this.posY ) / ( double )this.newPosRotationIncrements;
			double d2 = this.posZ + ( this.newPosZ - this.posZ ) / ( double )this.newPosRotationIncrements;
			//double d3 = MathHelper.wrapAngleTo180_double( this.newRotationYaw - ( double )this.rotationYaw );
			//this.rotationYaw = ( float )( ( double )this.rotationYaw + d3 / ( double )this.newPosRotationIncrements );
			//this.rotationPitch = ( float )( ( double )this.rotationPitch + ( this.newRotationPitch - ( double )this.rotationPitch ) / ( double )this.newPosRotationIncrements );
			this.rotationYaw = this.facing * 90 + 45;
			this.rotationPitch = 0;
			--this.newPosRotationIncrements;
			this.setPosition( d0, d1, d2 );
			this.setRotation( this.rotationYaw, this.rotationPitch );
		}

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

	// direction = 1 for right, -1 for left
	public void turn( int direction )
	{
		facing = ( facing + direction ) % 4;

		if ( facing < 0 )
		{
			facing += 4;
		}

		this.dataWatcher.updateObject( FACING, Integer.valueOf( facing ) );
	}

	public void move( double offsetX, double offsetY, double offsetZ )
	{
		switch ( facing )
		{
		case Facing.NORTH:
			this.goTo( posX + offsetX, posY + offsetY, posZ + offsetZ );
			break;
		case Facing.EAST:
			this.goTo( posX - offsetZ, posY + offsetY, posZ + offsetX );
			break;
		case Facing.SOUTH:
			this.goTo( posX - offsetX, posY + offsetY, posZ - offsetZ );
			break;
		case Facing.WEST:
			this.goTo( posX + offsetZ, posY + offsetY, posZ - offsetX );
			break;
		}
	}

	public void goTo( double pPosX, double pPosY, double pPosZ )
	{
		this.setPositionAndRotation( pPosX, pPosY, pPosZ, facing * 90 + 45, 0 );
	}

	public void threadDied()
	{

	}

	public void writeEntityToNBT( NBTTagCompound par )
	{
		super.writeEntityToNBT( par );

		par.setString( "source", this.lastSource );
		par.setString( "owner", this.owner );
		par.setString( "name", this.getName() );
		par.setInteger( "facing", this.facing );
	}

	public void readEntityFromNBT( NBTTagCompound par )
	{
		super.readEntityFromNBT( par );

		this.facing = par.getInteger( "facing" );
		this.owner = par.getString( "owner" );
		this.setName( par.getString( "name" ) );
		this.loadSource( par.getString( "source" ) );
	}

	public void sendToOwner( String string )
	{
		EntityPlayer player = this.worldObj.getPlayerEntityByName( owner );

		if ( player == null )
		{
			return;
		}

		player.addChatMessage( new ChatComponentText( "[" + this.getName() + "] " + string ) );
	}

	public void breakBlock( int x, int y, int z )
	{
		Block b = this.worldObj.getBlock( x, y, z );

		ArrayList< ItemStack > drops = b.getDrops( this.worldObj, x, y, z, this.worldObj.getBlockMetadata( x, y, z ), 0 );
		b.dropBlockAsItem( this.worldObj, x, y, z, this.worldObj.getBlockMetadata( x, y, z ), 0 );
		this.worldObj.removeTileEntity( x, y, z );
		this.worldObj.setBlockToAir( x, y, z );
	}
	
	public String getOwner()
	{
		return this.owner;
	}
	
	public synchronized String getName()
	{
		return this.name;
	}

	public synchronized void setName( String name )
	{
		this.name = name;
	}
}
