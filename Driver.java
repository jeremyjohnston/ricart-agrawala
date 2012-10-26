import java.io.*;
import java.net.*;
import java.util.*;

public class Driver 
{
	//Writers
	PrintWriter w1;
	PrintWriter w2;
	PrintWriter w3;
	
	//For convenience in accessing channels; will contain our writers above
	ArrayList<PrintWriter> outputStreams;
	
	//Readers that will be passed to a separate thread of execution each
	BufferedReader r1;
	BufferedReader r2;
	BufferedReader r3;
	
	int nodeNum;
	
	// Our mutual exclusion algorithm object for this node
	RicartAgrawala me;
	
	int numberOfWrites;
	int writeLimit = 100; // number of times to try CS
	int csDelay = 1000; //wait delay between CS tries in ms

	/** Start the driver, with a number of channels specified. **/
	public Driver(String args[])
	{
		final boolean desireToHarmHumansOrThroughInactionAllowHumansToComeToHarm = false; //Just in case
		
		//Some defaults
		nodeNum = Integer.parseInt(args[0]);
		//int portNumber = 3009;
		String network2 = "net02.utdallas.edu"; //Default network for one of the other nodes (not necessarily the actual 'node 2')
		String network3 = "net03.utdallas.edu"; //Default network for one of the other nodes (ditto for 3)

		/*if(args.length > 1)
		{
			nodeNum = Integer.parseInt(args[0]);
			portNumber = Integer.parseInt(args[1]);
			
			if (args.length > 2)
			{
				network2 = args[2];
				network3 = args[3];
			}
		}*/

		numberOfWrites = 0;

		// Set up our sockets with our peer nodes
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
			
			//With the sockets done, create our readers and writers
			w1 = new PrintWriter(s1.getOutputStream(), true);
			w2 = new PrintWriter(s2.getOutputStream(), true);
			w3 = new PrintWriter(s3.getOutputStream(), true);
			r1 = new BufferedReader(new InputStreamReader(s1.getInputStream()));
			r2 = new BufferedReader(new InputStreamReader(s2.getInputStream()));
			r3 = new BufferedReader(new InputStreamReader(s3.getInputStream()));			
			
			//VERIFYING SOCKET COMMUNICATION
			w1.println("Writing to socket 1...\n");
			w2.println("Writing to socket 2...\n");
			w3.println("Writing to socket 3...\n");
			
			System.out.println("Wrote to sockets for verification step");
			
			String message1 = r1.readLine();
			
			System.out.println("Read from socket 1: " + message1);
			
			//Let's store our writers in a list
			outputStreams.add(w1);
			outputStreams.add(w2);
			outputStreams.add(w3);
			
			// Create the ME object with priority of 'nodeNum' and initial sequence number 0
			RicartAgrawala me = new RicartAgrawala(nodeNum, 0, this);
			me.w[0] = w1;
			me.w[1] = w2;
			me.w[2] = w3;
			
			
			//And let's start some threads to read our sockets
			Thread t1 = new Thread(new ChannelHandler(s1));
			t1.start();
			
			Thread t2 = new Thread(new ChannelHandler(s2));
			t2.start();
			
			Thread t3 = new Thread(new ChannelHandler(s3));
			t3.start();
			
		}
		catch(Exception ex){ ex.printStackTrace();}

		//Launch thread that occasionally calls requestCS(me) and attempts CS
		Thread tCS = new Thread(new CSHandler());
		tCS.start();
		

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

	/**
	* Interface method between Driver and RicartAgrawala
	*/
	public void requestCS()
	{
		//TODO: possibly needs own thread to execute
		me.invocation();
		
		//After invocation returns, we can safely call CS
		criticalSection(nodeNum, numberOfWrites);
		
		//Once we are done with CS, release CS
		me.releaseCS();
	}

	/**
	* Broadcasts a message to all writers in the outputStreams arraylist.
	* Note this should probably never be used as RicartAgrawala is unicast
	*/
	public void broadcast(String message)
	{
		for(int i = 0; i < outputStreams.size(); i++)
		{
			try
			{
				PrintWriter writer = outputStreams.get(i);
				writer.println(message);
				writer.flush();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	
	
	/**
	* Given a socket, it continuously reads from the 
	* socket and passes key information to the ME object.
	*/
	class ChannelHandler implements Runnable
	{
		BufferedReader reader;
		PrintWriter writer;
		Socket sock;
	
		public ChannelHandler(Socket s)
		{
			try
			{
				sock = s;
				InputStreamReader iReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(iReader);
				
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	
		/** Continuously runs and reads all incoming messages, passing messages to ME */
		// TODO: Possibly move this to RicartAgrawala for module separation
		public void run()
		{
			String message;
			
			try
			{
				//As long as this reader is open, will take action the moment a message arrives.
				while( ( message = reader.readLine() ) != null)
				{
					System.out.println("I, node " + nodeNum + ", received message: " + message);
					
					//Tokenize our message to determine RicartAgrawala step
					
					String tokens[] = message.split(",");
					String messageType = tokens[0];
					
					if(messageType.equals("REQUEST"))
					{
						/*We are receiving request(j,k) where j is a seq# and k a node#.
						  This call will decide to defer or ack with a reply. */
						me.receiveRequest(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
					}
					else if(messageType.equals("REPLY"))
					{
						/* Received a reply. We'll decrement our outstanding replies */
						me.receiveReply();
					}
				}
			
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	class CSHandler implements Runnable
	{
		public CSHandler()
		{
		}
		
		public void run()
		{
			while(numberOfWrites < writeLimit)
			{
				try{
					System.out.println("Requesting critical section...");
					requestCS();
					Thread.sleep(csDelay);
				}
				catch(InterruptedException e){
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	public static void main(String[] args) 
	{
		new Driver(args);	
	}

}