package snakeladder.game;

import ch.aplu.jgamegrid.GGSound;
import snakeladder.utility.BackgroundDrawing;
import snakeladder.utility.ServicesRandom;

public class Snake extends Connection
{
   public Snake(int snakeStart, int snakeEnd)
   {
     super(snakeStart, snakeEnd);
     int randomNum = ServicesRandom.get().nextInt(2);
     String [] snakeImages = new String[] { "snake_1.png", "snake_2.png" };
     setImagePath(BackgroundDrawing.SPRITES_PATH + snakeImages[randomNum]);
   }
   
   public boolean getDirection() {
       if (getReverse()) {
	   return true;
       } else {
	   return false;
       }
   }
   
   public void countTraverse() {
 	if (getReverse()) {
 	    GamePane.getPuppet().increaseUpCount();
 	} else {
 	    GamePane.getPuppet().increaseDownCount();
 	}
     }
     
     public String status() {
         return "Digesting...";
     }
     
     public GGSound sound() {
         return GGSound.MMM;
     }
}
