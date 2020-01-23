/*
Canfield Class
Author : Kyle Love
Date : 10/18/2019
Class that organises and solves a solitare game named canfield
Look at the CanfieldClassFlowChart image to better understand the workflow behind the program

version 1.2 - Starting this project independently away from the Compsci class I took like 2 years ago

	I need to create the canCardSwap, swapCard, and adjustTableau methods.
	canCardSwap will run the swapCard method to swap the cards immediately after it finds that it can swap the card with another spot
*/

//xx

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.*;
import java.util.*;

public class Canfield extends JFrame{

	private DeckBuilder db;
	private List<Card> originalCardList;
	private Stack<Card> originalCardStack;

	private Pile stock;
	private Pile faceDownHand;
	private Pile faceUpHand;

	private Pile[] foundation;
	private Pile[] tableau;

	private List<Pile> allPiles;

	private JPanel mainPanel;
	private JPanel stockPanel;
	private JPanel foundationPanel;
	private JPanel tableauPanel;
	private JPanel handPanel;
	private JPanel rightPanel;
	private JPanel leftPanel;

	private final Color BACKGROUND = Color.green.darker();

	private final int WIDTH = 350;
	private final int HEIGHT = 250;

	public Canfield(){
		this.createDeck();
		this.createPiles();
		this.setUpPiles();
		this.createPanels();
		this.organizePanels();

		this.runGame();
	}

	private void createDeck(){
		db = new DeckBuilder();
		db.shuffle();
		originalCardList = Arrays.asList(db.sendAllCards());
		originalCardStack = new Stack<Card>();
		originalCardStack.addAll(originalCardList);
	}
	private void createPiles(){
		boolean up = true;
		boolean down = false;

		//anonymouse class to act as an interface function
		FollowingCardIntr FCfoundation = new FollowingCardIntr(){
			public Card[] nextCard(Pile p){
				//following card needs to be going upwards and in the same suit
				Card[] nextCards;

				if(p.empty()){
				  Card tempCard = foundation[0].firstCard();
				  Card.Rank firstR = tempCard.getRank();
				  nextCards = new Card[]{
				    new Card(firstR, Card.Suit.CLUBS),
				    new Card(firstR, Card.Suit.SPADES),
				    new Card(firstR, Card.Suit.DIAMANDS),
				    new Card(firstR, Card.Suit.HEARTS)};
					return nextCards;
				}

				Card tempCard = p.peekCard();
				Card.Rank nextR = tempCard.getNextRank();
				nextCards = new Card[]{ new Card(nextR, tempCard.getSuit())};
				return nextCards;
			}
		};

		//anonymouse class to act as an interface function
		FollowingCardIntr FCtableau = new FollowingCardIntr(){
			public Card[] nextCard(Pile p){
				//Following cards need to be going downwards and be opposing colors
				Card[] nextCards;

				Card tempCard = p.peekCard();
				Card.Rank prevR = tempCard.getPrevRank();
				Card.eColor opColor = tempCard.getOppColor();
				Card.Suit[] nextSuits = Card.Suit.fromColor(opColor);

				nextCards = new Card[]{
				  new Card(prevR,nextSuits[0]),
				  new Card(prevR,nextSuits[1])};

				return nextCards;
			}
		};

		stock = new Pile(up,"stock");
		faceDownHand = new Pile(down,"faceDownHand");
		faceUpHand = new Pile(up,"faceUpHand");

		foundation = new Pile[4];
		tableau = new Pile[4];
		for (int i = 0;i < 4;i++){
			foundation[i] = new Pile(up,"foundation"+i, FCfoundation);
			tableau[i] = new Pile(up,"tableau"+i, FCtableau);
		}
	}
	private void setUpPiles(){
		Card c;

		for (int i = 0;i < 13;i++){
			c = originalCardStack.pop();
			stock.addCard(c);
			System.out.println("#" + i + " stock:\t" + c);// for debugging purposes
		}

		c = originalCardStack.pop();
		foundation[0].addCard(c);
		System.out.println("#1 foundation:\t" + c);

		for (int i = 0;i < 4;i++){
			c = originalCardStack.pop();
			tableau[i].addCard(c);
			System.out.println("#" + i + " tableau:\t" + c);
		}

		int i = 0;

		while (!originalCardStack.empty()){
			c = originalCardStack.pop();
			faceDownHand.addCard(c);
			System.out.println("#" + i++ + " HandDown:\t" + c);
		}
	}
	private void createPanels(){
		FlowLayout defaultFlow = new FlowLayout(FlowLayout.LEFT, 4,4);
		BorderLayout defaultBorder = new BorderLayout(4,4);


		mainPanel = new JPanel(defaultFlow);
		mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		mainPanel.setBackground(BACKGROUND);

		stockPanel = new JPanel();
		stockPanel.add(stock);
		stockPanel.setBackground(BACKGROUND);
		stockPanel.setVisible(true);

		foundationPanel = new JPanel(defaultFlow);
		for (int i = 0;i < 4;i++)
			foundationPanel.add(foundation[i]);
		foundationPanel.setBackground(BACKGROUND);
		foundationPanel.setVisible(true);

		tableauPanel = new JPanel(defaultFlow);
		for (int i = 0;i < 4;i++)
			tableauPanel.add(tableau[i]);
		tableauPanel.setBackground(BACKGROUND);
		tableauPanel.setVisible(true);

		handPanel = new JPanel();
		handPanel.setLayout(new BoxLayout(handPanel, BoxLayout.Y_AXIS));
		handPanel.add(faceDownHand);
		handPanel.add(Box.createRigidArea(new Dimension(0,4)));
		handPanel.add(faceUpHand);
		handPanel.setBackground(BACKGROUND);
		handPanel.setVisible(true);

		rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBackground(BACKGROUND);
		rightPanel.setVisible(true);

		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.setBackground(BACKGROUND);
		leftPanel.setVisible(true);
	}
	private void organizePanels(){
		rightPanel.add(foundationPanel);
		rightPanel.add(tableauPanel);

		leftPanel.add(stockPanel);
		leftPanel.add(handPanel);

		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);

		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.add(mainPanel);
		super.pack();
		super.setVisible(true);
	}

	interface FollowingCardIntr{
		public Card[] nextCard(Pile p);
	}
	private class Pile extends JLabel{
		//todo: make a AddPile method for interchanging tableaus

		private String name;

		private Stack<Card> cardStack;
		private ImageIcon cardFace;
		private boolean faceUp;
		private ImageIcon backFace;
		private ImageIcon noCard;

		private FollowingCardIntr followingCardFunc;

		public Pile(boolean faceUp, String name){
			this.name = name;
			this.faceUp = faceUp;
			cardStack = new Stack<Card>();

			this.setText(null);

			backFace = new ImageIcon("Poker_Small_JPG/Backface_Blue.jpg");//start off the game with all cards face down so it can easily be formatted
			noCard = new ImageIcon("invisible_Card.jpg");
			cardFace = noCard;

			super.setIcon(cardFace);
			super.setText(null);
			super.setVisible(true);
		}

		//overloaded constructor for the tableau and foundation piles
		public Pile(boolean faceUp,String name, FollowingCardIntr funct){
			this.name = name;
			this.faceUp = faceUp;
			cardStack = new Stack<Card>();

			this.setText(null);

			backFace = new ImageIcon("Poker_Small_JPG/Backface_Blue.jpg");//start off the game with all cards face down so it can easily be formatted
			noCard = new ImageIcon("invisible_Card.jpg");
			cardFace = noCard;

			super.setIcon(cardFace);
			super.setText(null);
			super.setVisible(true);

			this.followingCardFunc = funct;
		}

		public void addCard(Card c){
			cardStack.push(c);
			this.updateVisual();
		}

		public Card popCard(){
			Card c = cardStack.pop();
			this.updateVisual();
			return c;
		}

		public Card peekCard(){
			this.updateVisual();
			try{
				return cardStack.peek();
			}catch(EmptyStackException e){
				System.out.println("EmptyStackException in Piles.peekCard() method.\n"+
													 "Likely due to a tableau running out of cards.");
				return new Card(null,null,null,null);
			}
		}

		public Card firstCard(){
			this.updateVisual();
			return cardStack.firstElement();
		}

		public boolean empty(){
			return cardStack.empty();
		}

		public void updateVisual(){
			if (cardStack.empty()){
				super.setIcon(null);
				return;
			}

			super.setVisible(true);

			if (faceUp)
				super.setIcon(cardStack.peek().getImageSmall());
			else
				super.setIcon(backFace);
		}

		public Card[] followingCard(){
			return followingCardFunc.nextCard(this);
		}

		public String toString(){
			String output = name + ":";
			for(Card c:cardStack){
				output = output + "\n" + c;
			}
			return output+"\n";
		}
	}

	public void runGame(){
		System.out.println("enter:runGame()");

		Card topCard;

		allPiles = new LinkedList<Pile>();
		allPiles.add(stock);
		allPiles.add(faceUpHand);
		//allPiles.addAll(Arrays.asList(foundation));//shouldn't move foundation cards
		allPiles.addAll(Arrays.asList(tableau));
		allPiles.addAll(Arrays.asList(foundation));

		//this.printAllPiles();//debugging

		int count,i;
		i = count = 0;
		while (i < 3){

			if (!faceDownHand.empty()){
				topCard = this.flip3Cards();
				System.out.println("fliping over three cards");
			}
			else{
				this.flipHand();
				System.out.println("turning over the hand pile");
				topCard = this.flip3Cards();
				i++;
			}

			System.out.println("#" + count++ + " top card is " + topCard);

			if (this.checkPiles())
				i = 0;

			this.slow();
		}
		System.out.println("exit:runGame()");
	}

	public Card flip3Cards(){
		Card c;

		for (int i = 0;i < 3 && !faceDownHand.empty();i++){
			c = faceDownHand.popCard();
			faceUpHand.addCard(c);
			//System.out.println("
		}
		return faceUpHand.peekCard();
	}

	public void flipHand(){
		while (!faceUpHand.empty())
			faceDownHand.addCard(faceUpHand.popCard());
	}

	public boolean checkPiles(){
		System.out.println("enter:checkPiles()");
		boolean noCardsMoved;
		int i=-1;

		do{
			noCardsMoved = checkCardTableau(stock) ||
										 tableauToFoundation() ||
										 checkCardTableau(faceUpHand) ||
										 checkTableauMove();
			i++;
		}while(noCardsMoved);
		System.out.println("checkPiles() # of Cycles : " + i);
		System.out.println("exit:checkPiles()");
		return (i>=1)?true:false; //checks if it moved cards at least one time
	}
	public boolean checkCardTableau(Pile p){
		System.out.println("enter:checkCardTableau()");
		boolean cardSwapped = false;

		for(int i = 0;i <= 3;i++){
			//need to make sure that if a tableau is currently empty to
			// add a card from the stock or the deck
			if(tableau[i].empty() && !stock.empty()){
				this.swapCard(stock,tableau[i]);
				cardSwapped = true;
				//System.out.println("checkCardTableau(Pile p):tableau empty");
			}
			else if(tableau[i].empty() && stock.empty() && !faceUpHand.empty()){
				this.swapCard(faceUpHand, tableau[i]);
				cardSwapped = true;
				//System.out.println("checkCardTableau(Pile p):tableau and stock empty");
			}
			else if(tableau[i].empty() && stock.empty() && faceUpHand.empty()){
				this.flip3Cards();
				this.swapCard(faceUpHand, tableau[i]);
				cardSwapped = true;
				System.out.println("checkCardTableau(Pile p):tableau and stock and uphand deck empty");
			}
		}

		for(int i = 0;i <= 3;i++){
			//checks if a card can be added to a tableau and does so
			Card c = p.peekCard();
			if(this.canCardSwap(c,tableau[i])){
				this.swapCard(p,tableau[i]);
				cardSwapped = true;
				System.out.println("checkCardTableau(Pile p):card " + c + "swapped with tableau #" + i);
			}
		}
		System.out.println("exit:checkCardTableau()");


		return cardSwapped || checkCardFoundation(p);
	}

	public boolean checkCardFoundation(Pile p){
		//pretty much the same thing as checkCardTableau() except on the foundations
		//System.out.println("enter:checkCardFoundation(Pile p)");
		boolean cardMoved = false;

		for(int i = 0;i <= 3;i++){
			Card c = p.peekCard();

			if(this.canCardSwap(c, foundation[i])){
				this.swapCard(p, foundation[i]);
				cardMoved = true;
				System.out.println("checkCardFoundation(Pile p):card " + c + " swapped with foundation #" + i);
			}
		}
			//System.out.println("exit:checkCardFoundation(Pile p)");
		return cardMoved;
	}

	public boolean tableauToFoundation(){
		System.out.println("enter:tableauToFoundation()");
		boolean cardMoved = false;
		for(int i = 0;i <= 3;i++){
			if(!tableau[i].empty()){
				System.out.println("tableau #" + i + " is not empty");
				System.out.println(tableau[i]);
				cardMoved = this.checkCardFoundation(tableau[i]);
			}
		}
		return cardMoved;
	}

	public boolean canCardSwap(Card c,Pile p){
		/*
		if(p.empty())
			return false;//so we don't throw any exceptions
		*/
		//breaks things
		//using tableauToFoundation to fix things

		Card[] playableCards;
		playableCards = p.followingCard();

		for(Card test:playableCards){
			if(test.equals(c)){
				return true;
			}
		}
		return false;
	}
	public void swapCard(Pile p, Pile s){
		//after canCardSwap decides whether it can swap a card, it will run this method to swap the two cards
		s.addCard(p.popCard());
	}

	public boolean checkTableauMove(){
		//used to move a Tableau pile onto another tableau if needed
		return false;
	}

	public void printAllPiles(){
		ListIterator<Pile> iterator = allPiles.listIterator();
		while (iterator.hasNext())
			System.out.println(iterator.next());
	}

	public void slow(){
		try
		{
		    Thread.sleep(100);
		}
		catch(InterruptedException ex)
		{
		    Thread.currentThread().interrupt();
		}
	}

	public static void main(String[] args){
		new Canfield();
	}
}
