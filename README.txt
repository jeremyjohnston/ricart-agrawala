ricart-agrawala
===============


Implementation of Ricart-Agrawala's mutual exclusion algorithm.

Current state of the program is uncompiled and untested. Needs a means
of determining number of and coordinating an arbitrary amount
of processes, perhaps by all processes registering with a single
agreed-upon ip.

This would ease the work of the second task; that of creating
some easy testing mechanism. Perhaps a Unix bash script file
to launch processes given a desired number of processes.

The processes should also somehow log times that critical sections
were entered (both logical and physical times), to show correctness
of the program.


