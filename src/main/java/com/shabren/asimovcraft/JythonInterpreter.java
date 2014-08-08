package com.shabren.asimovcraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

import org.python.core.PyCode;
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JythonInterpreter extends Thread
{
	private PythonInterpreter interp = null;
	public RobotAPI api = null;
	private String sourceID;
	private OutputStream ostream;
	public boolean keepRunning = true;

	public JythonInterpreter()
	{
	}

	public void setSource( String sid )
	{
		sourceID = sid;
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
				String source = loadSource( sourceID );

				if ( source.length() > 0 )
				{
					interp = new PythonInterpreter();
					interp.setOut( ostream );

					interp.exec( "import com.shabren.asimovcraft.RobotAPI" );
					interp.set( "robot", api );

					interp.exec( source );
				}
			}

			try
			{
				this.wait();
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
}
