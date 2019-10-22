/*
Card Class
Author : Kyle Love
Date : 12/9/17
Class that simulates a single card for a deck
*/

import javax.swing.ImageIcon;

public class Card{

	private CardImage cardIcon;

	private String name;
	private String rank;
	private String suit;
	private Rank ENUMRANK;
	private Suit ENUMSUIT;
	private eColor ENUMCOLOR;

	public Card(String name, ImageIcon small, ImageIcon large){
		String[] split;

		cardIcon = new CardImage(small, large);

		this.name = name;
		split = name.split("_");
		this.rank = split[0];
		this.suit = split[1];

		ENUMRANK = Rank.fromString(rank);
		ENUMSUIT = Suit.fromString(suit);
		ENUMCOLOR = eColor.fromSuit(ENUMSUIT);
	}

	public Rank getRank(){
		return ENUMRANK;
	}
	public Suit getSuit(){
		return ENUMSUIT;
	}
	public String getName(){
		return name;
	}
	public eColor getColor(){
		return ENUMCOLOR;
	}
	public String toString(){
		return name;
	}

	public ImageIcon getImageBig(){
		return cardIcon.getLarge();
	}

	public ImageIcon getImageSmall(){
		return cardIcon.getSmall();
	}

	public enum Rank{
		ACE,
		TWO,
		THREE,
		FOUR,
		FIVE,
		SIX,
		SEVEN,
		EIGHT,
		NINE,
		TEN,
		JACK,
		QUEEN,
		KING;

		public static Rank fromString(String r){
			switch(r){
				case "Ace" :
					return ACE;
				case "2" :
					return TWO;
				case "3" :
					return THREE;
				case "4" :
					return FOUR;
				case "5" :
					return FIVE;
				case "6" :
					return SIX;
				case "7" :
					return SEVEN;
				case "8" :
					return EIGHT;
				case "9" :
					return NINE;
				case "10":
					return TEN;
				case "Jack" :
					return JACK;
				case "Queen" :
					return QUEEN;
				case "King" :
					return KING;
				default :
					return null;
			}
		}
	}

	public enum Suit{
		CLUBS,
		SPADES,
		DIAMANDS,
		HEARTS;

		public static Suit fromString(String s){
			switch(s){
				case "Clubs" :
					return CLUBS;
				case "Spades" :
					return SPADES;
				case "Diamonds" :
					return DIAMANDS;
				case "Hearts" :
					return HEARTS;
				default :
					return null;
			}
		}
	}

	public enum eColor{//so I don't run into any potential problems
		RED,
		BLACK;

		public static eColor fromSuit(Suit s){//https://stackoverflow.com/questions/16706716/using-two-values-for-one-switch-case-statement
			switch(s){
				case DIAMANDS:
				case HEARTS:
					return RED;
				case CLUBS:
				case SPADES:
					return BLACK;
				default :
					return null;
			}
		}
	}


	private class CardImage{
		private ImageIcon small;
		private ImageIcon large;

		public CardImage(ImageIcon small, ImageIcon large){
			this.small = small;
			this.large = large;
		}

		public ImageIcon getSmall(){
			return this.small;
		}

		public ImageIcon getLarge(){
			return this.large;
		}
	}
}