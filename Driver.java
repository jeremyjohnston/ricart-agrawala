
import java.io.*;
import java.net.*;
import java.util.*;

public class Driver {
	
	public ArrayList<Socket> sockets = new ArrayList<Socket>();
	
	public ArrayList<PrintWriter> writers = new ArrayList<PrintWriter>();
	
	public ArrayList<InputStreamReader> streamReaders = new ArrayList<InputStreamReader>();
	public ArrayList<BufferedReader> readers = new ArrayList<BufferedReader>();
	
	//TODO: Maybe find some means of not using predetermined addresses
	// A fixed number of ip addresses
	public ArrayList<String> ipAddresses = new ArrayList<String>();
	
	// and ports
	public ArrayList<Integer> ports = new ArrayList<Integer>();
	
	// Just some arbitrary fixed count for now
	public int totalNodeCount = 5;

	/** Start the driver, with a number of channels specified. **/
	public Driver(int nodeAssignment){
		
		RicartAgrawala me = new RicartAgrawala(nodeAssignment, 0);
		
		//create N channels, 1 to each other process
		//TODO: Rather than assuming connections and passing number of processes, have processes register with the 0th process
		for(int i = 0; i < totalNodeCount; i++){
			
			if(i == nodeAssignment)
				continue;
			
			
			try{
				Socket s = new Socket(ipAddresses.get(i), ports.get(i));
				sockets.add(s);
				
				PrintWriter w = new PrintWriter(s.getOutputStream());
				writers.add(w);
				
				//TODO: similarly for readers
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		
		
		
		//Launch thread that occasionally calls requestCS(me) and attempts CS
		//Also thread to read channels
		
	}
	
	/** Invocation of Critical Section*/
	public void requestCS(RicartAgrawala me){
		me.invocation();
		
		for(PrintWriter w: writers){
			w.println("REQUEST,"+ me.nodeNum);
		}
	}
	
	public void releaseCS(RicartAgrawala me){
		for(PrintWriter w: writers){
			w.println("RELEASE,"+ me.nodeNum);
		}
	}
	
	public static void main(String[] args) {
		new Driver(Integer.parseInt(args[0]));
		
	}

}
