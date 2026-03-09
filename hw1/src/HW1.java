/* HW1. Battle
 * This file contains two classes :
 * 		- Deck represents a pack of cards,
 * 		- Battle represents a battle game.
 */

import java.util.LinkedList;
import java.util.Random;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class Deck { // represents a pack of cards

	
	final LinkedList<Integer> cards;

	private static final Random RANDOM = new Random();
	// The methods toString, hashCode, equals, and copy are used for 
	// display and testing, you should not modify them.

	@Override
	public String toString() {
		return cards.toString();
	}

	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		Deck d = (Deck) o;
		return cards.equals(d.cards);
	}

	Deck copy() {
		Deck d = new Deck();
		for (Integer card : this.cards)
			d.cards.addLast(card);
		return d;
	}

	// constructor of an empty deck
	Deck() {
		this.cards = new LinkedList<>();
	}

	// constructor from field
	Deck(LinkedList<Integer> cards) {
        Objects.requireNonNull(cards, "Initial cards list cannot be null");
        this.cards = new LinkedList<>(cards); 
    }

	// constructor of a complete sorted deck of cards with nbVals values
	Deck(int nbVals) {
        this();
        if (nbVals <= 0) {
            throw new IllegalArgumentException("""
                Invalid number of values: %d.
                nbVals must be strictly greater than 0.
                """.formatted(nbVals));
        }
        
        for (var val = 1; val <= nbVals; val++) {
            for (var color = 0; color < 4; color++) {
                this.cards.add(val);
            }
        }
    }

	// Question 1

	// takes a card from deck d to put it at the end of the current packet
	int pick(Deck d) {
		Objects.requireNonNull(d, "Source deck cannot be null");
        
        var card = d.cards.pollFirst();
        
        if (card != null) {
            this.cards.addLast(card);
            return card;
        }
        return -1;
	}

	// takes all the cards from deck d to put them at the end of the current deck
	void pickAll(Deck d) {
		Objects.requireNonNull(d, "Source deck cannot be null");
        while (!d.cards.isEmpty()) {
            this.pick(d);
        }
	}

	// checks if the current packet is valid
	boolean isValid(int nbVals) {
		if (nbVals <= 0) return false;
        
        var counts = new int[nbVals + 1];
        
        for (var card : this.cards) {
            if (card == null || card < 1 || card > nbVals) {
                return false; 
            }
            if (++counts[card] > 4) {
                return false;
            }
        }
        return true;
	}

	// Question 2.1

	// chooses a position for the cut
	int cut() {
		var n = this.cards.size();
        var head_count = 0;
        
        for (var i = 0; i < n; i++) {
            if (RANDOM.nextBoolean()) {
                head_count++;
            }
        }
        return head_count;
	}

	// cuts the current packet in two at the position given by cut()
	Deck split() {
		var c = cut();
        var newDeck = new Deck();
        
        for (var i = 0; i < c; i++) {
            var card = this.cards.pollFirst();
            if (card != null) {
                newDeck.cards.addLast(card);
            }
        }
        return newDeck;
	}

	// Question 2.2

	// mixes the current deck and the deck d
	void riffleWith(Deck d) {
		Objects.requireNonNull(d, "Deck to riffle with cannot be null");
        
        var f = new Deck();
        
        while (!this.cards.isEmpty() || !d.cards.isEmpty()) {
            var a = this.cards.size();
            var b = d.cards.size();
            
            if (a == 0) {
                f.pickAll(d);
            } else if (b == 0) {
                f.pickAll(this);
            } else {
                // 機率 a / (a + b) 
                var prob = (double) a / (a + b);
                if (RANDOM.nextDouble() < prob) {
                    f.pick(this);
                } else {
                    f.pick(d);
                }
            }
        }
        // 更新當前牌堆 
        this.cards.addAll(f.cards);
	}

	// Question 2.3

	// shuffles the current deck using the riffle shuffle
	void riffleShuffle(int m) {
		for (var i = 0; i < m; i++) {
            var topHalf = this.split();
            this.riffleWith(topHalf);
        }
	}
}


class Battle { // represents a battle game

	Deck player1;
	Deck player2;
	Deck trick;
	boolean turn;

	// constructor of a battle without cards
	Battle() {
		this(new Deck(), new Deck(), new Deck());
	}
	
	// constructor from fields
	Battle(Deck player1, Deck player2, Deck trick) {
        this.player1 = Objects.requireNonNull(player1, "Player 1 deck cannot be null");
        this.player2 = Objects.requireNonNull(player2, "Player 2 deck cannot be null");
        this.trick = Objects.requireNonNull(trick, "Trick deck cannot be null");
        this.turn = true;
    }
	

	// copy the battle
	Battle copy() {
		Battle r = new Battle();
		r.player1 = this.player1.copy();
		r.player2 = this.player2.copy();
		r.trick = this.trick.copy();
		return r;
	}

	// string representing the battle
	@Override
	public String toString() {
		return "Player 1 : " + player1.toString() + "\n" + "Player 2 : " + player2.toString() + "\nPli " + trick.toString();
	}

	// Question 3.1

	// constructor of a battle with a deck of cards of nbVals values
	Battle(int nbVals) {
        this();
        var fullDeck = new Deck(nbVals); // 建立 4 * nbVals 張牌 
		fullDeck.riffleShuffle(7);    // 洗牌 7 次 

        // 交替發牌給兩位玩家 
        var giveToP1 = true;
        while (!fullDeck.cards.isEmpty()) {
            if (giveToP1) {
                this.player1.pick(fullDeck);
            } else {
                this.player2.pick(fullDeck);
            }
            giveToP1 = !giveToP1;
        }
    }

	// Question 3.2

	// test if the game is over
	boolean isOver() {
		return this.player1.cards.isEmpty() || this.player2.cards.isEmpty();
	}

	// effectue un tour de jeu
	// /* 3.2
	boolean oneRound() {
		if (this.player1.cards.isEmpty() || this.player2.cards.isEmpty()) {
			return false;
		}
		int c1 = this.trick.pick(this.player1);
		int c2 = this.trick.pick(this.player2);

		if (c1 > c2) {
			this.player1.pickAll(this.trick);
			return true;
		} else if (c2 > c1) {
			this.player2.pickAll(this.trick);
			return true;
		} else {
			if (this.player1.cards.isEmpty() || this.player2.cards.isEmpty()) {
				return false;
			}

			this.trick.pick(this.player1);
			this.trick.pick(this.player2);

			return this.oneRound();
		}
	}
	//*/
	/* 4.2
	boolean oneRound() {
		if (this.player1.cards.isEmpty() || this.player2.cards.isEmpty()) {
			return false;
		}

		var p1Starts = this.turn;
		this.turn = !this.turn;

		int c1, c2;
		
		if (p1Starts) {
			c1 = this.trick.pick(this.player1);
			c2 = this.trick.pick(this.player2);
		} else {
			c2 = this.trick.pick(this.player2);
			c1 = this.trick.pick(this.player1);
		}

		if (c1 > c2) {
			this.player1.pickAll(this.trick);
			return true;
		} else if (c2 > c1) {
			this.player2.pickAll(this.trick);
			return true;
		} else {
			if (this.player1.cards.isEmpty() || this.player2.cards.isEmpty()) {
				return false;
			}

			var tieStarts = this.turn;
			this.turn = !this.turn; 

			if (tieStarts) {
				this.trick.pick(this.player1);
				this.trick.pick(this.player2);
			} else {
				this.trick.pick(this.player2);
				this.trick.pick(this.player1);
			}

			return this.oneRound();
		}
	}

	//*/
	// Question 3.3

	// returns the winner
	int winner() {
		var s1 = this.player1.cards.size();
        var s2 = this.player2.cards.size();
        if (s1 > s2) return 1;
        if (s2 > s1) return 2;
        return 0; // 平手
	}

	// plays a game with a fixed maximum number of moves
	int game(int turns) {
		for (var i = 0; i < turns; i++) {
            if (!this.oneRound()) break;
        }
        return this.winner();
	}

	// Question 4.1

	// plays a game without limit of moves, but with detection of infinite games
	int game() {
		var turtle = this.copy();
        var hare = this;

        while (true) {
            var h1 = hare.oneRound();
            if (!h1) return hare.winner(); 
            
            var h2 = hare.oneRound();
            var t1 = turtle.oneRound();
            
            if (!h2 || !t1) return hare.winner(); // 遊戲正常結束 

            if (hare.toString().equals(turtle.toString())) {
                return 3; // 無限遊戲 
            }
        }
	}

	// Question 4.2

	// performs statistics on the number of infinite games
	static void stats(int nbVals, int nbGames) {
		var p1Wins = new AtomicInteger(0);
        var p2Wins = new AtomicInteger(0);
        var draws = new AtomicInteger(0);
        var infinites = new AtomicInteger(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (var i = 0; i < nbGames; i++) {
                executor.submit(() -> {
                    var battle = new Battle(nbVals);
                    var result = battle.game();
                    
                    switch (result) {
                        case 0 -> draws.incrementAndGet(); 
                        case 1 -> p1Wins.incrementAndGet(); 
                        case 2 -> p2Wins.incrementAndGet(); 
                        case 3 -> infinites.incrementAndGet(); 
                    }
                });
            }
        } 

        System.out.println("""
            ========================================
            Battle Statistics (%d games, %d values)
            ----------------------------------------
            Player 1 Wins : %d
            Player 2 Wins : %d
            Draws         : %d
            Infinite Games: %d
            ========================================
            """.formatted(nbGames, nbVals, p1Wins.get(), p2Wins.get(), draws.get(), infinites.get()));
    }
}
