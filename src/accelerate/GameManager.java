/*
 * GameManager Class
 * 
 * Handles the input/overall game variables
 * 
 * August 12: Adding capability to click the mouse and the ball will elegantly glide to where the pointer is
 */
package accelerate;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;

/**
 *
 * @author Zain
 */
public class GameManager extends BasicGame {
    // *** VARIABLES ***
    
    private final float FORCE = 25f;
    private final int BALL_SIZE = 15;
    private final float BALL_MASS = 0.5f;
    private final float FRICTION = 1f;
    private final float GRAVITY = 9.8f;
    private final int FPS = 60;
    
    private enum BoundaryAction {
        BOUNCE, TELEPORT
    }
    
    private enum Direction {
        LEFT, RIGHT, UP, DOWN;
    }
    
    private GameContainer container;
    private Ball ball;
    private Input input;    // polled input
    private BoundaryAction boundaryAction;
    
    // Variables for the mouse click movement capability
    private boolean clickMovementInProgress;
//    private int numFrames_x;    // number of frames to apply the force to the x direction for to get the ball to the click location
//    private int numFrames_y;    // same as above for y direction 
    private int numFrames;
    private float distance_x;
    private float distance_y;
    private Vector2f direction; // the vector of the direction the ball needs to move in. This will be scaled to the length of the FORCE applied to the ball
//    private int destination_x;
//    private int destination_y;
    
    
    // *** CONSTRUCTOR ***
    
    public GameManager(String title) {
        super(title);
    }
    
    
    // *** METHODS ***
    
    // INITIALIZES THE SCREEN
    @Override
    public void init(GameContainer container) {
        ball = new Ball(container.getWidth() / 2, container.getHeight() / 2, BALL_SIZE, BALL_MASS);
        input = container.getInput();
        boundaryAction = BoundaryAction.TELEPORT;
        this.container = container;
        clickMovementInProgress = false;
    }
    
    
    // UPDATES THE SCREEN (GAME LOOP)
    @Override
    public void update(GameContainer container, int time) {
        if (clickMovementInProgress) {
            
            // applying the force to the ball automatically
            if (numFrames != 0) {
                applyForce(direction.normalise().scale(FORCE));
                numFrames--;
            }
            
//            if (numFrames_x != 0) {
//                if (distance_x > 0) {
//                    applyForce(new Vector2f(FORCE, 0));
//                } else {
//                    applyForce(new Vector2f(-FORCE, 0));
//                }
//                numFrames_x--;
//            }
//            if (numFrames_y != 0) {
//                if (distance_y > 0) {
//                    applyForce(new Vector2f(0, FORCE));
//                } else {
//                    applyForce(new Vector2f(0, -FORCE));
//                }
//                numFrames_y--;
//            }

//            if (numFrames != 0) {
//                test();
//                numFrames--;
//            }
            // determining when the click motion is completed
            if (!ball.isMoving()) {
                clickMovementInProgress = false;
            }
            
        } else {
            handleInput();
        }
        applyFriction();
        boundaryCheck();
        ball.move();
        checkClose();
    }
    
    
    // RENDERS THE GRAPHICS (GAME LOOP)
    @Override
    public void render(GameContainer container, Graphics g) {
        g.fill((Circle)ball);
        g.drawString(String.format("Speed: x = %.2f   y = %.2f", ball.getSpeed().getX(), ball.getSpeed().getY()), container.getWidth() / 2, 10);
        g.drawString("distance: " + ball.distance, 10, 100);
    }
    
    
    // HANDLE INPUT
    public void handleInput() {
        if (input.isKeyDown(Input.KEY_LEFT)) {
            applyForce(new Vector2f(-FORCE, 0));
        }
        if (input.isKeyDown(Input.KEY_RIGHT)) {
            applyForce(new Vector2f(FORCE, 0));
        }
        if (input.isKeyDown(Input.KEY_UP)) {
            applyForce(new Vector2f(0, -FORCE));
        }
        if (input.isKeyDown(Input.KEY_DOWN)) {
            applyForce(new Vector2f(0, FORCE));
        }
    }
    
    
    // checks key press for close window
    public void checkClose() {
        //to close window
        if (input.isKeyDown(Input.KEY_ESCAPE)) {
            closeRequested();
        }
    }
    
    
    // APPLIES FRICTION TO THE BALL
    public void applyFriction() {
        float friction = FRICTION * GRAVITY * ball.getMass();
        float theta = (float)ball.getSpeed().getTheta();
        float friction_x = Math.abs(friction * (float)Math.cos(theta));
        float friction_y = Math.abs(friction * (float)Math.sin(theta));
        
        if (ball.isMoving()) {
            // Applying friction to the x direction
            if (ball.getSpeed().getX() < 0) {
                applyForce(new Vector2f(friction_x, 0));
                
                if (ball.getSpeed().getX() > 0) {
                    ball.getSpeed().set(0, ball.getSpeed().getY());
                }
            } else if (ball.getSpeed().getX() > 0) {
                applyForce(new Vector2f(-friction_x, 0));
                
                if (ball.getSpeed().getX() < 0) {
                    ball.getSpeed().set(0, ball.getSpeed().getY());
                }
            }
            // Applying force in the y direction
            if (ball.getSpeed().getY() < 0) {
                applyForce(new Vector2f(0, friction_y));
                
                if (ball.getSpeed().getY() > 0) {
                    ball.getSpeed().set(ball.getSpeed().getX(), 0);
                }
            } else if (ball.getSpeed().getY() > 0) {
                applyForce(new Vector2f(0, -friction_y));
                
                if (ball.getSpeed().getY() < 0) {
                    ball.getSpeed().set(ball.getSpeed().getX(), 0);
                }
            }
        }
    }
    
    
    // APPLIES A FORCE TO THE BALL
    public void applyForce(Vector2f force) {
        ball.changeSpeedX(force.getX()/FPS/ball.getMass());
        ball.changeSpeedY(force.getY()/FPS/ball.getMass());
    }
    
    
    // WHEN THE BALL GOES PAST THE EDGES
    public void boundaryCheck() {
        if (boundaryAction == BoundaryAction.BOUNCE) {
            if (ball.getCenterX() - ball.getRadius() <= 0 || ball.getCenterX() + ball.getRadius() >= container.getWidth()) {
                // left or right edge: reversing the x component of speed
                ball.changeSpeedX(-ball.getSpeed().getX() * 2);
                //ball.getSpeed().set(ball.getSpeed().getX(), -ball.getSpeed().getY());
            }
            if (ball.getCenterY() - ball.getRadius() <= 0 || ball.getCenterY() + ball.getRadius() >= container.getHeight()) {
                // upper or lower edge: reversing the y component of speed
                ball.changeSpeedY(-ball.getSpeed().getY() * 2);
            }
        } else {
            if (ball.getCenterX() + ball.getRadius() < 0) {
                // left
                ball.setCenterX(container.getWidth() - ball.getRadius());
                
            } else if (ball.getCenterX() - ball.getRadius() > container.getWidth()) {
                // right
                ball.setCenterX(ball.getRadius());
                
            } else if (ball.getCenterY() + ball.getRadius() < 0) {
                // up
                ball.setCenterY(container.getHeight() - ball.getRadius());
                
            } else if (ball.getCenterY() - ball.getRadius() > container.getHeight()) {
                // down
                ball.setCenterY(ball.getRadius());
            }
        }
    }
    
    
    // MOUSE CLICKED
    @Override
    public void mouseReleased(int button, int x, int y) {
        distance_x = x - ball.getCenterX();
        distance_y = y - ball.getCenterY();
        direction = new Vector2f(distance_x, distance_y);
//        numFrames_x = timeToApplyForce(distance_x);
//        numFrames_y = timeToApplyForce(distance_y);
        numFrames = /*timeToApplyForce(direction.length());*/ 60;

        clickMovementInProgress = true;
    }
    
    public void test() {
        applyForce(new Vector2f(FORCE, 0));
    }
    
    // CALCULATES THE TIME TO APPLY THE FORCE FOR WHEN THE MOUSE IS CLICKED
    public int timeToApplyForce(float distance) {
        // I came up with this formula using pencil and paper, this is just the formula with no work shown so don't try to make sense of it without tools
        double time;
        distance = Math.abs(distance);
        
        time = Math.sqrt(2*FRICTION*GRAVITY*distance) / (FORCE/ball.getMass() - FRICTION*GRAVITY);
        
        // number of frames SHOULD BE (I think) the time in seconds since I adjusted the force aplied per frame in the applyForce() method
        // nvm. I'm going to try time * fps. // didn't work either
        /* Additional note (prev most likely wont work), force needs to be separated into x and y components or same mistake with friction will happen again
         * This will probably also remove the need for two variables to keep track of the time. One will be enough
        */
        return (int)(time*FPS);
    }
    
    
    // CLOSE REQUESTED
    @Override
    public boolean closeRequested() {
        System.out.println("Closed Window");
        System.exit(0);
        return true;
    }
    
}
