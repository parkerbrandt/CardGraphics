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

    private CardImage frontImg;

    private int rotateAngle;            // The amount the front face of card is opened


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
        front.setFront(true);
        this.add(front);

        // The "back" of the card - should always be slightly "behind" the front
        // Flip the back and push back
        back = new CardSide(textures, view, model);
        back.pushTransform(new Transform.Scale(0.5f, 0.8f, 0.01f));
        back.pushTransform(new Transform.Rotate(0.0f, 1.0f, 0.0f, 180));
        back.pushTransform(new Transform.Translate(0.5f, 0.0f, -0.02f));
        this.add(back);

        // Add the front image
        frontImg = new CardImage(12, textures, model);
        frontImg.pushTransform(new Transform.Scale(0.25f, 0.25f, 1.0f));
        frontImg.pushTransform(new Transform.Translate(0.35f, 0.3f, 0.08f));

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

        // Add some text to the front
        String[] frontText = model.getFrontText();
        CardText frt = new CardText(renderer, frontText);
        frt.pushTransform(new Transform.Translate(0.2f, 0.3f, 1.2f));
        frt.pushTransform(new Transform.Scale(0.8f, 0.8f, 1.0f));
        front.setText(frt);

        // Add some text to the inside on the right
        String[] insideText = model.getInsideText();
        CardText in = new CardText(renderer, insideText);
        in.pushTransform(new Transform.Scale(0.5f, 0.5f, 1.0f));
        in.pushTransform(new Transform.Rotate(0.0f, 1.0f, 0.0f, 180));
        in.pushTransform(new Transform.Translate(0.7f, 0.4f, -1.5f));
        back.setText(in);
    }

    /**
     * Creates a new card based off of data input in a CSV file in the /cards/ directory
     * Trees and text will be added manually by model class
     * Need to add clouds and sun
     */
    public Card(Texture[] textures, View view, Model model, int index) {
        super(textures);

        // Initialize variables
        this.view = view;
        this.model = model;
        this.renderer = view.getRenderer();

        cardIndex = index;

        // Create the front and back of the card
        // The "front" of the card
        front = new CardSide(textures, view, model);
        front.pushTransform(new Transform.Scale(0.5f, 0.8f, 0.01f));
        front.setFront(true);
        this.add(front);

        // The "back" of the card - should always be slightly "behind" the front
        // Flip the back and push back
        back = new CardSide(textures, view, model);
        back.pushTransform(new Transform.Scale(0.5f, 0.8f, 0.01f));
        back.pushTransform(new Transform.Rotate(0.0f, 1.0f, 0.0f, 180));
        back.pushTransform(new Transform.Translate(0.5f, 0.0f, -0.02f));
        this.add(back);

        // Add the front image
        frontImg = new CardImage(12, textures, model);
        frontImg.pushTransform(new Transform.Scale(0.25f, 0.25f, 1.0f));
        frontImg.pushTransform(new Transform.Translate(0.35f, 0.3f, 0.08f));

        // Add a sun at the top
        CardImage sun = new CardImage(6, textures, model);
        sun.pushTransform(new Transform.Scale(0.25f, 0.25f, 1.0f));
        sun.pushTransform(new Transform.Translate(0.7f, 0.7f, -0.08f));
        front.addImage(sun);

        // Add a few "random" clouds
        Random rand = new Random();
        for(int i = 0; i < rand.nextInt(5) + 1; i++) {
            float x = (float)rand.nextInt(50) / 100.0f;
            float y = (float)rand.nextInt(20) / 100.0f + 0.4f;

            addCloud(x, y, 0.25f, rand.nextBoolean());
        }

        // Add the default text to keep the correct transformations
        // Add some text to the front
        String[] frontText = model.getFrontText();
        CardText frt = new CardText(renderer, frontText);
        frt.pushTransform(new Transform.Translate(0.2f, 0.3f, 1.2f));
        frt.pushTransform(new Transform.Scale(0.8f, 0.8f, 1.0f));
        front.setText(frt);

        // Add some text to the inside on the right
        String[] insideText = model.getInsideText();
        CardText in = new CardText(renderer, insideText);
        in.pushTransform(new Transform.Scale(0.5f, 0.5f, 1.0f));
        in.pushTransform(new Transform.Rotate(0.0f, 1.0f, 0.0f, 180));
        in.pushTransform(new Transform.Translate(0.7f, 0.4f, -1.5f));
        back.setText(in);
    }


    //****************************************
    // Node Override Methods
    //****************************************

    @Override
    protected void change(GL2 gl) {

        // Check if the card has been reset
        if(model.isReset() && cardIndex == 0) {

            // Set the default color and text back to normal
            model.setCurrentColor(0);
            model.setFrontText(new String[] {"Hello", "Good Morning"});
            model.setInsideText(new String[] {"Have a", "good day!"});

            // Reset to have 3 trees in the correct starting positions
            front.removeTrees();
            back.removeTrees();

            addTree(0.5f, 0.2f, 0.25f, true);
            addTree(0.1f, 0.35f, 0.25f, true);
            addTree(0.5f, 0.3f, 0.25f, false);

            // Tell the model we dont have to reset anymore
            model.resetCard(false);
        }

        // Check if the clouds need to be switched to the other side
        ArrayList<Integer> switchClouds = new ArrayList<>();

        // Check the front
        for(int i = 0; i < front.getCloudLoc().size(); i++) {
            if(front.getCloudLoc().get(i).x <= 0)
                switchClouds.add(i);
        }

        // Add any clouds to the back
        for(int j = 0; j < switchClouds.size(); j++) {
            addCloud(0.7f, front.getCloudLoc().get(switchClouds.get(j)).y, 0.25f, false);
            front.removeCloud(switchClouds.get(j));
        }


        // Check the back
        switchClouds = new ArrayList<>();
        for(int i = 0; i < back.getCloudLoc().size(); i++) {
            if(back.getCloudLoc().get(i).x <= 0)
                switchClouds.add(i);
        }

        // Add any clouds to the front
        for(int j = 0; j < switchClouds.size(); j++) {
            addCloud(0.7f, back.getCloudLoc().get(switchClouds.get(j)).y, 0.25f, true);
            back.removeCloud(switchClouds.get(j));
        }



        // Check if the text on either side needs to be updated
        if(cardIndex == 0) {
            front.changeText(model.getFrontText());
            back.changeText(model.getInsideText());
        }
    }

    @Override
    protected void depict(GL2 gl) {

        // Rotate the front face of the card
        if(model.isCardOpen() && cardIndex == 0) {
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
        if(cardIndex == 0)
            setColor(model.getCardColor());

        // Check if the front image should be rendered
        if(model.isShowFront() && cardIndex == 0)
            front.add(frontImg);
        else
            front.remove(frontImg);

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
        this.id = toCopy.getId();

        this.front = toCopy.getFront();
        this.back = toCopy.getBack();
    }


    /**
     * Adds a tree to a side of the card, offset by a certain amount on the cards
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
            front.addTree(newTree, trunk, dx, dy);
        } else {
            back.addTree(newTree, trunk, dx, dy);
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

    /**
     * Adds some text to either the front or inside of the card
     * @param text
     * @param isFront
     */
    public void addText(String text, boolean isFront) {

        if(isFront) {
            front.changeText(new String[] {text});
        } else {
            back.changeText(new String[] {text});
        }
    }


    //****************************************
    // Getters and Setters
    //****************************************

    // Getters
    public int getId() {
        return id;
    }

    public CardSide getFront() {
        return front;
    }

    public CardSide getBack() {
        return back;
    }


    // Setters
    public void setColor(Color color) {
        // Set the color of the front and back of the card
        front.setColor(color);
        back.setColor(color);
    }

    public void setCardIndex(int index) {
        cardIndex = index;
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
        private ArrayList<CardImage>        trunks;             // Trunks for the trees

        private ArrayList<CardImage>        clouds;             // All clouds
        private ArrayList<Point2D.Float>    cloudLoc;           // All cloud locations

        private CardText text;                                // All text on this side

        private boolean isFront;


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
            trunks = new ArrayList<>();
            clouds = new ArrayList<>();
            cloudLoc = new ArrayList<>();

            isFront = false;

            text = new CardText(view.getRenderer(), new String[] {""});
        }


        //****************************************
        // Public Methods
        //****************************************

        // Add images to this side - specified by type of image
        public void addTree(CardImage image, CardImage trunk, float dx, float dy) {
            trees.add(image);
            treeLoc.add(new Point2D.Float(dx, dy));

            // Add the trunk
            trunks.add(trunk);
        }

        // Remove all trees from this side
        public void removeTrees() {
            trees = new ArrayList<>();
            treeLoc = new ArrayList<>();
            trunks = new ArrayList<>();
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
            if(model.isCardOpen() && view.getCounter() % 3 == 0) {
                int col = inColor.getRed() - 1;
                if (col < 25)
                    col = 255;
                inColor = new Color(col, col, col);
            }

            // Adjust tree locations
            for(int i = 0; i < trees.size(); i++) {
                trees.get(i).popTransform();
                trees.get(i).pushTransform(new Transform.Translate(treeLoc.get(i).x, treeLoc.get(i).y, -0.08f));

                trunks.get(i).popTransform();
                trunks.get(i).pushTransform(new Transform.Translate(treeLoc.get(i).x, treeLoc.get(i).y - 0.2f, -0.08f));
            }

            // Adjust movement of the clouds
            for(int i = 0; i < clouds.size(); i++) {

                // Move the clouds slightly when open
                if(model.isCardOpen()) {

                    // check the bounds, reset if necessary
                    if(cloudLoc.get(i).x > 0) {
                        cloudLoc.set(i, new Point2D.Float(cloudLoc.get(i).x - 0.001f, cloudLoc.get(i).y));
                        clouds.get(i).pushTransform(new Transform.Translate(-0.001f, 0.0f, 0.0f));
                    }
                }
            }
        }

        @Override
        protected void depict(GL2 gl) {

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
            // If in edit mode, get selected tree and draw a golden square around
            for(int i = 0; i < trees.size(); i++) {
                // Check the selected tree bounds
                if(model.getSelectedTree() > trees.size())
                    model.switchTreeSide();

                // Check if we should draw a box around the tree
                if(model.isEditMode() && i == model.getSelectedTree() && model.isFrontTree() == isFront) {

                    gl.glColor3f(1.0f, 215.0f/255.0f, 0.0f);

                    gl.glBegin(GL2.GL_LINE_LOOP);

                    gl.glVertex3f(treeLoc.get(i).x, treeLoc.get(i).y, -0.08f);
                    gl.glVertex3f(treeLoc.get(i).x, treeLoc.get(i).y + 0.25f, -0.08f);
                    gl.glVertex3f(treeLoc.get(i).x + 0.25f, treeLoc.get(i).y + 0.25f, -0.08f);
                    gl.glVertex3f(treeLoc.get(i).x + 0.25f, treeLoc.get(i).y, -0.08f);

                    gl.glEnd();
                }

                trees.get(i).render(gl);
                trunks.get(i).render(gl);
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

        public ArrayList<Point2D.Float> getTreeLoc() {
            return treeLoc;
        }

        public ArrayList<CardImage> getClouds() {
            return clouds;
        }

        public ArrayList<Point2D.Float> getCloudLoc() {
            return cloudLoc;
        }

        // Setters
        public void setColor(Color color) {
            this.color = color;
        }

        public void setTree(CardImage image, int index) {
            trees.set(index, image);
        }

        public void setTreeLoc(float dx, float dy, int index) {
            treeLoc.set(index, new Point2D.Float(dx, dy));
        }

        public void setText(CardText newText) {
            text = newText;
        }

        public void setFront(boolean isFront) {
            this.isFront = isFront;
        }


        //****************************************
        // Modification Methods
        //****************************************
        public void removeCloud(int index) {
            clouds.remove(index);
            cloudLoc.remove(index);
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
