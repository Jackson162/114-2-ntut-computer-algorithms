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
import java.util.Vector;


public class Hex {

  enum Player {
    NOONE, BLUE, RED
  }

  private Player grid[][];
  private Player cur_player;
  private int playable_n; 
  private int links[];
  private int ranks[];
  private record adj_link(int idi, int idj){};

  // create an empty board of size n*n
  Hex(int n) {
    this.cur_player = Player.RED;
    this.playable_n = n;
    // col(i) x row(j)
    this.grid = new Player[n+2][n+2];
    for (var i=0; i< n+2; i++) {
      for (var j=0; j< n+2; j++) {
        if ((i == 0 || i == n+1) && j != 0 && j != n+1) {
          grid[j][i] = Player.BLUE;
        } else if ((j == 0 || j == n+1) && i != 0 && i != n+1) {
          grid[j][i] = Player.RED;
        } else {
          grid[j][i] = Player.NOONE;        
        }
      }
    }

  }

  // return the color of cell i,j
  Player get(int i, int j) {
    return this.grid[j][i];
  }


  // update the board after the player with the trait plays the cell (i, j).
  // Does nothing if the move is illegal.
  // Returns true if and only if the move is legal.
  boolean click(int i, int j) {
    if (1 <= i && i <= this.playable_n && 1 <= j && j <= this.playable_n && grid[j][i] == Player.NOONE) {
      this.grid[j][i] = this.cur_player;
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
  
  int find(int id) {
    var next = this.links[id];
    if (id == next) return id;

    var root = this.find(next);

    // path compression
    links[id] = root;

    return root;
  }

  void union(int idi, int idj) {
    var ri = this.find(idi);
    var rj = this.find(idj);

    if (ri == rj) return;

    if (this.ranks[ri] > this.ranks[rj]) {
      this.links[rj] = ri;
    } else if (this.ranks[ri] < this.ranks[rj]) {
      this.links[ri] = rj;
    } else {
      this.links[rj] = ri;
      this.ranks[ri] += 1;
    }

  }

  boolean red_wins(Vector<Integer> uppers, Vector<Integer> lowers) {

    for (var k = 0; k < uppers.size(); k++) {
      var upper_id = this.find(uppers.get(k));
      for (var l = 0; l < lowers.size(); l++) {
        var lower_id = this.find(lowers.get(l));

        if (upper_id == lower_id) return true;
        
      }
    }

    return false;
  }

    boolean blue_wins(Vector<Integer> left, Vector<Integer> right) {

    for (var k = 0; k < left.size(); k++) {
      var left_id = this.find(left.get(k));
      for (var l = 0; l < right.size(); l++) {
        var right_id = this.find(right.get(l));

        if (left_id == right_id) return true;
        
      }
    }

    return false;
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

    var adj_links = new Vector<adj_link>();

    for (var i = 0; i < this.playable_n+2; i++) {
      for (var j = 0; j < this.playable_n+2; j++) {
        var cur = this.grid[j][i];
        var cur_id = this.getID(i, j);
        if (cur == Player.NOONE) continue;
        // check top left grid
        if (j-1 >= 0) {
          if (cur == this.grid[j-1][i]) adj_links.add(new adj_link(cur_id, this.getID(i, j-1)));
        }

        // check left grid
        if (i-1 >= 0) {
          if (cur == this.grid[j][i-1]) adj_links.add(new adj_link(cur_id, this.getID(i-1, j)));
        }

        // check down left grid
        if (i-1 >= 0 && j+1 < this.playable_n+2) {
          if (cur == this.grid[j+1][i-1]) adj_links.add(new adj_link(cur_id, this.getID(i-1, j+1)));
        }

        // check up right grid
        if (j-1 >= 0 && i+1 < this.playable_n+2) {
          if (cur == this.grid[j-1][i+1]) adj_links.add(new adj_link(cur_id, this.getID(i+1, j-1)));
        }

        // check right grid
        if (i+1 < this.playable_n+2) {
          if (cur == this.grid[j][i+1]) adj_links.add(new adj_link(cur_id, this.getID(i+1, j)));
        }
        
        // check right down grid
        if (j+1 < this.playable_n+2) {
          if (cur == this.grid[j+1][i]) adj_links.add(new adj_link(cur_id, this.getID(i, j+1)));    
        }
      }
    }

    // red
    var uppers = new Vector<Integer>();
    var lowers = new Vector<Integer>();

    for (var i = 1; i < this.playable_n+1; i++) {
      uppers.add(this.getID(i, 0));
      lowers.add(this.getID(i, this.playable_n+1));
    }

    // blue
    var left = new Vector<Integer>();
    var right = new Vector<Integer>();

    for (var j = 1; j < this.playable_n+1; j++) {
      left.add(this.getID(0, j));
      right.add(this.getID(this.playable_n+1, j));
    }

    // skip randomization: check if there is a path after every union will get conclusion faster

    for (var k =0; k < adj_links.size(); k++) {
      var idi = adj_links.get(k).idi();
      var idj = adj_links.get(k).idj();
      union(idi, idj);

      if (this.red_wins(uppers, lowers)) {
        this.cur_player =  Player.NOONE;
        return Player.RED;
      } else if (this.blue_wins(left, right)) {
        this.cur_player =  Player.NOONE;
        return Player.BLUE;      
      }
    }


    return Player.NOONE;
  }

  int getID(int i, int j) {
    return i + (this.playable_n + 2) * j; 
  }

  int label(int i, int j) {
    int id = this.getID(i, j);
    return this.find(id);
  }


  public static void main(String[] args) {
    HexGUI.createAndShowGUI();
  }
}
