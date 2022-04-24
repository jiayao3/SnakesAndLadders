package snakeladder.game;

import java.util.Properties;

import ch.aplu.jgamegrid.GGCheckButton;
import ch.aplu.jgamegrid.GGCheckButtonListener;

public class AutoCheck  extends Button implements GGCheckButtonListener{
    
    AutoCheck(Properties properties) {
	super(properties);
    }
    
    @Override
    public void buttonChecked(GGCheckButton ggCheckButton, boolean checked) {
        NavigationPane.setIsAuto(checked);;
        for (Puppet puppet: GamePane.getAllPuppets()) {
          puppet.setAuto(checked);
        }
    }
}
