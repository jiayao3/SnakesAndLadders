package snakeladder.game;

import ch.aplu.jgamegrid.GGSound;
import snakeladder.utility.BackgroundDrawing;

public class Ladder extends Connection
{
  public Ladder(int ladderStart, int ladderEnd)
  {
    super(ladderStart, ladderEnd);
    setImagePath(BackgroundDrawing.SPRITES_PATH + "ladder.png");
  }
  public void countTraverse() {
	if (getReverse()) {

	    GamePane.getPuppet().increaseDownCount();
	    System.out.println("downdowndown");
	} else {
	    GamePane.getPuppet().increaseUpCount();
	    System.out.println("upupup");
	}
  }
    
  public String status() {
        return "Climbing...";
  }
    
  public GGSound sound() {
        return GGSound.BOING;
  }
}
