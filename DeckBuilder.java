/*
DeckBuilder Class
Author : Kyle Love
Date : 12/10/17
class that constructs the cards of a deck
	creates an array of cards
*/
import javax.swing.*;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public class DeckBuilder{

	private Card[] cardArray;
	private String[] suits = { "Clubs", "Spades", "Diamonds", "Hearts" };
	private String[] ranks = { "Ace", "2","3","4","5","6","7","8","9","10", "Jack", "Queen", "King" };

	private ImageIcon cardBackSmall;
	private ImageIcon cardBackLarge;

	public DeckBuilder(){
		cardArray = new Card[52];
		cardBackSmall = new ImageIcon("Poker_Small_JPG/Backface_Blue.jpg");
		cardBackLarge = new ImageIcon("Cards/Backface_Blue.jpg");

		for (int s = 0;s < 4;s++){
			for (int r = 0;r < 13;r++){

				int place = s*13 + r;//there are 13 cards in each suit/ensures that I put all the cards in the right place
				String name = ranks[r].concat("_" + suits[s]);

				//System.out.println("\nsuit:	" + s + "\nrank:	" + r + "\nname:	" + name);//so I can debug and ensure all cards are in place

				ImageIcon small = new ImageIcon("Poker_Small_JPG/" + name + ".jpg");
				ImageIcon large = new ImageIcon("Cards/" + name + ".jpg");

				cardArray[place] = new Card(name, small, large);
			}
		}
	}

	public Card[] sendAllCards(){
		return cardArray;
	}

	public void shuffle(){
		List<Card> list = Arrays.asList(cardArray);
		Collections.shuffle(list);
		list.toArray(cardArray);
	}

	public void printCards(){//for debugging purposes
		int i = 0;
		System.out.println("\n");
		for(Card c:cardArray){
			System.out.println("\n" + i + ":" + c.getName());
			i++;
		}
	}

	/*
	public static void main(String[] args){
		DeckBuilder D1 = new DeckBuilder();
		D1.shuffle();
		D1.printCards();
	}*/

}
