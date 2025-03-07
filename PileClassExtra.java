//Note:
//this is an extra class not apart of the program
//it is actually a part of the canfield class, but I broke its
//own file so that I can easily look at it

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
    return cardStack.peek();
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
