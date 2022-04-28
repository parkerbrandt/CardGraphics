package edu.ou.cs.cg.project;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLRunnable;
import com.jogamp.opengl.util.texture.Texture;
import edu.ou.cs.cg.project.scene.Card;
import edu.ou.cs.cg.utilities.Transform;
import edu.ou.cs.cg.utilities.Utilities;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

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

    private double cardX, cardY;

    // TODO: Move cards here
    private ArrayList<Card> displayCards;

    private Texture[] textures;

    private int selectedTree;
    private boolean isFrontTree;

    private ArrayList<Point2D.Float> treeLoc;

    private String[] frontText;
    private String[] insideText;

    private Scanner in;


    //****************************************
    // Constructors
    //****************************************
    public Model(View view) {

        // Initialize variables
        this.view = view;

        isCardOpen = false;
        showInstructions = true;
        isEditMode = false;

        cardColors = new Color[]{   new Color(198, 41, 41, 255),
                                    new Color(0, 102, 0, 255),
                                    new Color(26, 62, 161),
                                    new Color(255, 255, 51),
                                    new Color(255, 0, 255),
                                    new Color(128, 128, 128),
                                    new Color(255, 255, 255),
                                    new Color(0, 0, 0)
                                };
        currentColor = 0;

        cardX = 0.0;
        cardY = 0.0;

        textures = view.getTextures();

        // Load first 9 display cards from the cards resource folder
        displayCards = new ArrayList<>();

        selectedTree = 0;
        isFrontTree = true;

        treeLoc = new ArrayList<>();

        frontText = new String[] {"Hello", "Good Morning"};
        insideText = new String[] {"Have a", "good day!"};

        in = new Scanner(System.in);
    }


    //****************************************
    // Private Methods
    //****************************************

    // File save/load methods
    /**
     * Save the held card data to a file
     * @param filename
     * @throws IOException
     */
    public void save(String filename, Card card) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

    }

    /**
     * Read the card data from the specified filename
     * CSV Format: ID,Color,TreeLocations
     * @param filename
     * @throws IOException
     */
    public Card load(String filename, Texture[] textures) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = "";
        while((line = reader.readLine()) != null) {

        }

        return new Card(textures, view, this);
    }


    // Card Modification Methods

    /**
     * Switch the currently selected color of the current card
     * Can only be done when in editing mode
     */
    public void switchColor() {
        if(isEditMode) {
            currentColor = currentColor == cardColors.length - 1 ? 0 : currentColor + 1;
        }
    }

    /**
     * Will swap the main card out with one that is on the
     * @param index
     */
    public void switchMainCard(int index) {

    }

    /**
     * Switch the currently selected tree
     * If out of bounds of tree array, will be modified in Card class
     * TODO: Check to adjust between left and right sides
     */
    public void switchSelectedTree() {
        if(isEditMode) {
            selectedTree += 1;
        }
    }

    public void switchTreeSide() {
        isFrontTree = !isFrontTree;
        selectedTree = 0;
    }


    /**
     * Adds a tree to the main card
     */
    public void addTree() {
        // main.addTree()
    }

    /**
     *
     */
    public void deleteTree() {
        // main.removeTree()
    }

    public void moveTreeUp(float amt) {

    }

    public void moveTreeRight(float amt) {

    }

    /**
     *
     */
    public void resetCard() {

    }

    /**
     * Allows the user to change the text on the card
     * TODO: Change to just changeText() and if isOpen() is true will determine if the front or inside text should be changed
     */
    public void changeText() {

        view.getCanvas().invoke(false, new BasicUpdater() {
            @Override
            public void update(GL2 gl) {

                if(isCardOpen) {
                    System.out.println("Write new text for inside here: ");
                    insideText = in.nextLine().split(",");
                } else {
                    System.out.println("Write new text for front here: ");
                    frontText = in.nextLine().split(",");
                }
            }
        });
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

    public boolean isEditMode() {
        return isEditMode;
    }

    public ArrayList<Card> getDisplayCards() {
        return new ArrayList<>();
    }

    public int getSelectedTree() {
        return selectedTree;
    }

    public String[] getFrontText() {
        return frontText;
    }

    public String[] getInsideText() {
        return insideText;
    }

    public boolean isFrontTree() {
        return isFrontTree;
    }


    // Setters
    public void setCardOpen(boolean isCardOpen) {
        this.isCardOpen = isCardOpen;
    }

    public void setShowInstructions(boolean shouldShow) {
        showInstructions = shouldShow;
    }

    public void setEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public void setSelectedTree(int index) {
        selectedTree = index;
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
