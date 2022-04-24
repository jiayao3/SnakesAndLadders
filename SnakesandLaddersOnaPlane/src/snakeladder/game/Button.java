package snakeladder.game;

import ch.aplu.jgamegrid.GGButton;
import ch.aplu.jgamegrid.GGButtonListener;
import java.util.Properties;

public abstract class Button{
    
    Properties properties;
    
    
    Button(Properties properties) {
	this.properties = properties;
    }

}
