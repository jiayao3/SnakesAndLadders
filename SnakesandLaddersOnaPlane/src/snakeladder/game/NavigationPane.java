package snakeladder.game;

import ch.aplu.jgamegrid.*;
import java.awt.*;
import ch.aplu.util.*;
import snakeladder.game.custom.CustomGGButton;
import snakeladder.utility.ServicesRandom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

@SuppressWarnings("serial")
public class NavigationPane extends GameGrid
  implements GGButtonListener
{
  private class SimulatedPlayer extends Thread
  {
    public void run()
    {
      while (true)
      {
	if (!isGameOver) {
	    Monitor.putSleep();
	    handBtn.show(1);
	    rolling();
	    handBtn.show(0);
	}

      }
    }

  }

  private final int DIE1_BUTTON_TAG = 1;
  private final int DIE2_BUTTON_TAG = 2;
  private final int DIE3_BUTTON_TAG = 3;
  private final int DIE4_BUTTON_TAG = 4;
  private final int DIE5_BUTTON_TAG = 5;
  private final int DIE6_BUTTON_TAG = 6;
  private final int RANDOM_ROLL_TAG = -1;

  private final Location handBtnLocation = new Location(110, 70);
  private final Location dieBoardLocation = new Location(100, 180);
  private final Location pipsLocation = new Location(70, 230);
  private final Location statusLocation = new Location(20, 330);
  private final Location statusDisplayLocation = new Location(100, 320);
  private final Location scoreLocation = new Location(20, 430);
  private final Location scoreDisplayLocation = new Location(100, 430);
  private final Location resultLocation = new Location(20, 495);
  private final Location resultDisplayLocation = new Location(100, 495);

  private final Location autoChkLocation = new Location(15, 375);
  private final Location toggleModeLocation = new Location(95, 375);

  private final Location die1Location = new Location(20, 270);
  private final Location die2Location = new Location(50, 270);
  private final Location die3Location = new Location(80, 270);
  private final Location die4Location = new Location(110, 270);
  private final Location die5Location = new Location(140, 270);
  private final Location die6Location = new Location(170, 270);

  private GamePane gp;
  private GGButton handBtn = new GGButton("sprites/handx.gif");

  private GGButton die1Button = new CustomGGButton(DIE1_BUTTON_TAG, "sprites/Number_1.png");
  private GGButton die2Button = new CustomGGButton(DIE2_BUTTON_TAG, "sprites/Number_2.png");
  private GGButton die3Button = new CustomGGButton(DIE3_BUTTON_TAG, "sprites/Number_3.png");
  private GGButton die4Button = new CustomGGButton(DIE4_BUTTON_TAG, "sprites/Number_4.png");
  private GGButton die5Button = new CustomGGButton(DIE5_BUTTON_TAG, "sprites/Number_5.png");
  private GGButton die6Button = new CustomGGButton(DIE6_BUTTON_TAG, "sprites/Number_6.png");

  private GGTextField pipsField;
  private GGTextField statusField;
  private GGTextField resultField;
  private GGTextField scoreField;
  private boolean isAuto;
  private GGCheckButton autoChk;
  private boolean isToggle = false;
  private GGCheckButton toggleCheck =
          new GGCheckButton("Toggle Mode", YELLOW, TRANSPARENT, isToggle);
  private int nbRolls = 0;
  private volatile boolean isGameOver = true;
  private Properties properties;
  private java.util.List<java.util.List<Integer>> dieValues = new ArrayList<>();
  private GamePlayCallback gamePlayCallback;
  private ArrayList<HashMap<Integer, Integer>> rolled = new ArrayList<HashMap<Integer, Integer>> ();

  NavigationPane(Properties properties)
  {
    this.properties = properties;
    int numberOfDice =  //Number of six-sided dice
            (properties.getProperty("dice.count") == null)
                    ? 1  // default
                    : Integer.parseInt(properties.getProperty("dice.count"));
    System.out.println("numberOfDice = " + numberOfDice);
    isAuto = Boolean.parseBoolean(properties.getProperty("autorun"));
    autoChk = new GGCheckButton("Auto Run", YELLOW, TRANSPARENT, isAuto);
    System.out.println("autorun = " + isAuto);
    setSimulationPeriod(200);
    setBgImagePath("sprites/navigationpane.png");
    setCellSize(1);
    setNbHorzCells(200);
    setNbVertCells(600);
    doRun();
    new SimulatedPlayer().start();
    for (int i = 0;  i < Integer.parseInt(properties.getProperty("players.count")); i++) {
	rolled.add(new HashMap<Integer, Integer> ());
	for (int j = Integer.parseInt(properties.getProperty("dice.count"));  j < Integer.parseInt(properties.getProperty("dice.count")) * 6 + 1; j++) {
	    rolled.get(i).put(j, 0);
	}
    }
  }

  void setupDieValues() {
    for (int i = 0; i < gp.getNumberOfPlayers(); i++) {
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

  void setGamePlayCallback(GamePlayCallback gamePlayCallback) {
    this.gamePlayCallback = gamePlayCallback;
  }

  void setGamePane(GamePane gp)
  {
    this.gp = gp;
    setupDieValues();
  }

  class ManualDieButton implements GGButtonListener {
    @Override
    public void buttonPressed(GGButton ggButton) {

    }

    @Override
    public void buttonReleased(GGButton ggButton) {

    }

    @Override
    public void buttonClicked(GGButton ggButton) {
	System.out.println("manual die button clicked");
	if(!isGameOver) {
		  System.out.println("Please change dice number before the game");
	}
	else {
	    
	    if (ggButton instanceof CustomGGButton) {
	        CustomGGButton customGGButton = (CustomGGButton) ggButton;
	        int tag = customGGButton.getTag();
	        System.out.println("manual die button clicked - tag: " + tag);
	        properties.setProperty("dice.count", Integer.toString(tag));
	    }
	    rolled = new ArrayList<HashMap<Integer, Integer>> ();
	    for (int i = 0;  i < Integer.parseInt(properties.getProperty("players.count")); i++) {
		rolled.add(new HashMap<Integer, Integer> ());
		for (int j = Integer.parseInt(properties.getProperty("dice.count"));  j < Integer.parseInt(properties.getProperty("dice.count")) * 6 + 1; j++) {
		    rolled.get(i).put(j, 0);
		}
	    }
      }
    }
  }
  
//add die button to the board
  void addDieButtons() {
    ManualDieButton manualDieButton = new ManualDieButton();

    addActor(die1Button, die1Location);
    addActor(die2Button, die2Location);
    addActor(die3Button, die3Location);
    addActor(die4Button, die4Location);
    addActor(die5Button, die5Location);
    addActor(die6Button, die6Location);

    die1Button.addButtonListener(manualDieButton);
    die2Button.addButtonListener(manualDieButton);
    die3Button.addButtonListener(manualDieButton);
    die4Button.addButtonListener(manualDieButton);
    die5Button.addButtonListener(manualDieButton);
    die6Button.addButtonListener(manualDieButton);
  }

//get current die value from total number of rolls
  private int getDieValue() {
    int rollPerPlayer = Integer.parseInt(properties.getProperty("dice.count"));
    if (dieValues == null) {
      return RANDOM_ROLL_TAG;
    }
    
 // calculation to find 
    int currentRound = Math.floorDiv(nbRolls, (gp.getNumberOfPlayers() * rollPerPlayer));
    int playerIndex = Math.floorDiv(nbRolls, rollPerPlayer) % gp.getNumberOfPlayers();
    int currentRoll = nbRolls % rollPerPlayer;
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

  void createGui()
  {
    addActor(new Actor("sprites/dieboard.gif"), dieBoardLocation);

    handBtn.addButtonListener(this);
    addActor(handBtn, handBtnLocation);
    addActor(autoChk, autoChkLocation);
    autoChk.addCheckButtonListener(new GGCheckButtonListener() {
      @Override
      public void buttonChecked(GGCheckButton button, boolean checked)
      {
        isAuto = checked;
        for (Puppet puppet: gp.getAllPuppets()) {
          puppet.setAuto(isAuto);
        }
        if (isAuto){
          Monitor.wakeUp();
        }
      }
    });

    addActor(toggleCheck, toggleModeLocation);
    toggleCheck.addCheckButtonListener(new GGCheckButtonListener() {
      @Override
      public void buttonChecked(GGCheckButton ggCheckButton, boolean checked) {
        isToggle = checked;
        gp.connectionDirectionChange();
      }
    });

    addDieButtons();

    pipsField = new GGTextField(this, "", pipsLocation, false);
    pipsField.setFont(new Font("Arial", Font.PLAIN, 16));
    pipsField.setTextColor(YELLOW);
    pipsField.show();

    addActor(new Actor("sprites/linedisplay.gif"), statusDisplayLocation);
    statusField = new GGTextField(this, "Click the hand!", statusLocation, false);
    statusField.setFont(new Font("Arial", Font.PLAIN, 16));
    statusField.setTextColor(YELLOW);
    statusField.show();

    addActor(new Actor("sprites/linedisplay.gif"), scoreDisplayLocation);
    scoreField = new GGTextField(this, "# Rolls: 0", scoreLocation, false);
    scoreField.setFont(new Font("Arial", Font.PLAIN, 16));
    scoreField.setTextColor(YELLOW);
    scoreField.show();

    addActor(new Actor("sprites/linedisplay.gif"), resultDisplayLocation);
    resultField = new GGTextField(this, "Current pos: 0", resultLocation, false);
    resultField.setFont(new Font("Arial", Font.PLAIN, 16));
    resultField.setTextColor(YELLOW);
    resultField.show();
  }

  void showPips(String text)
  {
    pipsField.setText(text);
    if (text != "") System.out.println(text);
  }

  void showStatus(String text)
  {
    statusField.setText(text);
    System.out.println("Status: " + text);
  }

  void showScore(String text)
  {
    scoreField.setText(text);
    System.out.println(text);
  }

  void showResult(String text)
  {
    resultField.setText(text);
    System.out.println("Result: " + text);
  }

  void prepareRoll(int currentIndex)
  {
    if (currentIndex == 100)  // Game over
    {
      playSound(GGSound.FADE);
      showStatus("Click the hand!");
      showResult("Game over");
      isGameOver = true;
      handBtn.setEnabled(true);

      java.util.List  <String> playerPositions = new ArrayList<>();
      for (Puppet puppet: gp.getAllPuppets()) {
        playerPositions.add(puppet.getCellIndex() + "");
      }
      gamePlayCallback.finishGameWithResults(nbRolls % gp.getNumberOfPlayers(), playerPositions);
      gp.resetAllPuppets();
      printStat(rolled);
      printTraverse();
    }
    else
    {
      moveOpponent();
      playSound(GGSound.CLICK);
      showStatus("Done. Click the hand!");
      String result = gp.getPuppet().getPuppetName() + " - pos: " + currentIndex;
      showResult(result);
      gp.switchToNextPuppet();
      // System.out.println("current puppet - auto: " + gp.getPuppet().getPuppetName() + "  " + gp.getPuppet().isAuto() );

      if (isAuto) {
        Monitor.wakeUp();
      } else if (gp.getPuppet().isAuto()) {
        Monitor.wakeUp();
      } else {
        handBtn.setEnabled(true);
      }
    }
  }

  void startMoving(int nb)
  {
    showStatus("Moving...");
    showPips("Pips: " + nb);
    showScore("# Rolls: " + (nbRolls));
    gp.getPuppet().go(nb);
  }

  void prepareBeforeRoll() {
    handBtn.setEnabled(false);
    if (isGameOver)  // First click after game over
    {
      isGameOver = false;
      nbRolls = 0;
    }
  }

  public void buttonClicked(GGButton btn)
  {
    System.out.println("hand button clicked");
    prepareBeforeRoll();
    rolling();
  }

  private void roll(int rollNumber)
  {
    int nb = rollNumber;

    showStatus("Rolling...");
    showPips("");
    System.out.println(nb);
    removeActors(Die.class);
    Die die = new Die(nb, this);
    addActor(die, dieBoardLocation);
    delay(1000);
    nbRolls++;
  }

  public void buttonPressed(GGButton btn)
  {
  }

  public void buttonReleased(GGButton btn)
  {
  }

  public void checkAuto() {
    if (isAuto) Monitor.wakeUp();
  }

  public void rolling() {
    int totalMove = 0;
    for (int i = 0; i < Integer.parseInt(properties.getProperty("dice.count")); i++) {
	int dieValue = getDieValue();
	if (dieValue == RANDOM_ROLL_TAG) {
	    dieValue = ServicesRandom.get().nextInt(6) + 1;
	}
        totalMove += dieValue;
        roll(dieValue);
    }
    
    String currentPuppetName = gp.getPuppet().getPuppetName();
    int currentPuppet = Integer.parseInt(currentPuppetName.substring(currentPuppetName.length() - 1)) - 1;
    if (rolled.get(currentPuppet).get(totalMove) == null) {
      rolled.get(currentPuppet).put(totalMove, 1);
    } else {
      rolled.get(currentPuppet).put(totalMove, rolled.get(currentPuppet).get(totalMove) + 1);
    }

    startMoving(totalMove);
  }
  
  public int getDiceNum() {
      return Integer.parseInt(properties.getProperty("dice.count"));
  }
  
  public void moveOpponent() {
      Puppet puppet = gp.getPuppetOnCell(gp.getPuppet().getCellIndex());
      if (puppet != null) {
	  gp.getAllPuppets().get(gp.getAllPuppets().indexOf(puppet)).moveToPreviousCell();
      }
  }
  
  public void printStat(ArrayList<HashMap<Integer, Integer>> rolled) {
      String currentPuppetName = gp.getPuppet().getPuppetName();
      int currentPuppet = Integer.parseInt(currentPuppetName.substring(currentPuppetName.length() - 1)) - 1;
      System.out.print(gp.getPuppet().getPuppetName() +" rolled: ");
      int i = 0;
      for (Integer roll : rolled.get(currentPuppet).keySet()) {
        if (i != 0) {
            System.out.print(", ");
        }
	System.out.print(roll + "-" + rolled.get(currentPuppet).get(roll));
        i++;
      }
      System.out.println("");
  }
  
  public void printTraverse() {
      int up = gp.getPuppet().getUpCount();
      int down = gp.getPuppet().getDownCount();

      System.out.print(gp.getPuppet().getPuppetName() + " traversed: ");
      System.out.println("up-" + up + ", " + "down-" + down);
  }
}
