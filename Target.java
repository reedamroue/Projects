 /*  Name: Reeda Mroue
  *  PennKey: rmroue
  *  Recitation: 206
  *
  *  A class that represents a target to be hit in
  *  Irate Avians. Can update its own position based
  *  on velocity and time.
  */


public class Target {
  
  // variables for width and height of screen (10, 5)
  private double width, height;
  
  // Position and radius
  private double xPos, yPos, radius;
  
  // Velocity components
  private double xVel, yVel;
  
  /**
   * When a target's hit points reach zero,
   * it has been destroyed by the bird.
   */
  private int hitPoints;
  
  // Track if target has been hit this shot.
  private boolean hitThisShot;
  
  /**
   * Given a position, a radius, a velocity, and a number of hit points,
   * construct a Target.
   */
  public Target(double width, double height, double xPos, double yPos, 
                double radius, double xVel, double yVel, int hitPoints) {
    this.radius = radius;
    this.xVel = xVel;
    this.yVel = yVel;
    this.xPos = xPos;
    this.yPos = yPos;
    this.width = width;
    this.height = height;
    this.hitPoints = hitPoints;
    hitThisShot = false;
  }
  
  /**
   * Draw a circle centered at the target's position
   * with a radius equal to the target's radius.
   * Only draw a Target if it has more than zero
   * hit points.
   */
  public void draw() {
    if (hitPoints > 0) {
      PennDraw.setPenColor(PennDraw.RED);
      PennDraw.filledCircle(xPos, yPos, radius);
      PennDraw.setPenColor(PennDraw.YELLOW);
      PennDraw.filledCircle(xPos, yPos, radius - radius * 0.1);
      PennDraw.setPenColor(PennDraw.BLUE);
      PennDraw.filledCircle(xPos, yPos, radius - radius * 0.2);
      PennDraw.setPenColor(PennDraw.GREEN);
      PennDraw.filledCircle(xPos, yPos, radius - radius * 0.3);
      PennDraw.setPenColor(PennDraw.ORANGE);
      PennDraw.filledCircle(xPos, yPos, radius - radius * 0.4);
      PennDraw.setPenColor(PennDraw.MAGENTA);
      PennDraw.filledCircle(xPos, yPos, radius - radius * 0.5);
      PennDraw.setPenColor(PennDraw.RED);
      PennDraw.filledCircle(xPos, yPos, radius - radius * 0.6);
      PennDraw.setPenColor(PennDraw.WHITE);
      PennDraw.text(xPos, yPos, Integer.toString(hitPoints));
    }
  }
  /**
   * Given the change in time, update the target's
   * position based on its x and y velocity. When
   * a target is completely offscreen horizontally,
   * its position should wrap back around to the opposite
   * horizontal side. For example, if the target moves off the
   * right side of the screen, its xPos should be set to the
   * left side of the screen minus the target's radius.
   * The same logic should apply to the target's vertical
   * position with respect to the vertical screen boundaries.
   */
  public void update(double timeStep) {
    xPos += xVel * timeStep;
    yPos += yVel * timeStep;
    if (xPos + radius < 0) {
      xPos = xPos + width;
    } else if (xPos > (width + radius)) {
      xPos = width - xPos;
    } else if (yPos + radius < 0) {
      yPos = yPos + height;
    } else if (yPos > (height + radius)) {
      yPos = height - yPos;
    }
  }
  // Decrement the target's hit points by 1.
  public void decreaseHP() {
    --hitPoints;
    radius *= 0.9;
  }
  
  /**
   * Setter function for whether or not target hit this round.
   */
  public void setHitThisShot(boolean hit) {
    hitThisShot = hit;
  }
  
  /**
   * Return whether or not this target is hit this round.
   */
  public boolean isHit() {
    if (hitThisShot) {
      return true;
    }
    return false;
  }
  
  /**
   * Getter functions that return a copy of the
   * indicated member variable.
   */
  public int getHitPoints() { 
    return hitPoints; 
  }
  public double getXpos() { 
    return xPos; 
  }
  public double getYpos() { 
    return yPos; 
  }
  public double getRadius() { 
    return radius; 
  }
  
}
