Experiment README
=====================


This was an experiment with sockets and threads to determine
a working way to do peer-to-peer communication in java.

Largely similar to the way sockets were done in a recent
revision to the real program, only args are passed in the following:

On net01:
  > java Driver 1 25565 net02.utdallas.edu 25565 net03.utdallas.edu 25565
  
On net02:
  > java Driver 2 25565 net01.utdallas.edu 25565 net03.utdallas.edu 25565
  
On net03:
  > java Driver 3 25565 net01.utdallas.edu 25565 net02.utdallas.edu 25565
 
So args are: <my Node #> <my listen port> <peer 1 ip> <peer 1 port> <peer 2 ip> <peer 2 port>

---

Output shows when the listen socket receives new connections, and
received messages are broadcasted to all peers, up to a limit, with
a counter to see some form of order of messages.

An interesting note is that this worked despite the listen port and the
port used to connect to peers were the same; I did not notice any error.
However maybe it is safer to use separate ports on each node.

I'm going to merge threading in the real implementation, although
for now it will be as simple as a passing the created readers to their
own thread, such as here a socket was passed to a thread, and a 
reader created.

For the writers, they can be accessed directly, either in their own 
variable or an element in some array.

