package com.shabren.asimovcraft.client;

import net.minecraft.client.gui.GuiScreen;

public class GuiController extends GuiScreen
{
	public static final int GUI_ID = 20;

	public GuiController()
	{
	}

	@Override
	public void drawScreen( int par1, int par2, float par3 )
	{
		this.drawDefaultBackground();
	}

	@Override
	public void initGui()
	{
		super.initGui();
		// make buttons
		// id, x, y, width, height, text
		// controlList.add(new GuiButton(1, 10, 52, 20, 20, "+"));
		// controlList.add(new GuiButton(2, 40, 72, 20, 20, "-"));
	}

	// protected void actionPerformed(GuiButton guibutton) {
	// id is the id you give your button
	// switch(guibutton.id) {
	// case 1:
	// i += 1;
	// break;
	// case 2:
	// i -= 1;
	// }
	// Packet code here
	// PacketDispatcher.sendPacketToServer(packet); //send packet
	// }
}
