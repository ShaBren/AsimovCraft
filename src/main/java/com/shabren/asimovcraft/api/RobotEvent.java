package com.shabren.asimovcraft.api;

import com.shabren.asimovcraft.Robot;

public interface RobotEvent
{
	public void run( Robot robot );
	public int fuelNeeded();
}
