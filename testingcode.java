	public static boolean criticalSection(int nodeNum, int numberOfWrites)
	{
		String nodeName = "";
		if(nodeNum == 1)
			nodeName = "P";
		else if (nodeNum == 2)
			nodeName = "Q";
		else if (nodeNum == 3)
			nodeName = "R";		
		else if (nodeNum == 4)
			nodeName = "S";
		else
			nodeName = "Node " + nodeNum;
		
		try
		{
			BufferedWriter criticalSection = new BufferedWriter(new FileWriter("CriticalSectionOutput"));
			
			criticalSection.write(nodeName + " started critical section access");
			Thread.sleep(100);
			//criticalSection.write(nodeName + " has now accessed it's critical section " + numberOfWrites + " times.");
			criticalSection.write(nodeName + " ended critical section access");
			criticalSection.flush(); //flush stream
			criticalSection.close(); //close write
		} 
		catch(IOException e){ System.out.println("Oh No! Something Has Gone Horribly Wrong");}
		return true;
	}
	
	public static void differReport(int nodeNum, int otherNodeNum, int numberOfWrites)
	{
		String nodeName = "";
		if(nodeNum == 1)
			nodeName = "P";
		else if (nodeNum == 2)
			nodeName = "Q";
		else if (nodeNum == 3)
			nodeName = "R";
		else if (nodeNum == 4)
			nodeName = "S";
		else
			nodeName = "Node " + nodeNum;
		
		String otherNodeName = "";
		if(OtherNodeNum == 1)
			otherNodeNum = "P";
		else if (OtherNodeNum == 2)
			otherNodeNum = "Q";
		else if (OtherNodeNum == 3)
			otherNodeNum = "R";
		else if (nodeNum == 4)
			nodeName = "S";
		else
			otherNodeNum = "Node " + nodeNum;
		
		try
		{
			BufferedWriter differRecord = new BufferedWriter(new FileWriter(nodeName +"DiffersTo" + otherNodeName));
			differRecord.write(nodeName + " differed to otherNodeName" + "(Sequence number: " + seqNum + ")");
			differRecord.flush(); //flush stream
			differRecord.close(); //close write
		} 
		catch(IOException e){ System.out.println("Oh No! Something Has Gone Horribly Wrong");}
	}