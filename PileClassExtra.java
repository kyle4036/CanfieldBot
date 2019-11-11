//Note:
//this is an extra class not apart of the program
//it is actually a part of the canfield class, but I broke its
//own file so that I can easily look at it

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
