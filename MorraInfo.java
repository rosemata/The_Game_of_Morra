import java.io.*;

public class MorraInfo implements Serializable {

	// all data members 
	public static final long serialVersionUID = 1L;
	public int p1Points = 0;
	public int p2Points = 0;
	public int p1PlayFingers = -1;
	public int p2PlayFingers = -1;
	public int p1Guess = -1;
	public int p2Guess = -1;
	public Boolean have2Players = false;
	public int correctGuess = 0;
	public int clientFingerChoose = -1;
	public int clientGuessChoose = -1;
	public Boolean p1Submit = false;
	public Boolean p2Submit = false;
	public Boolean clientToServerSent = false;
	
	// return 1 for client 1 win
	// return 2 for client 2 win
	// return 3 for both win
	// return 4 no win
	public int evalWin(int x1, int x2) {
		correctGuess = x1 + x2;
		if(p1Guess == correctGuess && p2Guess == correctGuess) {
			return 3;
		}
		else if(p1Guess == correctGuess) {
			return 1;
		}
		else if(p2Guess == correctGuess) {
			return 2;
		}
		else {
			return 4;
		}
	}
	
	// returns string who won
	public String announceWin(int x) {
		if(x == 1) {
			p1Points++;
			return "Player 1 win";
		}
		else if(x==2) {
			p2Points++;
			return "Player 2 win";
		}
		else if(x==3) {
			return "Both players guessed right";
		}
		else {
			return "None of players won";
		}
	}
	
	// prints all the current info
	public String info() {
		String temp = announceWin(evalWin(p1PlayFingers,p2PlayFingers));
		return ("\nPlayer 1 Points: " + this.p1Points +
			   "\nPlayer 2 Points: " + this.p2Points +
			   "\nPlayer 1 Finger: " + this.p1PlayFingers +
			   "\nPlayer 1 Guess: " + this.p1Guess +
			   "\nPlayer 2 Finger: " + this.p2PlayFingers +
			   "\nPlayer 2 Guess: " + this.p2Guess +
			   "\n Winner: " + temp +
			   "\nCorrect Guess was: " + this.correctGuess +
			   "\nHas 2 players: " + this.have2Players);
	}
	
	public void resetMorra() {
		p1Points = 0;
		p2Points = 0;
		p1PlayFingers = -1;
		p2PlayFingers = -1;
	    p1Guess = -1;
		p2Guess = -1;
		correctGuess = 0;
		clientFingerChoose = -1;
		clientGuessChoose = -1;
		p1Submit = false;
		p2Submit = false;
		clientToServerSent = false;
	}
	
	
}
