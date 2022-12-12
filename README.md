### Minesweeper

This is a version of Minesweeper created in Java. It implements the basic 
rules of the games in addition to the ability to save and resume game progress.
The map changes every time as mine placement is randomly generated. Enjoy!

## Instructions
Minesweeper is a game where mines are hidden in a grid of squares. Safe squares 
have numbers telling you how many mines are in adjacent squares. You can use 
the number clues to solve the game by opening all of the safe squares. If you 
click on a mine you lose the game and all the mines will be revealed! 

To open squares, click with the left mouse button and to place down flags on 
mines, click with the right mouse button. Pressing the right mouse button again 
removes the flag. When you open a square that does not touch any mines, it will 
be empty and the adjacent squares will automatically open in all directions 
until reaching squares that contain numbers. A common strategy for starting 
games is to randomly click until you get a big opening with lots of numbers. 

The status bar shows how many mines remain based on the number of flags placed. 
If a square is flagged, but there is no mine, the status will still update. You 
cannot place more flags down than there are mines on the board - the flag 
function will automatically disable and you will not be able to place down 
another flag until you remove an existing flag. Once you uncover all the squares 
with no mines, you win the game! 

To save your game progress, you can click 'Save Game' in the control panel - 
your game is not automatically saved. To resume the last saved game/game state, 
click on 'Resume Game' in the control panel.
