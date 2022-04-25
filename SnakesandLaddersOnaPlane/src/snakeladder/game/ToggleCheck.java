package snakeladder.game;

import java.util.Properties;

import ch.aplu.jgamegrid.GGCheckButton;
import ch.aplu.jgamegrid.GGCheckButtonListener;

public class ToggleCheck extends Button implements GGCheckButtonListener{

    ToggleCheck(Properties properties) {
	super(properties);
    }
    
    @Override
    public void buttonChecked(GGCheckButton ggCheckButton, boolean checked) {
      NavigationPane.setIsToggle(checked);
      GamePane.connectionDirectionChange();
    }
    
}
