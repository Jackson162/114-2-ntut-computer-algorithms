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
  private Player cur_player;
  private int playable_n; 
  private int links[];
  private int ranks[];

  // create an empty board of size n*n
  Hex(int n) {
    this.cur_player = Player.RED;
    this.playable_n = n;
    // col(i) x row(j)
    this.grid = new Player[n+2][n+2];
    for (var i=0; i< n+2; i++) {
      for (var j=0; j< n+2; j++) {
        if ((i == 0 || i == n+1) && j != 0 && j != n+1) {
          grid[i][j] = Player.BLUE;
        } else if ((j == 0 || j == n+1) && i != 0 && i != n+1) {
          grid[i][j] = Player.RED;
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
    if (1 <= i && i <= this.playable_n && 1 <= j && j <= this.playable_n && grid[i][j] == Player.NOONE) {
      this.grid[i][j] = this.cur_player;
      this.cur_player = this.cur_player == Player.RED ? Player.BLUE : Player.RED;
      return true;
    }

    return false;
  }

  // return the player with the trait or Player.NOONE if the game is over
  // because of a player's victory.
  Player currentPlayer() {
    return this.cur_player;
  }
  
  int find(int label) {
    var next = this.links[label];
    if (label == next) return label;

    var root = this.find(next);

    // path compression
    links[label] = root;

    return root;
  }

  void union(int labeli, int labelj) {
    var ri = this.find(labeli);
    var rj = this.find(labelj);

    if (this.ranks[ri] > this.ranks[rj]) {
      this.links[rj] = ri;
    } else if (this.ranks[ri] < this.ranks[rj]) {
      this.links[ri] = rj;
    } else {
      this.links[rj] = ri;
      this.rank[ri] += 1;
    }

  }

  // return the winning player, or Player.NOONE if no player has won yet
  Player winner() {
    var links_size = (this.playable_n+2) * (this.playable_n+2);
    this.links = new int[links_size];
    this.ranks = new int[links_size];

    for (var i = 0;i < links_size; i++) {
      this.links[i] = i;
      this.ranks[i] = 0;
    }


    return Player.NOONE;
  }

  int label(int i, int j) {
    return i + (this.playable_n + 2) * j;
  }


  public static void main(String[] args) {
    HexGUI.createAndShowGUI();
  }
}
