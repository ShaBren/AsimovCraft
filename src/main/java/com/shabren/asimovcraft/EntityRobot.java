package com.shabren.asimovcraft;

import java.util.ArrayList;

import cpw.mods.fml.common.SidedProxy;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EntityRobot extends EntityLiving
{
	protected Robot robot;

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

		this.loadRobot( player, "", "", 0 );
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

		if ( this.robot != null )
		{
			this.robot.doTick();
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
			double d3 = MathHelper.wrapAngleTo180_double( this.newRotationYaw - ( double )this.rotationYaw );
			this.rotationYaw = ( float )( ( double )this.rotationYaw + d3 / ( double )this.newPosRotationIncrements );
			this.rotationPitch = ( float )( ( double )this.rotationPitch + ( this.newRotationPitch - ( double )this.rotationPitch ) / ( double )this.newPosRotationIncrements );
			// this.rotationYaw = this.facing * 90 + 45;
			this.rotationPitch = 0;
			--this.newPosRotationIncrements;
			this.setPosition( d0, d1, d2 );
			this.setRotation( this.rotationYaw, this.rotationPitch );
		}
	}

	public void writeEntityToNBT( NBTTagCompound par )
	{
		super.writeEntityToNBT( par );

		par.setString( "source", robot.getLastSource() );
		par.setString( "owner", robot.getOwner() );
		par.setString( "name", robot.getName() );
		par.setInteger( "facing", robot.getFacing() );
	}

	public void readEntityFromNBT( NBTTagCompound par )
	{
		super.readEntityFromNBT( par );

		this.loadRobot( par.getString( "owner" ), par.getString( "name" ), par.getString( "source" ), par.getInteger( "facing" ) );
	}

	protected void loadRobot( String owner, String name, String source, int facing )
	{
		if ( !this.worldObj.isRemote )
		{
			this.robot = new Robot( this, owner );

			this.robot.setName( name );
			this.robot.loadSource( source );
			this.robot.setFacing( facing );
		}
	}

	public void breakBlock( int x, int y, int z )
	{
		Block b = this.worldObj.getBlock( x, y, z );

		ArrayList< ItemStack > drops = b.getDrops( this.worldObj, x, y, z, this.worldObj.getBlockMetadata( x, y, z ), 0 );
		b.dropBlockAsItem( this.worldObj, x, y, z, this.worldObj.getBlockMetadata( x, y, z ), 0 );
		this.worldObj.removeTileEntity( x, y, z );
		this.worldObj.setBlockToAir( x, y, z );
	}

	public int getX()
	{
		return ( int )posX;
	}

	public int getY()
	{
		return ( int )posY;
	}

	public int getZ()
	{
		return ( int )posZ;
	}

}
