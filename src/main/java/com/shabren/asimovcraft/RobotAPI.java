package com.shabren.asimovcraft;

import com.shabren.asimovcraft.EntityRobot.RobotEventType;

public class RobotAPI
{
	private EntityRobot robot;

	public RobotAPI( EntityRobot pRobot )
	{
		robot = pRobot;
	}

	public boolean goForward()
	{
		robot.queueEvent( RobotEventType.MOVE_FORWARD );
		return true;
	}

	public boolean goBack()
	{
		robot.queueEvent( RobotEventType.MOVE_BACKWARD );
		return true;
	}

	public boolean goLeft()
	{
		robot.queueEvent( RobotEventType.MOVE_LEFT );
		return true;
	}

	public boolean goRight()
	{
		robot.queueEvent( RobotEventType.MOVE_RIGHT );
		return true;
	}

	public boolean goUp()
	{
		robot.queueEvent( RobotEventType.MOVE_UP );
		return true;
	}

	public boolean goDown()
	{
		robot.queueEvent( RobotEventType.MOVE_DOWN );
		return true;
	}

}
