 /*  Name: Reeda Mroue
  *  PennKey: rmroue
  *  Recitation: 206
  *
  *  A class that represents the bird projectile in
  *  Irate Avians. Can update its own position based
  *  on velocity and time, and can compute whether
  *  it overlaps a given Target.
  *
  */

public class Bird {
  // The position, velocity, and radius members of the bird.
  private double xPos, yPos, xVel, yVel, radius;
  
  /**
   * How many more times the player can throw the bird
   * before losing the game.
   */
  private int numThrowsRemaining;
  
  /**
   * Initialize the bird's member variables
   * with the same names as the inputs to those values.
   * Initializes the bird's velocity components to 0.
   */
  public Bird(double xPos, double yPos, double radius, int numThrows) {
    this.xPos = xPos;
    this.yPos = yPos;
    this.radius = radius;
    this.numThrowsRemaining = numThrows;
    xVel = 0;
    yVel = 0;
  }
  
  /**
   * Draws a circle centered at the bird's position
   * with a radius equal to the bird's radius.
   * Additionally, draws a triangular beak and two
   * circular eyes somewhere on the circle to make
   * the bird look more like a bird. Additional details
   * are up to your discretion.
   * Also draws the bird's remaining throws 0.1 units
   * above its circular body.
   */
  public void draw() {
    PennDraw.setPenColor(PennDraw.BLUE);
    PennDraw.filledCircle(xPos, yPos, radius);
    PennDraw.text(xPos, yPos + radius + 0.1, Integer.toString(numThrowsRemaining));
    PennDraw.setPenColor(PennDraw.ORANGE);
    PennDraw.filledPolygon(xPos, yPos + radius / 4, xPos + radius / 2, yPos, xPos,
                           yPos - radius / 4);
    PennDraw.setPenColor(PennDraw.WHITE);
    PennDraw.filledCircle(xPos - radius / 4 , yPos + radius / 2, radius / 6);
    PennDraw.filledCircle(xPos + radius / 4 , yPos + radius / 2, radius / 6);
  }
  
  /**
   * Draw the line representing the bird's initial velocity
   * when the player is clicking and dragging the mouse.
   */
  public void drawVelocity() {
    PennDraw.setPenColor(PennDraw.BLACK);
    PennDraw.line(xPos, yPos, xPos + xVel, yPos + yVel);
  }
  /**
   * Set xPos and yPos to 1.0,
   * set xVel and yVel to 0.0,
   * and clear the list of targets hit this launch.
   */
  public void reset() {
    xPos = yPos = 1.0;
    xVel = yVel = 0.0;
  }
  
  /**
   * Compute the bird's initial velocity as the
   * vector from the mouse's current position to
   * the bird's current position. This will be used
   * in mouse listening mode to update the launch
   * velocity.
   */
  public void setVelocityFromMousePos() {
    xVel = xPos - PennDraw.mouseX();
    yVel = yPos - PennDraw.mouseY();
  }
  
  /**
   * Given the change in time, compute the bird's
   * new position and new velocity.
   */
  public void update(double timeStep) {
    xPos += xVel * timeStep;
    yPos += yVel * timeStep;
    yVel -= 0.25 * timeStep;
  }
  
  /**
   * A helper function used to find the distance
   * between two 2D points. Remember to use the
   * Pythagorean Theorem.
   */
  private static double distance(double x1, double y1, double x2, double y2) {
    return Math.sqrt((x1 - x2) * (x1 - x2) + 
                     (y1 - y2) * (y1 - y2));
  }
  
  /**
   * Given a Target, determine if the bird should
   * test for collision against it. If the bird
   * *should* see if it collides with the target,
   * then perform that test. If the bird collides,
   * then decrease the target's HP by 1 and add
   * the target to the bird's list of targets hit
   * during this launch.
   */
  public void testAndHandleCollision(Target t) {
    if (t.getHitPoints() > 0 && !t.isHit()) {
      if (distance(t.getXpos(), t.getYpos(),
                    getXpos(), getYpos()) < getRadius() + t.getRadius()) {
        t.setHitThisShot(true);
      }
    }
  }
  
  // Reduce numThrowsRemaining by 1.
  public void decrementThrows() {
    numThrowsRemaining -= 1;
  }
  
  /**
   * Getter functions that return a copy
   * of the indicated member variable.
   */
  public double getXpos() { 
    return xPos; 
  }
  public double getYpos() { 
    return yPos; 
  }
  public double getRadius() { 
    return radius; 
  }
  public int getNumThrowsRemaining() { 
    return numThrowsRemaining; 
  }
}
