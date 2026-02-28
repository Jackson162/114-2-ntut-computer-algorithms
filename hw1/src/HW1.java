/* HW1. Battle
 * This file contains two classes :
 * 		- Deck represents a pack of cards,
 * 		- Battle represents a battle game.
 */

import java.util.LinkedList;

class Deck { // represents a pack of cards

	
	LinkedList<Integer> cards;
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
		cards = new LinkedList<Integer>();
	}

	// constructor from field
	Deck(LinkedList<Integer> cards) {
		this.cards = cards;
	}

	// constructor of a complete sorted deck of cards with nbVals values
	Deck(int nbVals) {
		cards = new LinkedList<Integer>();
		for (int j = 1; j <= nbVals; j++)
			for (int i = 0; i < 4; i++)
				cards.add(j);
	}

	// Question 1

	// takes a card from deck d to put it at the end of the current packet
	int pick(Deck d) {
	//	throw new Error("Method pick(Deck d) to complete (Question 1)");
			if (!d.cards.isEmpty()) {
				int x = d.cards.removeFirst();
				cards.addLast(x);
				return x;
			} else {
				return -1;
			}
	}

	// takes all the cards from deck d to put them at the end of the current deck
	void pickAll(Deck d) {
	//	throw new Error("Method pickAll(Deck d) to complete (Question 1)");
		while (!d.cards.isEmpty()) {
			pick(d);
		}
	}

	// checks if the current packet is valid
	boolean isValid(int nbVals) {
	//	throw new Error("Method isValid(int nbVals) to complete (Question 1)");
	int[] numbers = new int[nbVals];
		for (Integer x : cards) {
			if (x < 1 || x > nbVals || numbers	[x - 1] > 3) 
				return false;			
			numbers[x - 1]++;
		}
		return true;
	}

	// Question 2.1

	// chooses a position for the cut
	int cut() {
		throw new Error("Method cut() to complete (Question 2.1)");
	}

	// cuts the current packet in two at the position given by cut()
	Deck split() {
		throw new Error("Method split() to complete (Question 2.1)");
	}

	// Question 2.2

	// mixes the current deck and the deck d
	void riffleWith(Deck d) {
		throw new Error("Method riffleWith(Deck d) to complete (Question 2.2)");
	}

	// Question 2.3

	// shuffles the current deck using the riffle shuffle
	void riffleShuffle(int m) {
		throw new Error("Method riffleShuffle(int m) to complete (Question 2.3)");
	}
}

class Battle { // represents a battle game

	Deck player1;
	Deck player2;
	Deck trick;

	// constructor of a battle without cards
	Battle() {
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
	}
	
	// constructor from fields
	Battle(Deck player1, Deck player2, Deck trick) {
		this.player1 = player1;
		this.player2 = player2;
		this.trick = trick;
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
		throw new Error("Constructor Battle() to complete (Question 3.1)");
	}

	// Question 3.2

	// test if the game is over
	boolean isOver() {
		throw new Error("Method isOver() to complete (Question 3.2)");
	}

	// effectue un tour de jeu
	boolean oneRound() {
		throw new Error("Method oneRound() to complete (Question 3.2)");
	}

	// Question 3.3

	// returns the winner
	int winner() {
		throw new Error("Method winner() to complete (Question 3.3)");
	}

	// plays a game with a fixed maximum number of moves
	int game(int turns) {
		throw new Error("Method game(int turns) to complete (Question 3.3)");
	}

	// Question 4.1

	// plays a game without limit of moves, but with detection of infinite games
	int game() {
		throw new Error("Method game() to complete (Question 4.1)");
	}

	// Question 4.2

	// performs statistics on the number of infinite games
	static void stats(int nbVals, int nbGames) {
		throw new Error("Method stats(int bvVals, int nb_of_games) to complete (Question 4.2)");
	}
}
