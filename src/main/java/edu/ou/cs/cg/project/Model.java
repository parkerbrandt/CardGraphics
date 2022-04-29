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
import java.net.URL;
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

    private boolean isCardOpen;         // Used to show if the card is open or not
    private boolean showInstructions;   // Used to show if the instructions should be open or not
    private boolean isEditMode;         // Use to determine if the user is editing the card or not
    private boolean reset;

    private final Color[] cardColors;   // List of all available card colors
    private int currentColor;           // Current index of selected color

    private double cardX, cardY;

    // TODO: Move cards here
    private ArrayList<Card> displayCards;

    private Texture[] textures;

    private int selectedTree;
    private boolean isFrontTree;

    private String[] frontText;
    private String[] insideText;

    private boolean showFront;

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
        reset = false;

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

        // Load first 9 display cards from the cards resource folder
        displayCards = new ArrayList<>();

        selectedTree = 0;
        isFrontTree = true;

        frontText = new String[] {"Hello", "Good Morning"};
        insideText = new String[] {"Have a", "good day!"};

        showFront = false;

        in = new Scanner(System.in);
    }


    //****************************************
    // Private Methods
    //****************************************

    // File save/load methods
    /**
     * Save the held card data to a file in the /cards/ directory
     * Save in the format "id,color,
     */
    public void save() {

        try {
            URL url = Model.class.getResource("/cards/");
            if(url != null) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(url.getPath() + "card" + view.getMainCard().getId() + ".csv"));

                String output = view.getMainCard().getId() + "," + currentColor + "," + frontText[0] + "," + insideText[0];
                writer.write(output);

                writer.close();
            }

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the card data from the specified filename
     * CSV Format: ID,Color,FrontText,InnerText,TreeLocations
     * @param filename
     * @throws IOException
     */
    private Card load(String filename, Texture[] textures, int index) throws IOException {

        // Card Variables
        Card add;
        int id;
        int colorIdx = 0;
        String frontText = "", inText = "";
        ArrayList<Point2D.Float> trees = new ArrayList<>();
        ArrayList<Boolean> treeSide = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = "";
        while((line = reader.readLine()) != null) {
            String[] data = line.split(",");

            id = Integer.parseInt(data[0]);
            colorIdx = Integer.parseInt(data[1]);
            frontText = data[2];
            inText = data[3];

            // Read in the available tree data
            if(data.length > 4) {
                for(int i = 4; i < data.length; i += 3) {
                    // Check if this belongs on the front or back of the card
                    treeSide.add(data[i].equals("front"));

                    // Get the dimensions
                    float dx = Float.parseFloat(data[i+1]);
                    float dy = Float.parseFloat(data[i+2]);
                    trees.add(new Point2D.Float(dx, dy));
                }
            }

        }
        reader.close();

        // Create the card
        add = new Card(view.getTextures(), view, this, index);
        add.setColor(cardColors[colorIdx]);
        add.addText(frontText, true);
        add.addText(inText, false);

        // Add trees to the card
        for(int i = 0; i < trees.size(); i++) {
            add.addTree(trees.get(i).x, trees.get(i).y, 0.25f, treeSide.get(i));
        }

        return add;
    }


    // Card Modification Methods

    /**
     * Loads the cards from the /cards/ directory and displays the first 9 on the shelves
     */
    public void loadDisplayCards() {

        for(int i = 0; i < 9; i++) {
            // Get the URL of the file
            URL url = Model.class.getResource("/cards/card0" + i + ".csv");

            // If not null, load the file
            if(url != null) {
                try {
                    displayCards.add(load(url.getPath(), textures, i));
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
     * Switch the currently selected tree
     * If out of bounds of tree array, will be modified in Card class
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
     * Move the selected tree up
     * TODO: Check bounds
     * @param amt
     */
    public void moveTreeUp(float amt) {

        ArrayList<Point2D.Float> tr;
        if(isFrontTree) {
            tr = view.getMainCard().getFront().getTreeLoc();
            if(tr.get(selectedTree).y + amt > 0.2 && tr.get(selectedTree).y + amt < 0.45)
                view.getMainCard().getFront().setTreeLoc(tr.get(selectedTree).x, tr.get(selectedTree).y + amt, selectedTree);
        } else {
            tr = view.getMainCard().getBack().getTreeLoc();
            if(tr.get(selectedTree).y + amt > 0.2 && tr.get(selectedTree).y + amt < 0.45)
                view.getMainCard().getBack().setTreeLoc(tr.get(selectedTree).x, tr.get(selectedTree).y + amt, selectedTree);
        }
    }

    /**
     * Move the selected tree to the right
     * TODO: Check bounds
     * @param amt
     */
    public void moveTreeRight(float amt) {

        ArrayList<Point2D.Float> tr;
        if(isFrontTree) {
            tr = view.getMainCard().getFront().getTreeLoc();
            if(tr.get(selectedTree).x + amt > 0 && tr.get(selectedTree).x + amt < 0.7)
                view.getMainCard().getFront().setTreeLoc(tr.get(selectedTree).x + amt, tr.get(selectedTree).y, selectedTree);
        } else {
            tr = view.getMainCard().getBack().getTreeLoc();
            if(tr.get(selectedTree).x + amt > 0 && tr.get(selectedTree).x + amt < 0.7)
                view.getMainCard().getBack().setTreeLoc(tr.get(selectedTree).x + amt, tr.get(selectedTree).y, selectedTree);
        }
    }

    /**
     * Will modify a boolean to tell the card if it should reset or not
     */
    public void resetCard(boolean shouldReset) {
        reset = shouldReset;
    }

    /**
     * Allows the user to change the text on the card
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

    public boolean isReset() {
        return reset;
    }

    public ArrayList<Card> getDisplayCards() {
        return displayCards;
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

    public boolean isShowFront() {
        return showFront;
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

    public void setCurrentColor(int index) {
        currentColor = index;
    }

    public void setFrontText(String[] text) {
        frontText = text;
    }

    public void setInsideText(String[] text) {
        insideText = text;
    }

    public void setShowFront(boolean showFront) {
        this.showFront = showFront;
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
