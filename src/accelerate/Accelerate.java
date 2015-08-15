/*
 * *** ACCELERATE ***
 * 
 * An application which allows the user to accelerate a ball using the arrow keys.
 * I'm going to attempt including friction and stuff to slow down the ball
 * 
 * Basically this is a 'test' or practice implementing physics into my programs.
 * 
 * Date: August 10 / 2015
 * 
 */
package accelerate;
import org.lwjgl.opengl.*;
import org.newdawn.slick.*;

/**
 *
 * @author Zain
 */
public class Accelerate {
    
    private static final int X = 2560;
    private static final int Y = 1600;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SlickException {
        AppGameContainer game = new AppGameContainer(new GameManager("Accelerate"));
        game.setDisplayMode(X, Y, true);
        game.setShowFPS(false);
        game.setTargetFrameRate(60);
        game.start();
        
//        try{
//        DisplayMode[] modes = Display.getAvailableDisplayModes();
//        
//         for (int i=0;i<modes.length;i++) {
//             DisplayMode current = modes[i];
//             System.out.println(current.getWidth() + "x" + current.getHeight() + "x" +
//                                 current.getBitsPerPixel() + " " + current.getFrequency() + "Hz");
//         }
//         System.out.println(game.getAspectRatio());
//                 } catch(Exception x) {}
    }
    
}
