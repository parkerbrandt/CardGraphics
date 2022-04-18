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
import edu.ou.cs.cg.utilities.Node;

import java.awt.*;
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
    private static final String RSRC = "images/";               // Resource folder location
    private static final String[] FILENAMES = {};               // TODO: Add filenames


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
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        // TODO: Don't allow resizing
    }


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

        // TODO: Add ambient light from a lamp
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
                    0.0, 0.0, 0.0,        // Focal point coordinates
                    0.0, 1.0, 0.0);              // "up" vector

        // TODO: Change camera position and angle of rotation

    }

    private void initTextures(GLAutoDrawable drawable) {

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
        renderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        renderer.endRendering();
    }


    //****************************************
    // Public Methods
    //****************************************

    // Scene Methods
    public void initScene(GLAutoDrawable drawable) {

    }


    //****************************************
    // Getters and Setters
    //****************************************


}
