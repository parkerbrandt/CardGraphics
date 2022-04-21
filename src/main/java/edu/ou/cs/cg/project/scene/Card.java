package edu.ou.cs.cg.project.scene;

import com.jogamp.opengl.GL2;
import edu.ou.cs.cg.project.Model;
import edu.ou.cs.cg.project.View;
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
    public Card(View view, Model model) {

        // Initialize variables
        this.view = view;
        this.model = model;

        Random rand = new Random();
        this.id = rand.nextInt(1000);

        // Load the tree, trunk, and apple images


        // Initialize location and dimensions of the card


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
    public Card(String filename, View view, Model model) {

        // Initialize variables
        this.view = view;
        this.model = model;

        time = 0;

        // Load the data from a file
        try {
            model.load(filename);
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

        // Update the day/night cycle
        time++;

        // Determine to alter the colors on the sky of the card by the time


        // Determine if card should be opened or not
        if(isOpen) {
            if(rotateAngle < 180)
                rotateAngle += 5;
        } else {
            if(rotateAngle > 15)
                rotateAngle -= 5;
        }

        // pushTransform(new Transform.Rotate(0.0f, 1.0f, 0.0f, rotateAngle));

        // TODO: Could create card sides as cubes and bind textures
        // TODO: Let user just look at the room
        // TODO: Need to add check where to show card

        // pushTransform(new Transform.Scale(0.1f, 0.3f, 0.01f));

        // The "front" of the card
        // TODO: Implement a Front and Back class or just two cubes

        // The "back" of the card


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

}
