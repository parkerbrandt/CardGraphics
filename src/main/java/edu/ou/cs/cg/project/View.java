package edu.ou.cs.cg.project;


import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import edu.ou.cs.cg.project.scene.Card;
import edu.ou.cs.cg.project.scene.Room;
import edu.ou.cs.cg.utilities.Node;
import edu.ou.cs.cg.utilities.Transform;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;



/**
 * The View Class
 * Handles rendering and drawing of elements in the window
 *
 * @author Parker Brandt
 */
public class View implements GLEventListener {

    //****************************************
    // Private Class Members
    //****************************************
    private static final int DEFAULT_FRAMES_PER_SECOND = 60;
    private static final String CRSRC = "/cards/";                  // Card Resource folder location
    private static final String RSRC = "/images/";                  // Image Resource folder location
    private static final String[] FILENAMES =
            {
                    "wall.png",             // Image used to texture the walls
                    "floor.png",            // Image used to texture the floor and shelves of the room
                    "paper.png",            // Texture of the actual cards
                    "tree.png",             // The tree to use
                    "trunk.png",            // The trunk of the tree to show
                    "apple.png",            // The apples that will fall from the tree
                    "sun.png",              // The image used for the sun
                    "cloud.png",            // The image used for clouds
                    "window.png",           // Transparent window put over the city image
                    "city.png",             // The city image used to show the "outside" world
                    "curtains.png",         // The transparent curtains image used over the window
                    "door.png",             // The texture for a door
                    "front.png",            // The image that can be put on the front side of the card, use 'F' to toggle
                    "bg.png",               // The background image that can be used in the card
            };




    //****************************************
    // Public Class Members
    //****************************************
    public static final GLUT glut = new GLUT();
    public static final Random RANDOM = new Random();


    //****************************************
    // Private Variables
    //****************************************
    private final GLJPanel canvas;
    private int            width;
    private int            height;

    private TextRenderer        renderer;

    private final FPSAnimator   animator;
    private int                 counter;        // Animation counter

    private final Model         model;

    private final KeyHandler    keyHandler;
    private final MouseHandler  mouseHandler;

    private Texture[]           textures;       // Textures loaded from FILENAMES
    private Node                root;           // Root node of scene graph

    private Room                stage;          // The cubic room scene takes place in
    private Card                main;           // The main/default card the user is holding





    //****************************************
    // Constructor
    //****************************************

    /**
     * Initializes an instance of the View class
     * @param canvas the canvas we are drawing too
     */
    public View(GLJPanel canvas) {

        // Initialize variables
        this.canvas = canvas;

        counter = 0;
        canvas.addGLEventListener(this);

        model = new Model(this);

        keyHandler =    new KeyHandler(this, model);
        mouseHandler =  new MouseHandler(this, model);

        // Start animating
        animator = new FPSAnimator(canvas, DEFAULT_FRAMES_PER_SECOND);
        animator.start();
    }



    //****************************************
    // GLEventListener Override Methods
    //****************************************
    @Override
    public void init(GLAutoDrawable drawable) {

        // Get dimensions of the canvas
        width = drawable.getSurfaceWidth();
        height = drawable.getSurfaceHeight();

        // Initialize the renderer
        renderer = new TextRenderer(new Font("Monospaced", Font.PLAIN, 12),
                                    true, true);

        initPipeline(drawable);
        initTextures(drawable);

        root = new Node();

        initScene(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        renderer = null;
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        updatePipeline(drawable);

        update(drawable);
        render(drawable);

        GL2 gl = drawable.getGL().getGL2();

        // Finish and display
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) { }


    //****************************************
    // Private Methods
    //****************************************

    // Pipeline Methods

    /**
     * Initializes the OpenGL data
     * @param drawable
     */
    private void initPipeline(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Enable OpenGL features
        gl.glEnable(GL2.GL_DEPTH_TEST);     // Depth buffer updates
        gl.glEnable(GL2.GL_LINE_SMOOTH);    // Line anti-aliasing

        gl.glEnable(GL2.GL_LIGHTING);       // Enable lighting
        gl.glEnable(GL2.GL_NORMALIZE);      // Normalize normals before lighting
        gl.glShadeModel(GL2.GL_SMOOTH);     // Smooth (Gouraud) shading

        gl.glEnable(GL2.GL_COLOR_MATERIAL); // Allow coloring
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Updates information about the scene
     * @param drawable
     */
    private void updatePipeline(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = GLU.createGLU();

        // Clear color to black
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        // Initialize the default camera
        float aspect = (float)width / (float)height;     // Aspect ratio

        // Set up a perspective projection with 45 degree FOV
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, aspect, 0.1f, 50.0f);

        // Initialize the actual camera with a position and where to look at
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        glu.gluLookAt(0.0, 1.0, 3.0,            // Camera coordinates
                    0.0, 1.0, 0.0,        // Focal point coordinates
                    0.0, 1.0, 0.0);              // "up" vector

    }

    /**
     * Load image files as textures into instances of JOGL's texture class
     * @param drawable
     */
    private void initTextures(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();

        textures = new Texture[FILENAMES.length];

        // Iterate through each file and initialize the texture
        for(int i = 0; i < FILENAMES.length; i++) {
            try {
                URL url = View.class.getResource(RSRC + FILENAMES[i]);

                if(url != null) {
                    // Create the texture from a JPG file
                    textures[i] = TextureIO.newTexture(url, false, TextureIO.PNG);

                    textures[i].setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER,
                            GL2.GL_LINEAR);
                    textures[i].setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER,
                            GL2.GL_LINEAR);
                    textures[i].setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S,
                            GL2.GL_CLAMP_TO_EDGE);
                    textures[i].setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T,
                            GL2.GL_CLAMP_TO_EDGE);
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Rendering Methods
    private void update(GLAutoDrawable drawable) {

        // Increment the animation counter
        counter++;

        GL2 gl = drawable.getGL().getGL2();

        // Update the root
        root.update(gl);
    }

    private void render(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();

        // Enable lighting
        root.enable(gl);

        // Render the scene graph
        root.render(gl);

        // Disable lighting
        root.disable(gl);

        // Draw any text
        drawMode(drawable);
    }

    private void drawMode(GLAutoDrawable drawable) {

        renderer.beginRendering(width, height);

        // Draw text in white
        renderer.setColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Draw instructions on the left side if escape key is clicked
        // TODO:
        String[] instruct = {   "Instructions: ",
                                "E to toggle edit mode",
                                "C to change color",
                                "D to reset the card",
                                "T to edit the text",
                                "S to save the card",
                                "Space to open the card",
                                "Num Keys to Change Card",
                                };

        if(model.showInstructions()) {
            for(int i = 0; i < instruct.length; i++) {
                renderer.draw(instruct[i], 2, height - 12 * (i + 1));
            }
        }

        // Draw in the bottom right to let the user know they are editing
        if(model.isEditMode()) {
            renderer.draw("EDITING", 2, height - 708);
        }

        renderer.endRendering();
    }


    //****************************************
    // Public Methods
    //****************************************

    // Scene Methods
    public void initScene(GLAutoDrawable drawable) {

        // Create a cubic room with various amenities
        stage = new Room(textures);
        root.add(stage);

        // Create the default card
        main = new Card(textures, this, model);
        main.pushTransform(new Transform.Translate(0.0f, 1.0f, 1.0f));          // Move the default card in front of the user
        root.add(main);

        // Display the first 9 display cards from model
        ArrayList<Card> displayCards = model.getDisplayCards();
        int limit = Math.min(displayCards.size(), 9);
        if(displayCards.size() > 0) {
            System.out.println("AHAHAHAH");
            for (int i = 0; i < limit; i++) {
                // TODO: Add the transformations to put them on the shelves
                displayCards.get(i).pushTransform(new Transform.Scale(0.25f, 0.25f, 0.25f));
                displayCards.get(i).pushTransform(new Transform.Translate(-1.1f, 0.1f, 0.0f));
                root.add(displayCards.get(i));
            }
        }
    }


    //****************************************
    // Getters and Setters
    //****************************************

    public GLJPanel getCanvas() {
        return canvas;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TextRenderer getRenderer() {
        return renderer;
    }

    public Texture[] getTextures() {
        return textures;
    }

    public int getCounter() {
        return counter;
    }

    public Card getMainCard() {
        return main;
    }
}
