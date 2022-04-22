package edu.ou.cs.cg.project.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import edu.ou.cs.cg.project.Model;
import edu.ou.cs.cg.project.View;
import edu.ou.cs.cg.utilities.Cube;
import edu.ou.cs.cg.utilities.Node;
import edu.ou.cs.cg.utilities.Point3D;
import edu.ou.cs.cg.utilities.Transform;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * The Card Class
 * Represents the Cards that the user can interact and modify
 * Handles drawing, initializing, calculations, and saving/loading from a csv file
 *
 * @author Parker Brandt
 */
public class Card extends Node {

    //****************************************
    // Private Class Members
    //****************************************
    private static final String TREE_IMG_FILE =     "images/tree.jpg";
    private static final String TREE_TRUNK_FILE =   "images/trunk.jpg";
    private static final String APPLE_IMG_FILE =    "images/apple.jpg";

    //****************************************
    // Private Variables
    //****************************************
    private View    view;               // The corresponding view class
    private Model   model;              // The corresponding model class

    private int     id;                 // The unique ID of the card

    private CardSide front;
    private CardSide back;

    private Color inColor;              // The color of the inside of the card - will always be the same

    private boolean isOpen;             // Decide if the card is open or closed
    private int rotateAngle;            // The amount the front face of card is opened

    private Point3D loc;                // The x,y,z location of the bottom left of the card
    private int width;                  // The width of each side of the card
    private int height;                 // The height of each side of the card

    private ArrayList<String>  text;     // All text on the card

    private ArrayList<Point2D.Double> trees;    // The location of all trees on the inside of the card

    private int time;       // The time of day on the card - ranges from 0 to 1800 (30 seconds) for day/night cycle


    //****************************************
    // Constructor
    //****************************************

    /**
     * Default Constructor
     * Creates a base card in the user's hand
     */
    public Card(Texture[] textures, View view, Model model) {
        super(textures);

        // Initialize variables
        this.view = view;
        this.model = model;

        Random rand = new Random();
        this.id = rand.nextInt(1000);

        // Create the two parts of card
        // The "front" of the card
        front = new CardSide(textures, view, model);
        front.pushTransform(new Transform.Scale(0.1f, 0.5f, 0.01f));
        this.add(front);

        // The "back" of the card - should always be slightly "behind" the front
        back = new CardSide(textures, view, model);
        back.pushTransform(new Transform.Translate(0.0f, 0.0f, 0.1f));
        back.pushTransform(new Transform.Scale(0.1f, 0.5f, 0.01f));
        this.add(back);

        // Load the tree, trunk, and apple images

        isOpen = false;
        rotateAngle = 0;


        // Add some default text
        text = new ArrayList<>();
        text.add("The Default Card");

        // Add two trees to the inside
        trees = new ArrayList<>();

        // Start the day/night cycle
        time = 0;
    }

    /**
     * Loads a card from a CSV file and initializes data
     * @param filename the file location/name
     */
    public Card(String filename, Texture[] textures, View view, Model model) {
        super(textures);

        // Initialize variables
        this.view = view;
        this.model = model;

        time = 0;

        // Load the data from a file
        try {
            model.load(filename, textures);
        } catch(IOException e) {
            e.printStackTrace();
        }

        // TODO: Display on shelf
        int cardIndex;
    }


    //****************************************
    // Node Override Methods
    //****************************************

    @Override
    protected void change(GL2 gl) {

    }

    @Override
    protected void depict(GL2 gl) {

        // Depict the front and back of the card
        // TODO: Rotate front by value in thing
        front.depict(gl);
        back.depict(gl);
    }


    //****************************************
    // Private Methods
    //****************************************

    /**
     * Copies the colors and designs of the card
     * @param toCopy
     */
    public void copyCard(Card toCopy) {

        // Adjust the variables of this card to match that of the copy card

    }


    //****************************************
    // Getters and Setters
    //****************************************

    // Getters
    public int getId() {
        return id;
    }


    /**
     * Inner Class to represent each side of a card
     */
    public static class CardSide extends Node {

        //****************************************
        // Private Variables
        //****************************************
        private View view;
        private Model model;


        //****************************************
        // Constructors
        //****************************************
        public CardSide(Texture[] textures, View view, Model model) {
            super(textures);

            // Initialize variables
            this.view = view;
            this.model = model;

            // Scale the card to the appropriate size for a card
            pushTransform(new Transform.Scale(0.1f, 0.0f, 0.01f));
        }


        //****************************************
        // Node Override Methods
        //****************************************
        @Override
        protected void depict(GL2 gl) {

            // Depict as transformed cube with paper texture

            // Get the color of the outside of the card
            // TODO: Make inside of card always white
            Color out = model.getCardColor();
            gl.glColor3f((float)out.getRed()/255.0f, (float)out.getGreen()/255.0f, (float)out.getBlue()/255.0f);

            Cube.fillFace(gl, 0, getTexture(2));
            Cube.fillFace(gl, 1, getTexture(2));
            Cube.fillFace(gl, 2, getTexture(2));
            Cube.fillFace(gl, 3, getTexture(2));
            Cube.fillFace(gl, 4, getTexture(2));
            Cube.fillFace(gl, 5, getTexture(2));
        }
    }
}
