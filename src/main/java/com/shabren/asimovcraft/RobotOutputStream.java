package com.shabren.asimovcraft;

import java.io.IOException;
import java.io.OutputStream;

public class RobotOutputStream extends OutputStream
{
	private StringBuilder buffer = new StringBuilder();
	private RoboBrain brain;

	public RobotOutputStream setRobot( RoboBrain pbrane )
	{
		brain = pbrane;
		return this;
	}

	@Override
	public void write( int arg0 ) throws IOException
	{
		if ( arg0 == '\n' )
		{
			brain.sendToOwner( buffer.toString() );
			buffer.setLength( 0 );
		}
		else
		{
			buffer.append( ( char )arg0 );
		}
	}

}
