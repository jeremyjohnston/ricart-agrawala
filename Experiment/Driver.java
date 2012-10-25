
import java.io.*;
import java.net.*;
import java.util.*;

public class Driver {
	
	ArrayList outputStreams;
	int counter = 0;
	
	public Socket otherSocket;
	public Socket anotherSocket;
	
	
	public int listenPort = 25565;
	public ServerSocket listenSocket;
	public int node;
	
	
	public int totalNodeCount = 3;

	/** Start the driver, with a number of channels specified. **/
	public Driver(int node, int listenPort, String otherIP, int peerPort, String anotherIP, int anotherPort){
		
		outputStreams = new ArrayList();
		this.node = node;
		this.listenPort = listenPort;
		
		System.out.println("Searching for active peers...");
		
		//First we try to connect to any peers given in args,
		//  and we'll launch a thread to read from it
		//  and add a writer to this root thread to write to it
		try
		{
			otherSocket = new Socket(otherIP, peerPort);
			
			PrintWriter writer = new PrintWriter(otherSocket.getOutputStream());
			outputStreams.add(writer);
			
			//This thread will listen and buffer messages
			Thread t = new Thread(new ChannelHandler(otherSocket));
			t.start();
			
			
			/////// ....and again for another peer, our third node....///////
			
			anotherSocket = new Socket(anotherIP, anotherPort);
			
			PrintWriter anotherWriter = new PrintWriter(anotherSocket.getOutputStream());
			outputStreams.add(anotherWriter);
			
			//This thread will listen and buffer messages
			Thread t2 = new Thread(new ChannelHandler(anotherSocket));
			t2.start();
			
			//Beyond these 3 nodes total, could easily read a file of ips to loop through
			//  or even keep a counter and increment to some given number of netXX ips
			//
			//Interestingly enough, in testings the listen port and attempt to connect
			//to peer ports were allowed to be the same? didn't see top mess anything
			//up at all.
			
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		System.out.println("Starting to listen...");
		
		
		//Now we must listen for additional connects.
		//  A connection from a peer we've already connected
		//  to will simply fail, as we already have a connection.
		//
		//  New connections will create a new socket connections.
		try
		{
			listenSocket = new ServerSocket(listenPort);
			
			while(true)
			{
				Socket newChannel = listenSocket.accept();
				
				PrintWriter writer = new PrintWriter(newChannel.getOutputStream());
				outputStreams.add(writer);
				
				Thread t = new Thread(new ChannelHandler(newChannel));
				t.start();
				System.out.println("New connection");
				broadcast("Node " + node + " got a new connection!");
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
	
	public void broadcast(String message)
	{
		Iterator it = outputStreams.iterator();
		
		while(it.hasNext())
		{
			try
			{
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				writer.flush();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	
	
	public class ChannelHandler implements Runnable
	{
		BufferedReader reader;
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
	
		public void run()
		{
			String message;
			
			
		
			try
			{
			
				while( ( message = reader.readLine() ) != null)
				{
					System.out.println("read " + message + " " + counter);
					counter++;
					
					if(counter < 5000)
						broadcast("Node " + node + " got a message at count " + counter);
					
				}
			
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		if(args.length != 6)
		{
			System.out.println("Improper arguments");
		}
		else
		{	//We pass 1. our Node Number, 2. our listen port, 3. a peers IP, and 4. a peers port to Driver
			new Driver(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2], Integer.parseInt(args[3]), args[4], Integer.parseInt(args[5]));
		}
	}

}
