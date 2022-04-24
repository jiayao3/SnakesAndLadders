package snakeladder.game;

import ch.aplu.jgamegrid.GGSound;
import ch.aplu.jgamegrid.Location;

public abstract class Connection
{
  Location locStart;
  Location locEnd;
  int cellStart;
  int cellEnd;
  boolean reverse = false;
  
  
  Connection(int cellStart, int cellEnd)
  {
    this.cellStart = cellStart;
    this.cellEnd = cellEnd;
    locStart = GamePane.cellToLocation(cellStart);
    locEnd = GamePane.cellToLocation(cellEnd);
  }

  String imagePath;

  public Location getLocStart() {
    return locStart;
  }

  public Location getLocEnd() {
    return locEnd;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String imagePath) {
    this.imagePath = imagePath;
  }

  public double xLocationPercent(int locationCell) {
    return (double) locationCell / GamePane.NUMBER_HORIZONTAL_CELLS;
  }
  public double yLocationPercent(int locationCell) {
    return (double) locationCell / GamePane.NUMBER_VERTICAL_CELLS;
  }
  
  public void switchDirection() {
      int start = cellStart;
      int end = cellEnd;
      Location startLoc = locStart;
      Location endLoc = locEnd;
      this.cellStart = end;
      this.cellEnd = start;
      this.locEnd = startLoc;
      this.locStart = endLoc;
      if (reverse) {
	  reverse = false;
      } else {
	  reverse = true;
      }
  }
  
  public boolean getReverse() {
      return reverse;
  }
  

  public abstract boolean getDirection();
  
  public abstract void countTraverse();
  
  public abstract String status();
  
  public abstract GGSound sound();
}
