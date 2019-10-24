********************
Classes
********************
Archive:
An archive of all the Generations and all the elite Genomes.
Note: Only an overview of the generations.

Board:
Consists of a two dimensional array of integers (BOARD) that models the board. Includes methods 
for moving and rotating the current piece.

ControllerView:
View element for MyGameModel. Displays behind the scenes data, such as the numeric 
values for the tiles of the board, current score, and number of moves taken.

Coordinator:
Main class that initalises the program. Main purpose is to coordinate the game and the
genetic algorithm.

GameController:
Takes input data from the user and calls the appropiate methods in the model.

GameView:
View element for MyGameModel. Displays the value of the boards in a graphic manner.

Generation:
Convenience class, stores all the genomes of the current generation in an ArrayList.
Also contains methods for getting the fittest individual and the like.

GeneticAlgorithm:
The heart of the algorithm. It's purpose is to wrap all different functions, such as 
evolve(), loadState(), etc., into convenient methods.
	
Genome:
Class for storing all the current weights, fitness, moves taken, etc. 

Move:
Stores the move sequence and the move score.

MyGameModel:
Methods making the game playable, coordinating the rest of the classes relating to
the game itself.

********************
Definitions
********************
Tile - One element of the BOARD array in the Board class.
Current tile - A current tile is defined as the tile id (1-7) times -10 minus the position
Current piece - All currently selected tiles forming a tetromoni
