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

		stock = new Pile(up);
		faceDownHand = new Pile(down);
		faceUpHand = new Pile(up);

		foundation = new Pile[4];
		tableau = new Pile[4];
		for (int i = 0;i < 4;i++){
			foundation[i] = new Pile(up);
			tableau[i] = new Pile(up);
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


	private class Pile extends JLabel{
		//todo: make a AddPile method for interchanging tableaus

		private Stack<Card> cardStack;
		private ImageIcon cardFace;
		private boolean faceUp;
		private ImageIcon backFace;
		private ImageIcon noCard;

		public Pile(boolean faceUp){
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
			return cardStack.peek();
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

	}

	public void runGame(){
		Card topCard;

		allPiles = new LinkedList<Pile>();
		allPiles.add(stock);
		allPiles.add(faceUpHand);
		//allPiles.addAll(Arrays.asList(foundation));//shouldn't move foundation cards
		allPiles.addAll(Arrays.asList(tableau));

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

	public boolean checkPiles(){//need another variable to make sure that if the
		boolean noCardsMoved;
		int i=-1;

		do{
			noCardsMoved = checkCardTableau(stock) ||
										   checkCardFoundation(tableau[0]) ||
											 checkCardFoundation(tableau[1]) ||
											 checkCardFoundation(tableau[2]) ||
											 checkCardFoundation(tableau[3]) ||
										 checkCardTableau(faceUpHand) ||
										 checkTableauMove();
			i++;
		}while(noCardsMoved);
		System.out.println("checkPiles() # of Cycles : " + i);

		return (i>=1)?true:false; //checks if it moved cards at least one time
	}
	public boolean checkCardTableau(Pile p){
		boolean cardSwapped = false;

		for(int i = 0;i <= 3;i++){
			Card c = p.peekCard();

			if(this.canCardSwap(c,tableau[i])){
				this.swapCard(p,tableau[i]);
				cardSwapped = true;
			}

		}

		return cardSwapped || checkCardFoundation(p);
	}

	public boolean checkCardFoundation(Pile p){
		//pretty much the same thing as checkCardTableau() except on the foundations
		boolean cardMoved = false;

		for(i = 0;i <= 3;i++){
			Card c = p.peekCard();

			if(this.canCardSwap(c, foundation[i])){
				this.swapCard(p, foundation[i]);
				cardMoved = true;
			}
		}

		return cardMoved;
	}

	public boolean canCardSwap(Card c,Pile p){
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
