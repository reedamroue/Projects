 /*  Name: Reeda Mroue
  *  PennKey: rmroue
  *  Recitation: 206
  *
  *  A class representing the arena in which the Irate Avians
  *  game takes place. Keeps track of the game's Bird and
  *  Targets and receives the player's input to control the bird.
  *
  */
public class Arena {
  // The width and height of the PennDraw screen (10, 5)
  private int width, height;
  
  // All the targets in the Arena
  private Target[] targets;
  
  // The one and only bird in the game
  private Bird bird;
  
  /**
   * Whether the game is currently listening for
   * the player's mouse input, or letting the bird
   * fly. Begins as true.
   */
  private boolean mouseListeningMode;
  
  /**
   * Tells the program if the user was pressing
   * the mouse in the previous update call. Lets
   * the program know if the user has just released
   * the mouse because mouseWasPressedLastUpdate will
   * be true, but PennDraw.mousePressed() will be false
   * in the current update call. This enables the game
   * to transition from the mouse listening state into
   * the bird flight state.
   */
  private boolean mouseWasPressedLastUpdate;
  
  /**
   * Given a file that describes the contents of the
   * Arena, parse the file and initialize all member
   * variables of the Arena.
   * The file will be in the following format:
   * numTargets width height
   * bird.numThrows
   * target0.xPos target0.yPos target0.radius target0.xVel
   * target0.yVel target0.hitPoints
   * target1.xPos... etc.
   */
  public Arena(String filename) {
    In in = new In(filename);
    int numTargets = in.readInt(); 
    targets = new Target[numTargets];
    width = in.readInt(); 
    height = in.readInt(); 
    int numThrows = in.readInt(); 
    double xPos = 0;
    double yPos = 0;
    double radius = 0; 
    double xVel = 0;
    double yVel = 0;
    int hitPoints = 0;
    bird = new Bird(1, 1, 0.25, numThrows);
    mouseListeningMode = true;
    mouseWasPressedLastUpdate = false;
    
    for (int i = 0; i < numTargets; i++) { 
      xPos = in.readDouble();
      yPos = in.readDouble();
      radius = in.readDouble();
      xVel = in.readDouble();
      yVel = in.readDouble();
      hitPoints = in.readInt();
      targets[i] = new Target(width, height, xPos, yPos, radius,
                              xVel, yVel, hitPoints);
    }
    in.close();
  }
  
  /**
   * Returns true when all targets' hit points are 0.
   * Returns false in any other scenario.
   */
  private boolean didPlayerWin() {
    int counter = 0;
    for (int i = 0; i < targets.length; i++) {
      if (targets[i].getHitPoints() == 0) {
        counter += 1;
      }   
    }
    return counter == targets.length;
  }
  /**
   * Returns true when the bird's remaining throw count is 0
   * when the game is in mouse-listening mode.
   * Returns false in any other scenario.
   */
  private boolean didPlayerLose() {
    if (mouseListeningMode == true && bird.getNumThrowsRemaining() == 0) {
      return true;
    }
    return false;
  }
  /**
   * Returns true when either the win or lose
   * condition is fulfilled.
   * Win: All targets' hit points are 0.
   * Lose: The bird's remaining throw count reaches 0.
   *   Additionally, the game must be in mouse listening
   *   mode for the player to have lost so that the bird
   *   can finish its final flight and potentially hit
   *   the last target(s).
   */
  public boolean gameOver() {
    if (didPlayerLose() || didPlayerWin()) {
      return true;
    }
    return false;
  }
  
  /**
   * Update each of the entities within the arena.
   * 1. Call each Target's update function
   * 2. Check the game state (mouse listening or bird moving)
   * and invoke the appropriate functions for the bird.
   */
  public void update(double timeStep) {
    for (int i = 0; i < targets.length; i++) {
      targets[i].update(timeStep);
    }
          /**
       * If the mouse is currently pressed, then
       * set mouseWasPressedLastUpdate to true, and
       * call bird.setVelocityFromMousePos.
       */
    if (mouseListeningMode) {
      if (!mouseWasPressedLastUpdate) {
        if (PennDraw.mousePressed()) {
          mouseWasPressedLastUpdate = true;
          bird.setVelocityFromMousePos();
        }
      } else {
              /**
       * If the mouse is NOT currently pressed, AND
       * mouseWasPressedLastUpdate is currently true,
       * that means the player has just released the
       * mouse button, and the game should transition
       * from mouse-listening mode to bird-flight mode.
       */
        if (PennDraw.mousePressed()) {
          bird.setVelocityFromMousePos();
        } else {
          bird.decrementThrows();
          StdAudio.play("birdAudio1.wav");
          mouseWasPressedLastUpdate = false;
          mouseListeningMode = false;
        }
      }
    } else {
      bird.update(timeStep);
      for (int i = 0; i < targets.length; i++) {
        bird.testAndHandleCollision(targets[i]);
      }
      if (birdIsOffscreen() == true) {
        for (int i = 0; i < targets.length; i++) {
          if (targets[i].isHit()) {
            StdAudio.play("targetSound.wav");
            targets[i].decreaseHP();
            targets[i].setHitThisShot(false);
          }
        }
        StdAudio.play("resetSound.wav");
        bird.reset();
        mouseListeningMode = true;   
      }
    }
  }
  /**
   * A helper function for the Arena class that lets
   * it know when to reset the bird's position and velocity
   * along with the game state.
   * Returns true when the bird is offscreen to the left, right,
   * or bottom. However, the bird is allowed to go above the top
   * of the screen without resetting.
   */
  private boolean birdIsOffscreen() {
    while (bird.getYpos() + bird.getRadius() < 0 || bird.getXpos() +
           bird.getRadius() < 0 || bird.getXpos() - bird.getRadius() > width) {
      return true;
    }
    return false;
  }
  /**
   * 1. Clear the screen
   * 2. Draw each target
   * 3. Draw the bird
   * 4. If in mouse listening mode and
   *    the mouse was pressed last update,
   *    draw the bird's velocity as a line.
   * 5. Advance PennDraw.
   */
  public void draw() {
    PennDraw.clear();
    for (int i = 0; i < targets.length; i++) {
      targets[i].draw();
    }
    bird.draw();
    if (mouseWasPressedLastUpdate) {
      bird.drawVelocity();
    }
    PennDraw.advance();
  }
  
  /**
   * Draws either the victory or loss screen.
   * If all targets have 0 hit points, the player has won.
   * Otherwise they have lost.
   */
  public void drawGameCompleteScreen() {
    PennDraw.clear();
    if (didPlayerWin()) {
      PennDraw.setPenColor(PennDraw.BLACK);
      PennDraw.text(5, 2.5, "You Win!");
      StdAudio.play("winSound.wav");
    } else if (didPlayerLose()) {
      PennDraw.setPenColor(PennDraw.BLACK);
      PennDraw.text(5, 2.5, "You have lost...");
    }
    PennDraw.advance();
  }
}
