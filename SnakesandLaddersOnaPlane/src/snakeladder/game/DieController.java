package snakeladder.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


public class DieController {
    
    private java.util.List<java.util.List<Integer>> dieValues = new ArrayList<>();
    private final int RANDOM_ROLL_TAG = -1;
    private ArrayList<HashMap<Integer, Integer>> rolled = new ArrayList<HashMap<Integer, Integer>> ();
    private Properties properties;
    
    DieController(Properties properties)
    {
      this.properties = properties;
      for (int i = 0;  i < Integer.parseInt(properties.getProperty("players.count")); i++) {
		rolled.add(new HashMap<Integer, Integer> ());
		for (int j = Integer.parseInt(properties.getProperty("dice.count"));  j < Integer.parseInt(properties.getProperty("dice.count")) * 6 + 1; j++) {
		    rolled.get(i).put(j, 0);
		}
      }
    }
   
    //get current die value from total number of rolls
    public int getDieValue() {
	    int rollPerPlayer = getDiceNum();
	    if (dieValues == null) {
	      return RANDOM_ROLL_TAG;
	    }
	    // calculation to find 
	    int currentRound = Math.floorDiv(NavigationPane.getNbRolls(), (GamePane.getNumberOfPlayers() * rollPerPlayer));
	    int playerIndex = Math.floorDiv(NavigationPane.getNbRolls(), rollPerPlayer) % GamePane.getNumberOfPlayers();
	    int currentRoll = NavigationPane.getNbRolls() % rollPerPlayer;
	    int currentMove = currentRound * rollPerPlayer + currentRoll;
	    if (dieValues.get(playerIndex).size() > currentMove) {
		/*
		System.out.println("round: " + currentRound);
		System.out.println("player: " + playerIndex);
		System.out.println(currentMove);
		*/

	      return dieValues.get(playerIndex).get(currentMove);
	    }

	    return RANDOM_ROLL_TAG;
    }
    
    void setupDieValues() {
	    for (int i = 0; i < GamePane.getNumberOfPlayers(); i++) {
	      java.util.List<Integer> dieValuesForPlayer = new ArrayList<>();
	      if (properties.getProperty("die_values." + i) != null) {
	        String dieValuesString = properties.getProperty("die_values." + i);
	        String[] dieValueStrings = dieValuesString.split(",");
	        for (int j = 0; j < dieValueStrings.length; j++) {
	          dieValuesForPlayer.add(Integer.parseInt(dieValueStrings[j]));
	        }
	        dieValues.add(dieValuesForPlayer);
	      } else {
	        System.out.println("All players need to be set a die value for the full testing mode to run. " +
	                "Switching off the full testing mode");
	        dieValues = null;
	        break;
	      }
	    }
	    System.out.println("dieValues = " + dieValues);
    }
    
    public ArrayList<HashMap<Integer, Integer>> getRolled() {
	return rolled;
    }
    
    public int getDiceNum() {
	      return Integer.parseInt(properties.getProperty("dice.count"));
    }
    
    public void rolledRecordChange() {
	    rolled = new ArrayList<HashMap<Integer, Integer>> ();
	    for (int i = 0;  i < Integer.parseInt(properties.getProperty("players.count")); i++) {
		rolled.add(new HashMap<Integer, Integer> ());
		for (int j = Integer.parseInt(properties.getProperty("dice.count"));  j < Integer.parseInt(properties.getProperty("dice.count")) * 6 + 1; j++) {
		    rolled.get(i).put(j, 0);
		}
	    }
    }
    
    public void addRolledRecord(int totalMove) {
	    int currentPuppet = GamePane.getCurrentPuppetIndex();
	    if (rolled.get(currentPuppet).get(totalMove) == null) {
	      rolled.get(currentPuppet).put(totalMove, 1);
	    } else {
	      rolled.get(currentPuppet).put(totalMove, rolled.get(currentPuppet).get(totalMove) + 1);
	    }
    }

}
