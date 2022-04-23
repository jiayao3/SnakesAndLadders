package snakeladder.game;

import java.util.ArrayList;
import java.util.HashMap;

public class StatisticsPrinter {
    
    public void printStat(ArrayList<HashMap<Integer, Integer>> rolled) {
	      for (int i = 0; i < rolled.size(); i++) {
		  System.out.print("Player " + (i + 1) + " rolled: ");
		  int j = 0;
		  for (Integer roll : rolled.get(i).keySet()) {
		        if (j != 0) {
		            System.out.print(", ");
		        }
			System.out.print(roll + "-" + rolled.get(i).get(roll));
			j++;
		  }
		  System.out.println("");
	      }

	  }
	  
    public void printTraverse() {

	for (Puppet puppet: GamePane.getAllPuppets()) {
		  int up = puppet.getUpCount();
		  int down = puppet.getDownCount();

		  System.out.print(puppet.getPuppetName() + " traversed: ");
		  System.out.println("up-" + up + ", " + "down-" + down);
	      }
	      
	  }
    
}
