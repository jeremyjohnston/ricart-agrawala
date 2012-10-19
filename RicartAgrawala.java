
//TODO: Perhaps make runnable to run in a thread

public class RicartAgrawala {

	public boolean bRequestingCS;
	public int outstandingReplies;
	public int highestSeqNum;
	public int seqNum;
	public int nodeNum;
	public int channelCount; // 1 channel per other channel; our 'N'
	public boolean[] replyDeferred;
	
	public RicartAgrawala(int nodeNum, int seqNum){
		bRequestingCS = false;
		
		// TODO: 0 for now; needs to be number of channels minus one
		outstandingReplies = channelCount;
		
		highestSeqNum = 0;
		this.seqNum = seqNum;
		
		// TODO: Need some way to communicate to all processes their unique priority, or node, number
		this.nodeNum = nodeNum;
		
		replyDeferred = new boolean[channelCount];
	}
	
	/** Invocation (begun in driver module with request CS) */
	public void invocation(){
		bRequestingCS = true;
		seqNum = highestSeqNum + 1;
		outstandingReplies = channelCount;
		
		for(int i = 0; i < channelCount; i++){
			if(i != nodeNum){
				//send(REQUEST(seqNum, nodeNum), i);
			}
		}
		
		
		while(outstandingReplies > 0){/*wait until we have replies from all other processes */}
	
		//TODO: Critical Section here
		
		//release CS
		
		bRequestingCS = false;
		
		for(int i = 0; i < channelCount; i++){
			if(replyDeferred[i]){
				replyDeferred[i] = false;
				//send(REPLY, i);
			}
		}
	}
	
	/** Receiving Request 
	 * 
	 *	@param	j	The incoming message's sequence number
	 *	@param	k	The incoming message's node number 
	 * 
	 */
	public void receiveRequest(int j, int k){
		boolean bDefer = false;
		
		highestSeqNum = Math.max(highestSeqNum, j);
		bDefer = bRequestingCS && ((j > seqNum) || (j == seqNum && k > nodeNum));
		
		if(bDefer){
			//TODO: Implement a structure to track deferred messages
			replyDeferred[k] = true;
		}
		else{
			//TODO: Create a means to handle incoming messages and parse the message type, to then send to the right method.     
			//send(REPLY, k);
		}
		
	}
	
	/** Receiving Replies */
	public void receiveReply(){
		outstandingReplies = Math.max((outstandingReplies - 1), 0);
	}

}
