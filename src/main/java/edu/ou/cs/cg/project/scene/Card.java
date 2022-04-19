package edu.ou.cs.cg.project.scene;

import com.jogamp.opengl.GL2;
import edu.ou.cs.cg.utilities.Node;
import edu.ou.cs.cg.utilities.Point3D;

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
    private int     id;                 // The unique ID of the card
    private Color   outColor;           // The color of the outside of the card
    private Color   inColor;            // The color of the inside of the card

    private boolean isOpen;             // Decide if the card is open or closed
    private int rotateAngle;            // The amount the front face of card is opened

    private Point3D loc;                // The x,y,z location of the bottom left of the card
    private int width;                  // The width of each side of the card
    private int height;                 // The height of each side of the card

    private ArrayList<String>  text;     // All text on the card

    private ArrayList<Point2D.Double> trees;    // The location of all trees on the inside of the card

    private int time;       // The time of day on the card - ranges from 0 to 1800 (30 seconds) for day/night cycle


    //****************************************
    // Public Variables
    //****************************************




    //****************************************
    // Constructor
    //****************************************

    /**
     * Default Constructor
     * Creates a base card in the user's hand
     */
    public Card() {

        // Initialize variables
        Random rand = new Random();
        this.id = rand.nextInt(1000);

        outColor = new Color(255, 0, 0);
        inColor = new Color(255, 230, 230);

        // Initialize location and dimensions of the card


        isOpen = false;
        rotateAngle = 0;


        // Add some default text
        text.add("The Default Card");

        // Add two trees to the inside


        // Start the day/night cycle
        time = 0;
    }

    /**
     * Loads a card from a CSV file and initializes data
     * @param filename the file location/name
     */
    public Card(String filename) {

        // Initialize variables
        time = 0;

        // Load the data from a file
        try {
            load(filename);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }


    //****************************************
    // Node Override Methods
    //****************************************

    @Override
    protected void change(GL2 gl) {

    }

    @Override
    protected void depict(GL2 gl) {

        // Update the day/night cycle
        time++;

        // Determine if card should be opened or not
        if(isOpen) {
            if(rotateAngle < 180)
                rotateAngle += 5;
        } else {
            if(rotateAngle > 15)
                rotateAngle -= 5;
        }

        // Create the back part of the card
        // TODO: Could create card sides as cubes and bind textures
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
    public void load(String filename) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line = "";
        while((line = reader.readLine()) != null) {

        }
    }


    //****************************************
    // Getters and Setters
    //****************************************

    // Getters
    public Color getOutColor() {
        return outColor;
    }

    public Color getInColor() {
        return inColor;
    }

    public Point3D getLoc() {
        return loc;
    }


    // Setters


}
