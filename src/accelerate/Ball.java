/*
 * Ball Class
 * 
 * The Ball object. Contians all the info for the ball and stuff
 */
package accelerate;
import org.newdawn.slick.geom.*;

/**
 *
 * @author Zain
 */
public class Ball extends Circle {
    // VARIABLES
    
    private Vector2f speed;
    private float mass;
    
    public int distance = 0;
    
    
    // CONSTRUCTOR
    
    public Ball(float x, float y, float radius, float m) {
        super(x, y, radius);
        speed = new Vector2f(0, 0);
        mass = m;
    }
    
    
    // ACC, MUT
    
    public Vector2f getSpeed() {
        return speed;
    }
    
    public float getMass() {
        return mass;
    }
    
    public void setMass(float other) {
        mass = other;
    }
    
    
    // *** METHODS ***
    
    public void changeSpeedX(float value) {
        speed.set(speed.getX() + value, speed.getY());
    }
    
    public void changeSpeedY(float value) {
        speed.set(speed.getX(), speed.getY() + value);
    }
    
    
    // Moves the ball depending on its speed (moves this much per second)
    public void move() {
        setCenterX(getCenterX() + speed.getX());
        setCenterY(getCenterY() + speed.getY());
        distance += speed.getX();
    }
    
    
    // returns whether the ball is in motion
    public boolean isMoving() {
        return speed.length() != 0;
    }
    
    
    
    /*
     * Note
     * 
     * Everything else (drawing the ball, and (possibly) even MOVING the ball should go in
     * the GameManager class. It shouldn't be in the scope of this class how the game utilizes
     * this class, it is just an extension of the Circle class with a variable to track the speed
     * of the ball. If this feature is deemed unnecessary (as it may well be), there is no longer a use
     * for this class and the ball should just be a variable in the GameManager class of the type Circle.
     */
    
    /**
     * Note #2
     * 
     * I changed my mind and added the move method in this class
     * The draw method will be in the GameManager class however (unlike the Snake game)
     */

}
