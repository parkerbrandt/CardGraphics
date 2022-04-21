package edu.ou.cs.cg.project;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLRunnable;
import edu.ou.cs.cg.project.scene.Card;
import edu.ou.cs.cg.utilities.Utilities;

import java.awt.*;
import java.io.*;

/**
 * The Model Class
 * Handles user interaction with data
 *
 * @author Parker Brandt
 */
public class Model {

    //****************************************
    // Private Variables
    //****************************************
    private final View view;

    // TODO: Other variables
    private boolean isCardOpen;         // Used to show if the card is open or not
    private boolean showInstructions;   // Used to show if the instructions should be open or not
    private boolean isEditMode;         // Use to determine if the user is editing the card or not

    private final Color[] cardColors;   // List of all available card colors
    private int currentColor;           // Current index of selected color


    //****************************************
    // Constructors
    //****************************************
    public Model(View view) {
        // Initialize variables
        this.view = view;

        isCardOpen = false;
        showInstructions = false;
        isEditMode = false;

        cardColors = new Color[]{   new Color(198, 41, 41, 255),
                                    new Color(0, 102, 0, 255),
                                    new Color(26, 62, 161)
                                };
        currentColor = 0;
    }


    //****************************************
    // Private Methods
    //****************************************

    // File save/load methods
    /**
     * Save the card data to a file
     * @param filename
     * @throws IOException
     */
    public void save(String filename) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
    }

    /**
     * Read the card data from the specified filename
     * @param filename
     * @throws IOException
     */
    public Card load(String filename) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = "";
        while((line = reader.readLine()) != null) {

        }

        return new Card(view, this);
    }


    // Card Modification Methods

    /**
     * Switch the currently selected color of the current card
     */
    public void switchColor() {
        currentColor = currentColor == cardColors.length-1 ? 0 : currentColor + 1;
    }



    //****************************************
    // Getters and Setters
    //****************************************

    // Getters
    public boolean isCardOpen() {
        return isCardOpen;
    }

    public boolean showInstructions() {
        return showInstructions;
    }

    public Color getCardColor() {
        return cardColors[currentColor];
    }


    // Setters
    public void setCardOpen(boolean isCardOpen) {
        this.isCardOpen = isCardOpen;
    }

    public void setShowInstructions(boolean shouldShow) {
        showInstructions = shouldShow;
    }



    //****************************************
    // Inner Classes
    //****************************************
    // Convenience class to simplify the implementation of most updaters.
    private abstract class BasicUpdater implements GLRunnable
    {
        public final boolean	run(GLAutoDrawable drawable)
        {
            GL2 gl = drawable.getGL().getGL2();

            update(gl);

            return true;	// Let animator take care of updating the display
        }

        public abstract void	update(GL2 gl);
    }

    // Convenience class to simplify updates in cases in which the input is a
    // single point in view coordinates (integers/pixels).
    private abstract class ViewPointUpdater extends BasicUpdater
    {
        private final Point q;

        public ViewPointUpdater(Point q)
        {
            this.q = q;
        }

        public final void	update(GL2 gl)
        {
            int		    h = view.getHeight();
            double[]	p = Utilities.mapViewToScene(gl, q.x, h - q.y, 0.0);

            update(p);
        }

        public abstract void	update(double[] p);
    }
}
