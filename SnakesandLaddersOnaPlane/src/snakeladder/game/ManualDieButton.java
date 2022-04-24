package snakeladder.game;

import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import snakeladder.game.custom.CustomGGButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class ManualDieButton extends Button implements GGButtonListener {

    ManualDieButton(Properties properties) {
	super(properties);
    }

    @Override
    public void buttonPressed(GGButton ggButton) {

    }

    @Override
    public void buttonReleased(GGButton ggButton) {

    }

    @Override
    public void buttonClicked(GGButton ggButton) {
    	System.out.println("manual die button clicked");
	if(!NavigationPane.getIsGameOver()) {
	    System.out.println("Please change dice number before the game");
	}
	else {
		    
	    if (ggButton instanceof CustomGGButton) {
		CustomGGButton customGGButton = (CustomGGButton) ggButton;
		int tag = customGGButton.getTag();
		System.out.println("manual die button clicked - tag: " + tag);
		properties.setProperty("dice.count", Integer.toString(tag));
	    }
	    rolledRecordChange();
	}
    }
    
    public void rolledRecordChange() {
	    ArrayList<HashMap<Integer, Integer>>rolled = new ArrayList<HashMap<Integer, Integer>> ();
	    for (int i = 0;  i < Integer.parseInt(properties.getProperty("players.count")); i++) {
		rolled.add(new HashMap<Integer, Integer> ());
		for (int j = Integer.parseInt(properties.getProperty("dice.count"));  j < Integer.parseInt(properties.getProperty("dice.count")) * 6 + 1; j++) {
		    rolled.get(i).put(j, 0);
		}
	    }
	    DieController.setRolled(rolled);
    }
}
