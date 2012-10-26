ricart-agrawala
===============

Implementation of Ricart-Agrawala's mutual exclusion algorithm.

Group Members: 
David A. Sanders, Jr 
James Sanderlin
Jeremy Johnston

To test our implementation of Ricart-Agrawala's mutual exclusion algorithm you'll need to put Driver.java and RicartAgrawala.java into the directory to be used. Then use 4 terminals to ssh into net01.utdallas.edu, net06.utdallas.edu, net03.utdallas.edu and net04.utdallas.edu. (net02.utdallas.edu was down when we were testing this)

Driver takes 1 argument, the node number. It is important that the arguments be done as below on the machines.

On net01.utdallas.edu:
javac Driver.java
javac RicartAgrawala.java

java Driver 1

On net06.utdallas.edu:
java Driver 2

On net03.utdallas.edu:
java Driver 3

On net04.utdallas.edu:
java Driver 4


After the nodes have finished communicating you can kill the processes with ctrl+c. Each terminal should have a record of defers output to it. The file "CriticalSectionOutput.txt" will have been created.

To verify mutual exclusion, examine the file "CriticalSectionOutput.txt." To verify competition examine the output in the terminal windows. 