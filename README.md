6.005ps3
========

The purpose of this problem set is to explore multithreaded programming with a shared mutable data type, which 
you should protect using synchronization.

Overview of the pset:
You will start with some minimal server code and implement a server and thread-safe data structure for playing a
multiplayer variant of the classic computer game “Minesweeper.”

You can review the traditional single-player Minesweeper concept rules on Wikipedia: Minesweeper (video game)

You can try playing traditional/single-player Minesweeper here.

Note: You may notice that the implementation in the latter link above does something subtle. It ensures that there’s 
never a bomb where you make your first click of the game. You should not implement this for the assignment. (It would 
be in conflict with giving the option to pass in a pre-designed board, for example.)

The final product will consist of a server and no client; it should be fully playable using the telnet utility to send 
text commands directly over a network connection (see further below).
