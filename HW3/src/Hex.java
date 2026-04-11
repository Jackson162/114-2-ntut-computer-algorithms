/* The Hex game
   https://en.wikipedia.org/wiki/Hex_(board_game)
   desigened by Jean-Christophe Filliâtre

   grid size : n*n

   playable cells : (i,j) with 1 <= i, j <= n

   blue edges (left and right) : i=0 or i=n+1, 1 <= j <= n
    red edges (top and bottom) : 1 <= i <= n, j=0 or j=n+1

      note: the four corners have no color

   adjacence :      i,j-1   i+1,j-1

                 i-1,j    i,j   i+1,j

                    i-1,j+1    i,j+1

*/

public class Hex {

  enum Player {
    NOONE, BLUE, RED
  }

  private Player grid[][];

  // create an empty board of size n*n
  Hex(int n) {
    // row(i) x col(j)
    this.grid = new Player[n+2][n+2];
    for (var i=0; i< n+2; i++) {
      for (var j=0; j< n+2; j++) {
        if (i == 0 || i == n+1) {
          grid[i][j] = Player.RED;
        } else if (j == 0 || i == n+1) {
          grid[i][j] = Player.BLUE;
        } else {
          grid[i][j] = Player.NOONE;        
        }
      }
    }
  }

  // return the color of cell i,j
  Player get(int i, int j) {
    return this.grid[i][j];
  }


  // update the board after the player with the trait plays the cell (i, j).
  // Does nothing if the move is illegal.
  // Returns true if and only if the move is legal.
  boolean click(int i, int j) {
    return false;
  }

  // return the player with the trait or Player.NOONE if the game is over
  // because of a player's victory.
  Player currentPlayer() {
    return Player.NOONE;
  }


  // return the winning player, or Player.NOONE if no player has won yet
  Player winner() {
    return Player.NOONE;
  }

  int label(int i, int j) {
    return 0;
  }


  public static void main(String[] args) {
    HexGUI.createAndShowGUI();
  }
}
