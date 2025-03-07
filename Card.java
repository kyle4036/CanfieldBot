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

	//overloaded constructor
	public Card(Rank enumrank, Suit enumsuit){
		ENUMRANK = enumrank;
		ENUMSUIT = enumsuit;
		ENUMCOLOR = eColor.fromSuit(ENUMSUIT);
	}
	public Card(Rank enumrank, Suit enumsuit,eColor enumcolor,Object o){
		ENUMRANK = enumrank;
		ENUMSUIT = enumsuit;
		ENUMCOLOR = enumcolor;
	}

	public Rank getRank(){
		return ENUMRANK;
	}
	public Rank getNextRank(){
		return Rank.nextRank(ENUMRANK);
	}
	public Rank getPrevRank(){
		return Rank.prevRank(ENUMRANK);

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
	public eColor getOppColor(){
		if(ENUMCOLOR == eColor.BLACK)
			return eColor.RED;
		return eColor.BLACK;
	}
	public String toString(){
		return name;
	}
	public boolean equals(Card c){
		if(this.ENUMRANK == c.getRank() && this.ENUMSUIT == c.getSuit())
			 	return true;
		return false;
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
		public static Rank nextRank(Rank r){
			switch(r){
				case ACE :
					return TWO;
				case TWO :
					return THREE;
				case THREE :
					return FOUR;
				case FOUR :
					return FIVE;
				case FIVE :
					return SIX;
				case SIX :
					return SEVEN;
				case SEVEN :
					return EIGHT;
				case EIGHT :
					return NINE;
				case NINE:
					return TEN;
				case TEN:
					return JACK;
				case JACK :
					return QUEEN;
				case QUEEN :
					return KING;
				case KING :
					return ACE;
				default :
					return null;
				}
			}
			public static Rank prevRank(Rank r){
				switch(r){
					case ACE :
						return KING;
					case TWO :
						return ACE;
					case THREE :
						return TWO;
					case FOUR :
						return THREE;
					case FIVE :
						return FOUR;
					case SIX :
						return FIVE;
					case SEVEN :
						return SIX;
					case EIGHT :
						return SEVEN;
					case NINE:
						return EIGHT;
					case TEN:
						return NINE;
					case JACK :
						return TEN;
					case QUEEN :
						return JACK;
					case KING :
						return QUEEN;
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
		public static Suit[] fromColor(eColor c){
			if(c == eColor.RED)
				return new Suit[]{DIAMANDS, HEARTS};
			else
				return new Suit[]{CLUBS,SPADES};
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
