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
  private static int nbRolls = 0;
  private volatile boolean isGameOver = true;
  private Properties properties;
  private DieController dieController;
  private GamePlayCallback gamePlayCallback;

  NavigationPane(Properties properties)
  {
    this.properties = properties;
    dieController = new DieController(properties);
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
  }



  void setGamePlayCallback(GamePlayCallback gamePlayCallback) {
    this.gamePlayCallback = gamePlayCallback;
  }

  void setGamePane(GamePane gp)
  {
    this.gp = gp;
    dieController.setupDieValues();
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
	    dieController.rolledRecordChange();
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
      printStat(dieController.getRolled());
      printTraverse();
    }
    else
    {
      gp.moveOpponent();
      playSound(GGSound.CLICK);
      showStatus("Done. Click the hand!");
      String result = gp.getPuppet().getPuppetName() + " - pos: " + currentIndex;
      showResult(result);
      if (isAuto) {
	  if (gp.toggleDecision()) {
	      if (!isToggle) {
		  gp.connectionDirectionChange();
	      }
	      toggleCheck.setChecked(true);
	      isToggle = true;
	  } else {
	      if (isToggle) {
		  gp.connectionDirectionChange();
	      }
	      toggleCheck.setChecked(false);
	      isToggle = false;
	  }
      }
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
  
  void prepareBeforeRoll() {
	    handBtn.setEnabled(false);
	    if (isGameOver)  // First click after game over
	    {
	      isGameOver = false;
	      nbRolls = 0;
	    }
  }
  
  public void roll(int rollNumber)
  {
        int nb = rollNumber;
  
        showStatus("Rolling...");
        showPips("");
        System.out.println(nb);
        removeActors(Die.class);
        Die die = new Die(nb);
        addActor(die, dieBoardLocation);
        delay(1);
        nbRolls++;
  }
  
  void startMoving(int nb)
  {
    showStatus("Moving...");
    showPips("Pips: " + nb);
    showScore("# Rolls: " + (nbRolls));
    gp.getPuppet().go(nb);
  }

  public void buttonClicked(GGButton btn)
  {
    System.out.println("hand button clicked");
    prepareBeforeRoll();
    rolling();
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
  
  public int getDiceNum() {
      return Integer.parseInt(properties.getProperty("dice.count"));
  }
  
  public void rolling() {
	    int totalMove = 0;
	    for (int i = 0; i < getDiceNum(); i++) {
		int dieValue = dieController.getDieValue();
		if (dieValue == RANDOM_ROLL_TAG) {
		    dieValue = ServicesRandom.get().nextInt(6) + 1;
		}
	        totalMove += dieValue;
	        roll(dieValue);
	    }
	    
	    String currentPuppetName = gp.getPuppet().getPuppetName();
	    int currentPuppet = Integer.parseInt(currentPuppetName.substring(currentPuppetName.length() - 1)) - 1;
	    dieController.addRolledRecord(totalMove);

	    startMoving(totalMove);
}
  
  
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
      
      for (Puppet puppet: gp.getAllPuppets()) {
	  int up = puppet.getUpCount();
	  int down = puppet.getDownCount();

	  System.out.print(puppet.getPuppetName() + " traversed: ");
	  System.out.println("up-" + up + ", " + "down-" + down);
      }
      
  }
  
  public boolean isToggle() {
      return isToggle;
  }
  
  public static int getNbRolls() {
      return nbRolls;
  }
}
