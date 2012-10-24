import java.io.*;
import java.net.*;
import java.util.*;

public class Driver 
{
	PrintWriter w1;
	PrintWriter w2;
	PrintWriter w3;
	BufferedReader r1;
	BufferedReader r2;
	BufferedReader r3;

	/** Start the driver, with a number of channels specified. **/
	public Driver(String[] args)
	{
		final boolean desireToHarmHumansOrThroughInactionAllowHumansToComeToHarm = false;
		//Just in case
		
		int nodeNum = 1;
		int portNumber = 3009; //Default
		String network2 = "net02.utdallas.edu"; //Default network for one of the other nodes
		String network3 = "net03.utdallas.edu"; //Default network for one of the other nodes

		if(args.length != 0)
		{
			nodeNum = Integer.parseInt(args[0]);
			//portNumber = Integer.parseInt(args[1]);
			
			if (args.length > 2)
			{
				network2 = args[2];
				network3 = args[3];
			}
		}

		int numberOfWrites = 0;

		//RicartAgrawala me = new RicartAgrawala(nodeNum, 0);


		//TODO: Rather than assuming connections and passing number of processes, have processes register with the 0th process

		try
		{
			ServerSocket ss1;
			ServerSocket ss2;
			ServerSocket ss3;
			Socket s1;
			Socket s2;
			Socket s3;	
			
			if(nodeNum == 1)
			{
				System.out.println("Node 1 here");
				ss1 = new ServerSocket(4441); //ServerSocket for net02
				ss2 = new ServerSocket(4442); //ServerSocket for net03
				ss3 = new ServerSocket(4443); //ServerSocket for net04
				s1 = ss1.accept();
				s2 = ss2.accept();
				s3 = ss3.accept();
			}
			else if(nodeNum == 2)
			{
				System.out.println("Node 2 here");
				s1 = new Socket("net01.utdallas.edu", 4441); //ClientSocket for net01
				ss2 = new ServerSocket(4442); //ServerSocket for net03
				ss3 = new ServerSocket(4443); //ServerSocket for net04
				
				s2 = ss2.accept();
				s3 = ss3.accept();
			}
			else if(nodeNum == 3)
			{
				System.out.println("Node 3 here");
				s1 = new Socket("net01.utdallas.edu", 4442); //ClientSocket for net01
				s2 = new Socket("net02.utdallas.edu", 4442); //ClientSocket for net02
				ss3 = new ServerSocket(4443); //ServerSocket for net04
				
				s3 = ss3.accept();
			}
			else
			{
				System.out.println("Node 4 here");
				s1 = new Socket("net01.utdallas.edu", 4443);
				s2 = new Socket("net02.utdallas.edu", 4443);
				s3 = new Socket("net03.utdallas.edu", 4443);
			}
			
			System.out.println("Created all sockets");
			w1 = new PrintWriter(s1.getOutputStream(), true);
			w2 = new PrintWriter(s2.getOutputStream(), true);
			w3 = new PrintWriter(s3.getOutputStream(), true);
			r1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
			r2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
			r3 = new BufferedReader(new InputStreamReader(s3.getInputStream()));			
			
			//TESTING SOCKET COMMUNICATION
			w1.println("Writing to socket 1...\n");
			w2.println("Writing to socket 2...\n");
			w3.println("Writing to socket 3...\n");
			
			System.out.println("Wrote to sockets");
			
			String message1 = r1.readLine();
			
			System.out.println("Read from socket 1: " + message1);
		}
		catch(Exception ex){ ex.printStackTrace();}

		//Launch thread that occasionally calls requestCS(me) and attempts CS
		//Also thread to read channels

	}

	/** Invocation of Critical Section*/
	public static boolean criticalSection(int nodeNum, int numberOfWrites)
	{
		try
		{
			BufferedWriter criticalSection = new BufferedWriter(new FileWriter("CriticalSectionOutput"));
			criticalSection.write("Node" + nodeNum + " has now accessed its critical section " + numberOfWrites + " times.");
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