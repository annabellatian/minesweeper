=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: phoeniix
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
     I used two 2D arrays to track the state of each cell on the grid. Each cell
     represented by different characters for the various states: covered cell (“O”),
     flag (“F”), flagged mine (“M”), covered mine (“+”), uncovered mine (“*”), and
     number of adjacent mines (“1”..“8”). There is a 2D array called 'board'
     representing the board shown to the user, which is continuously updated as
     the user plays the game. Additionally, there is another 2D array called
     'hiddenBoard' which is the fixed board that is automatically generated every
     time the game is reset. The hidden board is accessed to update the user board.

     This concept is appropriate for the feature because it allows me to easily
     access each cell on the grid and change values, but not the size of the grid
     which should stay fixed for each game. Also, I used type String for my arrays,
     which makes sense as there are several possible states for each cell, and the
     integers of numbered cells can be parsed to integers if I need to access the
     number contained.

  2. Recursion
     I used recursion for the situation where multiple cells are uncovered,
     specifically when the user clicks on a cell with no adjacent mines, cells in all
     directions are uncovered until reaching numbered cells. In this case, I recurse
     through the surrounding cells and uncover if they are numbered or empty, ending
     in the base case where the cell contains a number.

     Recursion is appropriate since I want to mimic the playing turn function on
     surrounding cells, so calling the method within itself is logical. Additionally,
     there is a base case to be reached, so the recursion will not continue forever.

  3. File I/O
     My Minesweeper stores game state using File I/O to allow the user to save and
     resume the game. I include a save and resume game button in the control panel.
     When saved, the user and hidden board 2D array is written to a text file with
     each value delimited by commas. Additionally, the number of mines and flags are
     saved as these values are continually updated during the game rather than
     continuously calculated. When resume is clicked, the text file is read for the
     board to be displayed as well as the hidden board to be stored for the game to
     be continued.

     This is appropriate for saving and resuming games because it directly
     incorporates reading and writing to files. This is fitting as the file will not
     change each time the code is compiled so the user can resume games from the last
     time they played, not just from each session of playing.

  4. JUnit Testable Component
     Since the state of my game is stored in a 2D array, I used JUnit testable
     components to check whether the array is updated correctly when the user clicks
     on cells (the result will depend on the state of the cell). I tested edge cases
     such as when they click on a flagged cell or an already uncovered cell as well
     as different methods like saving and resuming the game and calculating the
     number of mines in adjacent cells.

     This is appropriate for my game since the 2D array is easily testable and
     separable from the GUI of the game. I have playTurn and playFlag methods that
     allow me to mimic the user playing the game with the GUI, making JUnit testing
     a good fit for testing my game's functions.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  My game has two main classes: Minesweeper and GameBoard. GameBoard is more
  of an overarching class for the functions of the game. It handles the control
  panel buttons as well as draws the board and each cell. Minesweeper is more
  specific to the game, implements the specific functions like generating the
  board's contents, playing turns, and playing flags.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  I did not encounter any significant stumbling blocks, although there were
  several changes I had to make to my code to adapt it for JUnit testing. I had
  to add multiple methods to allow the testing to set the value of cells as well
  as remove/change many of the values I had hard-coded before such as board
  height, board width, and number of mines on the board.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I think the separation of functionality became muddled over time as I added many
  getter and setter methods for variables and functions to allow myself to access
  Minesweeper's properties when I created a GameBoard. The private state is
  relatively well encapsulated with getters and setters for all and returning a copy
  of an array rather than the array itself. Given the chance, I would restructure
  the separation of functions to make them more clear, so that I would only need to
  access the Minesweeper behavior from the GameBoard, so I would not need two
  getters and/or setters just to access Minesweeper functions or variables.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
  I used the RandomNumberGenerator and FileLineIterator from the TwitterBot homework.