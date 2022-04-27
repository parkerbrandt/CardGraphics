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
        sun.pushTransform(new Transform.Translate(0.7f, 0.7f, -0.08f));
        front.addImage(sun);

        // Add three default trees to the inside of the card
        addTree(0.5f, 0.2f, 0.25f, true);
        addTree(0.1f, 0.35f, 0.25f, true);
        addTree(0.5f, 0.3f, 0.25f, false);

        // Add a few "random" clouds
        Random rand = new Random();
        for(int i = 0; i < rand.nextInt(5) + 1; i++) {
            float x = (float)rand.nextInt(50) / 100.0f;
            float y = (float)rand.nextInt(20) / 100.0f + 0.4f;

            addCloud(x, y, 0.25f, rand.nextBoolean());
        }

        // Add some default text to the front
        String[] frontText = model.getFrontText();
        CardText gm = new CardText(renderer, frontText);
        gm.pushTransform(new Transform.Translate(0.2f, 0.3f, 1.2f));
        gm.pushTransform(new Transform.Scale(0.8f, 0.8f, 1.0f));
        front.setText(gm);

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
    protected void change(GL2 gl) {

        // Check if the text on the front side needs to be updated
        front.changeText(model.getFrontText());
    }

    @Override
    protected void depict(GL2 gl) {

        // Rotate the front face of the card
        // Also rotate each tree
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
     * TODO: Store tree location information
     *
     * @param dx x offset
     * @param dy y offset
     * @param scale scale of the tree
     * @param isFront put on the front or the back
     */
    public void addTree(float dx, float dy, float scale, boolean isFront) {

        // Create the new tree
        CardImage newTree = new CardImage(3, textures, model);
        newTree.pushTransform(new Transform.Scale(scale, scale, 1.0f));
        newTree.pushTransform(new Transform.Translate(dx, dy, -0.08f));

        // Add the trunk underneath
        CardImage trunk = new CardImage(4, textures, model);
        trunk.pushTransform(new Transform.Scale(scale, scale, 1.0f));
        trunk.pushTransform(new Transform.Translate(dx, dy - 0.2f, -0.08f));

        // Add the tree to the intended side
        if(isFront) {
            front.addTree(newTree, dx, dy);
            front.add(trunk);
        } else {
            back.addTree(newTree, dx, dy);
            back.add(trunk);
        }
    }

    /**
     * Adds a cloud to a side of the card
     * @param dx
     * @param dy
     * @param scale
     * @param isFront
     */
    public void addCloud(float dx, float dy, float scale, boolean isFront) {

        // Create the cloud
        CardImage newCloud = new CardImage(7, textures, model);
        newCloud.pushTransform(new Transform.Scale(scale, scale, 1.0f));
        newCloud.pushTransform(new Transform.Translate(dx, dy, -0.08f));

        // Add the cloud to the intended side
        if(isFront)
            front.addCloud(newCloud, dx, dy);
        else
            back.addCloud(newCloud, dx, dy);
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

        private Color color;        // The color of the outside of the card
        private Color inColor;      // The color of the inside of the card

        private ArrayList<CardImage>        images;             // All of the images contained on this side
        private ArrayList<CardImage>        trees;              // All trees
        private ArrayList<Point2D.Float>    treeLoc;            // All tree locations

        private ArrayList<CardImage>        clouds;             // All clouds
        private ArrayList<Point2D.Float>    cloudLoc;           // All cloud locations

        private CardText   text;               // All text on this side

        private float treeRotate;       // The angle of rotation of the trees


        //****************************************
        // Constructors
        //****************************************
        public CardSide(Texture[] textures, View view, Model model) {
            super(textures);

            // Initialize variables
            this.view = view;
            this.model = model;

            color = model.getCardColor();
            inColor = new Color(255, 255, 255);

            images = new ArrayList<>();
            trees = new ArrayList<>();
            treeLoc = new ArrayList<>();
            clouds = new ArrayList<>();
            cloudLoc = new ArrayList<>();

            treeRotate = 180;

            text = new CardText(view.getRenderer(), new String[] {""});
        }


        //****************************************
        // Public Methods
        //****************************************

        // Add images to this side - specified by type of image
        public void addTree(CardImage image, float dx, float dy) {
            trees.add(image);
            treeLoc.add(new Point2D.Float(dx, dy));
        }

        public void addCloud(CardImage image, float dx, float dy) {
            clouds.add(image);
            cloudLoc.add(new Point2D.Float(dx, dy));
        }

        public void addImage(CardImage image) {
            images.add(image);
        }

        public void changeText(String[] string) {
            text.changeText(string);
        }


        //****************************************
        // Node Override Methods
        //****************************************
        @Override
        protected void change(GL2 gl) {

            // Adjust the shading on the inside of the card for a day/night cycle when the card is open
            if(model.isCardOpen()) {
                int col = inColor.getRed() - 1;
                if (col < 25)
                    col = 255;
                inColor = new Color(col, col, col);
            }

            // Adjust the movement of the trees and the clouds
            // once the clouds reach the right side of the card - remove and spawn a new one
            for(CardImage tree : trees) {
                // Rotate the trees up or down based on if the card is open or closed
                if(model.isCardOpen()) {
                    if(treeRotate >= 0) {
                        //tree.pushTransform(new Transform.Rotate(1.0f, 0.0f, 0.0f, -2f));
                        //treeRotate -= 2;
                    }
                } else {
                    if(treeRotate <= 180) {
                        //tree.pushTransform(new Transform.Rotate(1.0f, 0.0f, 0.0f, 2f));
                        //treeRotate += 2;
                    }
                }
            }

            // TODO: Need to utilize x and y for clouds
            for(int i = 0; i < clouds.size(); i++) {
                // Check if cloud needs to be reset
                if(cloudLoc.get(i).x > 1.0) {
                    cloudLoc.set(i, new Point2D.Float());
                }
            }
        }

        @Override
        protected void depict(GL2 gl) {

            // TODO: Check if card has been reset

            // Depict as transformed cube with paper texture

            gl.glColor3f((float)color.getRed()/255.0f, (float)color.getGreen()/255.0f, (float)color.getBlue()/255.0f);

            Cube.fillFace(gl, 0, getTexture(2));

            // Color the inside of the card white
            gl.glColor3f((float)inColor.getRed()/255.0f, (float)inColor.getGreen()/255.0f, (float)inColor.getBlue()/255.0f);

            Cube.fillFace(gl, 1, getTexture(2));
            Cube.fillFace(gl, 2, getTexture(2));
            Cube.fillFace(gl, 3, getTexture(2));
            Cube.fillFace(gl, 4, getTexture(2));
            Cube.fillFace(gl, 5, getTexture(2));


            // Draw all the images for this side of the card
            // Draw all trees
            // TODO: If in edit mode, get selected tree and draw a golden square around
            for(int i = 0; i < trees.size(); i++) {
                // Check the selected tree bounds
                if(model.getSelectedTree() >= trees.size())
                    model.setSelectedTree(0);

                // Check if we should draw a box around the tree
                if(model.isEditMode() && i == model.getSelectedTree()) {

                    gl.glColor3f(1.0f, 215.0f/255.0f, 0.0f);

                    gl.glBegin(GL2.GL_QUADS);

                    gl.glEnd();
                }

                trees.get(i).render(gl);
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
            text.render(gl);

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

        public void setText(CardText newText) {
            text = newText;
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
