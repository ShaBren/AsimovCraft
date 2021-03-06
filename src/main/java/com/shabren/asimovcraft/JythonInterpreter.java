package com.shabren.asimovcraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import org.python.core.PyCode;
import org.python.core.PyDictionary;
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import com.shabren.asimovcraft.api.RobotAPI;
import com.shabren.asimovcraft.api.RobotAPIMovement;

public class JythonInterpreter extends Thread
{
	private PythonInterpreter interp = null;
	private String sourceID;
	private OutputStream ostream;
	private Robot robot;

	private RobotAPI apiGeneral;
	private RobotAPIMovement apiMovement;

	public boolean keepRunning = true;

	public JythonInterpreter( Robot robot )
	{
		this.robot = robot;

		apiGeneral = new RobotAPI( robot );
		apiMovement = new RobotAPIMovement( robot );
	}

	public void setSource( String sid )
	{
		sourceID = sid;

		synchronized ( this )
		{
			this.notify();
		}
	}

	public void setOStream( OutputStream stream )
	{
		ostream = stream;
	}

	@Override
	public void run()
	{
		while ( keepRunning )
		{
			if ( sourceID.length() > 0 )
			{
				try
				{
					this.loadProgram();
				}
				catch ( Throwable e )
				{
					robot.sendToOwner( "Robot Exception:" );

					for ( String line : e.toString().split( "\n" ) )
					{
						robot.sendToOwner( line );
					}
				}
			}

			try
			{
				synchronized ( this )
				{
					this.wait();
				}
			}
			catch ( InterruptedException e )
			{
			}
		}
	}

	private String loadSource( String address )
	{
		StringBuilder output = new StringBuilder();

		try
		{
			URL url = new URL( "http://haste.shabren.com/raw/" + address );
			BufferedReader in = new BufferedReader( new InputStreamReader( url.openStream() ) );

			String inputLine;

			while ( ( inputLine = in.readLine() ) != null )
			{
				output.append( inputLine );
				output.append( "\n" );
			}

			in.close();
		}
		catch ( Exception e )
		{

		}

		return output.toString();
	}

	protected void loadProgram()
	{
		String source = loadSource( sourceID );

		if ( source.length() > 0 )
		{
			interp = new PythonInterpreter();

			interp.getSystemState().setClassLoader( new RobotClassLoader() );
			interp.setOut( ostream );

			interp.exec( "import com.shabren.asimovcraft.RobotAPI" );
			interp.exec( "import com.shabren.asimovcraft.RobotAPIMovement" );
			interp.exec( "open = None" );
			interp.exec( "file = None" );
			interp.exec( "execfile = None" );
			interp.exec( "input = None" );
			interp.exec( "raw_input = None" );
			interp.exec( "reload = None" );
			interp.set( "general", apiGeneral );
			interp.set( "movement", apiMovement );
			interp.exec( "print 'Robot activated'" );

			try
			{
				interp.exec( source );

				interp.exec( "print 'Execution complete. Robot deactivated.'" );
			}
			catch ( PyException e )
			{
				robot.sendToOwner( "Robot Exception:" );

				for ( String line : e.toString().split( "\n" ) )
				{
					robot.sendToOwner( line );
				}

				robot.sendToOwner( "Robot Inactive" );
			}
		}
	}

}
