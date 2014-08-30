package com.shabren.asimovcraft.controller;

import com.shabren.asimovcraft.EntityRobot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

public class TileController extends TileEntity implements IInventory
{
	protected EntityRobot robot;
	protected EntityPlayer owner;
	protected ItemStack[] inventory;

	public TileController()
	{
		inventory = new ItemStack[ 36 ];
	}

	public void openGUI( EntityPlayer player )
	{
		if ( !this.playerCanUse( player ) )
		{
			return;
		}
	}

	public boolean playerCanUse( EntityPlayer player )
	{
		if ( owner == null )
		{
			owner = player;
			return true;
		}

		if ( owner == player )
		{
			return true;
		}

		return false;
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot( int slot )
	{
		return inventory[ slot ];
	}

	@Override
	public void setInventorySlotContents( int slot, ItemStack stack )
	{
		inventory[ slot ] = stack;
		if ( stack != null && stack.stackSize > getInventoryStackLimit() )
		{
			stack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public ItemStack decrStackSize( int slot, int amt )
	{
		ItemStack stack = getStackInSlot( slot );
		if ( stack != null )
		{
			if ( stack.stackSize <= amt )
			{
				setInventorySlotContents( slot, null );
			}
			else
			{
				stack = stack.splitStack( amt );
				if ( stack.stackSize == 0 )
				{
					setInventorySlotContents( slot, null );
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing( int slot )
	{
		ItemStack stack = getStackInSlot( slot );
		if ( stack != null )
		{
			setInventorySlotContents( slot, null );
		}
		return stack;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer( EntityPlayer player )
	{
		return worldObj.getTileEntity( xCoord, yCoord, zCoord ) == this && player.getDistanceSq( xCoord + 0.5, yCoord + 0.5, zCoord + 0.5 ) < 64;
	}

	@Override
	public void readFromNBT( NBTTagCompound data )
	{
		NBTTagList nbttaglist = data.getTagList( "Items", Constants.NBT.TAG_COMPOUND );

		for ( int j = 0; j < nbttaglist.tagCount(); ++j )
		{
			NBTTagCompound slot = nbttaglist.getCompoundTagAt( j );
			int index;

			if ( slot.hasKey( "index" ) )
			{
				index = slot.getInteger( "index" );
			}
			else
			{
				index = slot.getByte( "Slot" );
			}

			if ( index >= 0 && index < inventory.length )
			{
				setInventorySlotContents( index, ItemStack.loadItemStackFromNBT( slot ) );
			}
		}
	}

	@Override
	public void writeToNBT( NBTTagCompound data )
	{
		NBTTagList slots = new NBTTagList();

		for ( byte index = 0; index < inventory.length; ++index )
		{
			if ( inventory[ index ] != null && inventory[ index ].stackSize > 0 )
			{
				NBTTagCompound slot = new NBTTagCompound();
				slots.appendTag( slot );
				slot.setByte( "Slot", index );
				inventory[ index ].writeToNBT( slot );
			}
		}

		data.setTag( "Items", slots );
	}

	@Override
	public String getInventoryName()
	{
		return "Robot Controller";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public boolean isItemValidForSlot( int p_94041_1_, ItemStack p_94041_2_ )
	{
		// TODO Auto-generated method stub
		return false;
	}
}
