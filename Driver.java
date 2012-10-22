
import java.io.*;
import java.net.*;
import java.util.*;

public class Driver 
{

	/** Start the driver, with a number of channels specified. **/
	public Driver(String[] args)
	{
		final boolean desireToHarmHumansOrThroughInactionAllowHumansToComeToHarm = false;
		//Just in case
		String nodeName = "P"; //Default
		int nodeNum = 1;
		int portNumber = 3009; //Default
		String network2 = "net02.utdallas.edu"; //Default network for one of the other nodes
		String network3 = "net03.utdallas.edu"; //Default network for one of the other nodes
		
		if(args.length != 0)
		{
			nodeName = args[0];
			nodeNum = Integer.parseInt(args[1]);
			portNumber = Integer.parseInt(args[2]);
			if (args.length > 2)
			{
				network2 = args[3];
				network3 = args[4];
			}
		}
		
		int numberOfWrites = 0;
		
		RicartAgrawala me = new RicartAgrawala(nodeNum, 0);
		
		
		//TODO: Rather than assuming connections and passing number of processes, have processes register with the 0th process

		try
		{
			Socket s1 = new Socket();
			Socket s2 = new Socket();
			PrintWriter w1 = new PrintWriter(s1.getOutputStream());
			PrintWriter w2 = new PrintWriter(s2.getOutputStream());
			//TODO: similarly for readers	
		}
		catch(Exception ex){ ex.printStackTrace();}
			
		//Launch thread that occasionally calls requestCS(me) and attempts CS
		//Also thread to read channels
		
	}
	
	/** Invocation of Critical Section*/
	public static boolean criticalSection(String nodeName, int numberOfWrites)
	{
		try
		{
			BufferedWriter criticalSection = new BufferedWriter(new FileWriter("CriticalSectionOutput"));
			criticalSection.write(nodeName + " has now accessed it's critical section " + numberOfWrites + " times.");
			criticalSection.flush(); //flush stream
			criticalSection.close(); //close write
		} 
		catch(IOException e){ System.out.println("Oh No! Something Has Gone Horribly Wrong");}
		return true;
	}
	
	public void requestCS(RicartAgrawala me)
	{
		me.invocation();
		//w1.println("REQUEST,"+ me.nodeNum);
	}
	
	public void releaseCS(RicartAgrawala me)
	{
		//w1.println("RELEASE,"+ me.nodeNum);
	}
	
	public static void main(String[] args) 
	{
		new Driver(args);	
	}

}
