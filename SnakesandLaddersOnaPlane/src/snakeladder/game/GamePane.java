package snakeladder.game;

import ch.aplu.jgamegrid.*;
import snakeladder.utility.PropertiesLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("serial")
public class GamePane extends GameGrid
{
  private NavigationPane np;
  private static int numberOfPlayers = 1;
  private static int currentPuppetIndex = 0;
  private List<Puppet> puppets =  new ArrayList<>();
  private List<Boolean> playerManualMode;
  private ArrayList<Connection> connections = new ArrayList<Connection>();
  final Location startLocation = new Location(-1, 9);  // outside grid
  final int animationStep = 10;
  public static final int NUMBER_HORIZONTAL_CELLS = 10;
  public static final int NUMBER_VERTICAL_CELLS = 10;
  private final int MAX_PUPPET_SPRITES = 4;

  GamePane(Properties properties)
  {
    setSimulationPeriod(100);
    setBgImagePath("sprites/gamepane_blank.png");
    setCellSize(60);
    setNbHorzCells(NUMBER_HORIZONTAL_CELLS);
    setNbHorzCells(NUMBER_VERTICAL_CELLS);
    doRun();
    createSnakesLadders(properties);
    setupPlayers(properties);
    setBgImagePath("sprites/gamepane_snakeladder.png");
  }

  void setupPlayers(Properties properties) {
    numberOfPlayers = Integer.parseInt(properties.getProperty("players.count"));
    playerManualMode = new ArrayList<>();
    for (int i = 0; i < numberOfPlayers; i++) {
      playerManualMode.add(Boolean.parseBoolean(properties.getProperty("players." + i + ".isAuto")));
    }
    System.out.println("playerManualMode = " + playerManualMode);
  }

  void createSnakesLadders(Properties properties) {
    connections.addAll(PropertiesLoader.loadSnakes(properties));
    connections.addAll(PropertiesLoader.loadLadders(properties));
  }

  void setNavigationPane(NavigationPane np)
  {
    this.np = np;
  }

  void createGui()
  {
    for (int i = 0; i < numberOfPlayers; i++) {
      boolean isAuto = playerManualMode.get(i);
      int spriteImageIndex = i % MAX_PUPPET_SPRITES;
      String puppetImage = "sprites/cat_" + spriteImageIndex + ".gif";

      Puppet puppet = new Puppet(this, np, puppetImage);
      puppet.setAuto(isAuto);
      puppet.setPuppetName("Player " + (i + 1));
      addActor(puppet, startLocation);
      puppets.add(puppet);
    }
  }

  Puppet getPuppet()
  {
    return puppets.get(currentPuppetIndex);
  }
  
  void switchToNextPuppet() {
    currentPuppetIndex = (currentPuppetIndex + 1) % numberOfPlayers;
  }
  
  void switchPuppetTo(int index) {
      currentPuppetIndex = index;
  }

  List<Puppet> getAllPuppets() {
    return puppets;
  }

  void resetAllPuppets() {

    for (Puppet puppet: puppets) {
      puppet.resetToStartingPoint();
    }
  }

  public static int getNumberOfPlayers() {
    return numberOfPlayers;
  }

  Connection getConnectionAt(Location loc)
  {
    for (Connection con : connections)
      if (con.locStart.equals(loc))
        return con;
    return null;
  }

  static Location cellToLocation(int cellIndex)
  {
    int index = cellIndex - 1;  // 0..99

    int tens = index / NUMBER_HORIZONTAL_CELLS;
    int ones = index - tens * NUMBER_HORIZONTAL_CELLS;

    int y = 9 - tens;
    int x;

    if (tens % 2 == 0)     // Cells starting left 01, 21, .. 81
      x = ones;
    else     // Cells starting left 20, 40, .. 100
      x = 9 - ones;

    return new Location(x, y);
  }
  
  int x(int y, Connection con)
  {
    int x0 = toPoint(con.locStart).x;
    int y0 = toPoint(con.locStart).y;
    int x1 = toPoint(con.locEnd).x;
    int y1 = toPoint(con.locEnd).y;
    // Assumption y1 != y0
    double a = (double)(x1 - x0) / (y1 - y0);
    double b = (double)(y1 * x0 - y0 * x1) / (y1 - y0);
    return (int)(a * y + b);
  }
  
  List<Integer> getAllPuppetsCell() {
      List<Integer> cells = new ArrayList<Integer>(); 
      for (int i = 0; i < getNumberOfPlayers(); i++) {
	  cells.add(puppets.get(i).getCellIndex());
      }
      return cells;
    }

  Puppet getPuppetOnCell(int cell) {
      for(Puppet puppet: getAllPuppets()) {
	  if (cell == puppet.getCellIndex() && puppet.getPuppetName() != this.getPuppet().getPuppetName()) {
	      return puppet;
	  }
      }
      return null;
  }
  
  public static int getCurrentPuppetIndex() {
      return currentPuppetIndex;
  }
  
  public void connectionDirectionChange() {
      for(Connection connection: connections) {
	  connection.switchDirection();
      }
  }
  
  public ArrayList<Connection> getAllConnection() {
      return connections;
  }
  
  
  // return true if toggle
  public boolean toggleDecision() {
      Puppet puppet = null;
      int countUp = 0;
      int countDown = 0;
      if (currentPuppetIndex == numberOfPlayers - 1) {
	  puppet = puppets.get(0);
      } else {
	  puppet = puppets.get(currentPuppetIndex + 1);
      }
      int currentIndex = puppet.getCellIndex() + np.getDiceNum();
      for (; currentIndex < puppet.getCellIndex() + np.getDiceNum() * 6 + 1; currentIndex++) {
          for (Connection connection: connections) {
              if (connection instanceof Snake) {
                  if (!np.isToggle()) {
                      if (connection.cellStart == currentIndex) {
                          countDown++;
                      }
                  } else {
                      if (connection.cellStart == currentIndex) {
            	      	  countUp++;
            	      }
                  }
              } else {
        	  if (!np.isToggle()) {
        	      if (connection.cellStart == currentIndex) {
        		  countUp++;
        	      }
        	  } else {
        	      if (connection.cellStart == currentIndex) {
        		  countDown++;
        	      }
        	  }
              }
          }
      }
      if (!np.isToggle()) {
	  System.out.println("no toggle" + puppet.getCellIndex() + "-" + currentIndex + ": " + countUp + "             " + countDown);
      } else {
	  System.out.println("toggle" + puppet.getCellIndex() + "-" + currentIndex + ": " + countUp + "             " + countDown);
      }
      
      if (!np.isToggle()) {
	  if(countUp >= countDown) {
	      return true;
	  } else {
	      return false;
	  }
      } else if (np.isToggle()){
	  if (countUp <= countDown) {
	      return false;
	  } else {
	      return true;
	  }
      }
      
      return np.isToggle();
  }
}
