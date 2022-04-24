package edu.ou.cs.cg.project.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
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
    // Private Variables
    //****************************************
    private final View          view;               // The corresponding view class
    private final Model         model;              // The corresponding model class
    private final TextRenderer  renderer;           // The text renderer

    private int     id;                 // The unique ID of the card

    private int     cardIndex;          // The index of the card - used to determine if this is the "main" card

    private CardSide front;
    private CardSide back;

    private int rotateAngle;            // The amount the front face of card is opened

    private ArrayList<String>  text;     // All text on the card

    private ArrayList<Point2D.Double> trees;    // The location of all trees on the inside of the card

    private int time;                    // The time of day on the card - ranges from 0 to 1800 (30 seconds) for day/night cycle


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
        this.renderer = view.getRenderer();

        Random rand = new Random();
        this.id = rand.nextInt(1000);

        cardIndex = 0;

        rotateAngle = 0;

        // Create the two parts of card
        this.pushTransform(new Transform.Translate(0.0f, -0.5f, 0.5f));

        // The "front" of the card

        front = new CardSide(textures, view, model);
        front.pushTransform(new Transform.Scale(0.5f, 0.8f, 0.01f));
        this.add(front);

        // The "back" of the card - should always be slightly "behind" the front
        back = new CardSide(textures, view, model);
        back.pushTransform(new Transform.Scale(0.5f, 0.8f, 0.01f));
        this.add(back);

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
    public Card(String filename, Texture[] textures, View view, Model model, int index) {
        super(textures);

        // Initialize variables
        this.view = view;
        this.model = model;
        this.renderer = view.getRenderer();

        cardIndex = index;

        time = 0;

        // Load the data from a file
        try {
            model.load(filename, textures);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    //****************************************
    // Node Override Methods
    //****************************************

    @Override
    protected void change(GL2 gl) { }

    @Override
    protected void depict(GL2 gl) {

        // TODO: Get card translation from model

        // Rotate the front face of the card
        if(model.isCardOpen()) {
            if(rotateAngle <= 180) {
                rotateAngle += 2;
                front.pushTransform(new Transform.Rotate(0.0f, 1.0f, 0.0f, -2));
            }
        } else {
            if(rotateAngle >= 0) {
                rotateAngle -= 2;
                front.pushTransform(new Transform.Rotate(0.0f, 1.0f, 0.0f, 2));
            }
        }


        // Render each side of the card
        front.render(gl);
        back.render(gl);
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

        private ArrayList<CardImage> images;


        //****************************************
        // Constructors
        //****************************************
        public CardSide(Texture[] textures, View view, Model model) {
            super(textures);

            // Initialize variables
            this.view = view;
            this.model = model;

            images = new ArrayList<>();
        }


        //****************************************
        // Node Override Methods
        //****************************************
        @Override
        protected void depict(GL2 gl) {

            // Depict as transformed cube with paper texture

            // Get the color of the outside of the card
            Color out = model.getCardColor();
            gl.glColor3f((float)out.getRed()/255.0f, (float)out.getGreen()/255.0f, (float)out.getBlue()/255.0f);

            Cube.fillFace(gl, 0, getTexture(2));
            Cube.fillFace(gl, 1, getTexture(2));
            Cube.fillFace(gl, 2, getTexture(2));
            Cube.fillFace(gl, 3, getTexture(2));
            Cube.fillFace(gl, 4, getTexture(2));
            Cube.fillFace(gl, 5, getTexture(2));

            // Color the inside of the card white - may be able to use just some quads for a cartoony look
            gl.glColor3f(1.0f, 1.0f, 1.0f);
            gl.glBegin(GL2.GL_QUADS);

            gl.glEnd();
        }
    }


    /**
     * Inner Class to represent the images that are rendered onto the card
     */
    public static class CardImage extends Node {

        //****************************************
        // Private Variables
        //****************************************
        private int index;

        //****************************************
        // Constructors
        //****************************************

        public CardImage(int index, Texture[] textures) {
            super(textures);

            // Initialize variables
            this.index = index;
        }


        //****************************************
        // Node Override Methods
        //****************************************

        @Override
        protected void depict(GL2 gl) {

            // Draw the image on a Cube
            Cube.fillFace(gl, 0, getTexture(index));
        }
    }
}
