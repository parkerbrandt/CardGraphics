package edu.ou.cs.cg.project.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import edu.ou.cs.cg.project.Model;
import edu.ou.cs.cg.project.View;
import edu.ou.cs.cg.utilities.Cube;
import edu.ou.cs.cg.utilities.Node;
import edu.ou.cs.cg.utilities.Transform;

import java.awt.*;
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

    protected int   cardIndex;          // The index of the card - used to determine if this is the "main" card

    private CardSide front;
    private CardSide back;

    private int rotateAngle;            // The amount the front face of card is opened

    private ArrayList<String>  text;     // All text on the card

    private ArrayList<CardImage> trees;    // The location of all trees on the inside of the card

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

        this.id = 0;

        cardIndex = 0;

        rotateAngle = 0;

        // Create the two parts of card
        this.pushTransform(new Transform.Translate(0.0f, -0.5f, 0.5f));

        // The "front" of the card
        front = new CardSide(textures, view, model);
        front.pushTransform(new Transform.Scale(0.5f, 0.8f, 0.01f));
        this.add(front);

        // The "back" of the card - should always be slightly "behind" the front
        // Flip the back and push back
        back = new CardSide(textures, view, model);
        back.pushTransform(new Transform.Scale(0.5f, 0.8f, 0.01f));
        back.pushTransform(new Transform.Rotate(0.0f, 1.0f, 0.0f, 180));
        back.pushTransform(new Transform.Translate(0.5f, 0.0f, -0.02f));
        this.add(back);


        // Add a sun at the top
        CardImage sun = new CardImage(6, textures, model);
        sun.pushTransform(new Transform.Scale(0.25f, 0.25f, 1.0f));
        sun.pushTransform(new Transform.Translate(0.5f, 0.5f, -0.08f));
        front.addImage(sun);

        // TODO: Make list of trees to rotate in depict
        // Add two default trees with trunks to the inside of the card front
        CardImage tree1 = new CardImage(3, textures, model);
        tree1.pushTransform(new Transform.Scale(0.25f, 0.25f, 1.0f));
        tree1.pushTransform(new Transform.Translate(0.5f, 0.2f, -0.08f));
        front.addTree(tree1);

        CardImage trunk1 = new CardImage(4, textures, model);
        trunk1.pushTransform(new Transform.Scale(0.25f, 0.25f, 1.0f));
        trunk1.pushTransform(new Transform.Translate(0.5f, 0.0f, -0.08f));
        front.addImage(trunk1);


        // Add some default text to the front
        String[] frontText = new String[] { "Hello,", "Good Morning"};
        CardText gm = new CardText(renderer, frontText);
        gm.pushTransform(new Transform.Translate(0.2f, 0.3f, 1.2f));
        gm.pushTransform(new Transform.Scale(0.8f, 0.8f, 1.0f));
        front.addText(gm);

        // Add some default text to the back inside part
        String[] inside = new String[] { "Graphics", "Final Project"};
        CardText in = new CardText(renderer, inside);
        // back.addTe


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

        // Rotate the front face of the card
        // Also rotate each tree
        if(model.isCardOpen()) {
            if(rotateAngle <= 180) {
                rotateAngle += 2;
                front.pushTransform(new Transform.Rotate(0.0f, 1.0f, 0.0f, -2));

                // TODO: private boolean openTree = true;
            }
        } else {
            if(rotateAngle >= 0) {
                rotateAngle -= 2;
                front.pushTransform(new Transform.Rotate(0.0f, 1.0f, 0.0f, 2));
            }
        }

        // Check if the color needs to be changed if this is the "main" card
        if(id == 0)
            setColor(model.getCardColor());

        // Render each side of the card
        front.render(gl);
        back.render(gl);
    }


    //****************************************
    // Public Methods
    //****************************************

    /**
     * Copies the colors and designs of the card
     * @param toCopy
     */
    public void copyCard(Card toCopy) {

        // Adjust the variables of this card to match that of the copy card

    }


    /**
     * Adds a tree to a side of the card, offset by a certain amount on the cards
     * @param dx x offset
     * @param dy y offset
     * @param scale scale of the tree
     * @param isFront put on the front or the back
     */
    public void addTree(float dx, float dy, float scale, boolean isFront) {

        // Create the new tree
        CardImage newTree = new CardImage(3, textures, model);
        newTree.pushTransform(new Transform.Translate(dx, dy, 0.0f));
        newTree.pushTransform(new Transform.Scale(scale, scale, 1.0f));

        // Add the tree to the intended side
        if(isFront)
            front.addTree(newTree);
        else
            back.add(newTree);
    }


    //****************************************
    // Getters and Setters
    //****************************************

    // Getters
    public int getId() {
        return id;
    }


    // Setters
    public void setColor(Color color) {
        // Set the color of the front and back of the card
        front.setColor(color);
        back.setColor(color);
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

        private Color color;

        private ArrayList<CardImage>    images;            // All of the images contained on this side
        private ArrayList<CardImage>    trees;             // All trees
        private ArrayList<CardImage>    clouds;            // All clouds
        private ArrayList<CardText>     text;               // All text on this side


        //****************************************
        // Constructors
        //****************************************
        public CardSide(Texture[] textures, View view, Model model) {
            super(textures);

            // Initialize variables
            this.view = view;
            this.model = model;

            color = model.getCardColor();

            images = new ArrayList<>();
            trees = new ArrayList<>();
            clouds = new ArrayList<>();
            text = new ArrayList<>();
        }


        //****************************************
        // Public Methods
        //****************************************

        // Add images to this side - specified by type of image
        public void addTree(CardImage image) {
            trees.add(image);
        }

        public void addCloud(CardImage image) {
            clouds.add(image);
        }

        public void addImage(CardImage image) {
            images.add(image);
        }

        public void addText(CardText string) {
            text.add(string);
        }


        //****************************************
        // Node Override Methods
        //****************************************
        @Override
        protected void change(GL2 gl) {

            // Adjust the movement of the trees and the clouds
            // once the clouds reach the right side of the card - remove and spawn a new one
            for(CardImage tree : trees) {
                // Rotate the trees up or down based on if the card is open or closed
                if(model.isCardOpen()) {

                }
            }

            // TODO: Need to utilize x and y for clouds
            for(CardImage cloud : clouds) {

            }
        }

        @Override
        protected void depict(GL2 gl) {

            // Depict as transformed cube with paper texture

            gl.glColor3f((float)color.getRed()/255.0f, (float)color.getGreen()/255.0f, (float)color.getBlue()/255.0f);

            Cube.fillFace(gl, 0, getTexture(2));

            // Color the inside of the card white
            gl.glColor3f(1.0f, 1.0f, 1.0f);

            Cube.fillFace(gl, 1, getTexture(2));
            Cube.fillFace(gl, 2, getTexture(2));
            Cube.fillFace(gl, 3, getTexture(2));
            Cube.fillFace(gl, 4, getTexture(2));
            Cube.fillFace(gl, 5, getTexture(2));


            // Draw all the images for this side of the card
            // Draw all trees
            for(CardImage tree : trees) {
                tree.render(gl);
            }

            // Draw all clouds
            for(CardImage cloud : clouds) {
                cloud.render(gl);
            }

            // Draw all other relevant images
            for(CardImage image : images) {
                image.render(gl);
            }

            // Draw all the text for this side of the card
            for(CardText line : text) {
                line.render(gl);
            }

        }


        //****************************************
        // Getters and Setters
        //****************************************

        // Getters
        public ArrayList<CardImage> getTrees() {
            return trees;
        }


        // Setters
        public void setColor(Color color) {
            this.color = color;
        }

        public void setTree(CardImage image, int index) {
            trees.set(index, image);
        }
    }



    /**
     * Inner Class to represent the images that are rendered onto the card
     */
    public static class CardImage extends Node {

        //****************************************
        // Private Variables
        //****************************************
        private int index;          // The index to keep track of the type of image being used
        public double x, y;        // The coordinates of the image

        //****************************************
        // Constructors
        //****************************************

        public CardImage(int index, Texture[] textures, Model model) {
            super(textures);

            // Initialize variables
            this.index = index;

            x = 0;
            y = 0;
        }


        //****************************************
        // Node Override Methods
        //****************************************

        @Override
        protected void change(GL2 gl) { }

        @Override
        protected void depict(GL2 gl) {

            // Draw the image on a Cube
            Cube.fillFace(gl, 0, getTexture(index));
            Cube.fillFace(gl, 1, getTexture(index));
        }
    }



    /**
     * Inner class to represent the text used on the inside and outside of the cards
     */
    public static class CardText extends Node {

        //****************************************
        // Private Variables
        //****************************************
        private TextRenderer renderer;
        private String[] text;


        //****************************************
        // Constructors
        //****************************************

        public CardText(TextRenderer renderer, String[] text) {
            // Initialize variables
            this.renderer = renderer;
            this.text = text;
        }


        //****************************************
        // Public Methods
        //****************************************

        /**
         * Change the text of this
         * @param newText
         */
        public void changeText(String[] newText) {
            this.text = newText;
        }


        //****************************************
        // Node Override Methods
        //****************************************

        @Override
        protected void depict(GL2 gl) {

            // Draw the text using 3D rendering
            renderer.begin3DRendering();

            for(int i = 0; i < text.length; i++) {
                renderer.draw3D(text[i], 0.0f, 0.8f - (0.1f * i), 0.0f, 0.01f);
            }

            renderer.end3DRendering();
        }
    }
}
