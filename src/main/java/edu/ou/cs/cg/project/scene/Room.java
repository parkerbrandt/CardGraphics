package edu.ou.cs.cg.project.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

import edu.ou.cs.cg.utilities.*;


/**
 * The Room Class
 * Represents a Cubic room with walls and floor
 *
 * TODO: Add window with city background
 *
 * @author Parker Brandt
 */
public class Room extends Node {

    //****************************************
    // Private Class Members
    //****************************************

    private static final String CITY_IMAGE = "images/city.jpg";


    //****************************************
    // Private Class Members
    //****************************************
    private final Lamp lamp;
    private final Window window;
    private Shelf[] shelves;


    //****************************************
    // Constructors
    //****************************************

    /**
     * Constructor add textures with
     * @param textures
     */
    public Room(Texture[] textures) {
        super(textures);

        // Create the lamp for the room that emits a yellow-whitish color
        //new float[]{1.0f, 1.6f, 204.0f/255.0f, 0.0f}
        lamp = new Lamp(textures, 32, new float[]{160.0f/255.0f, 160.0f/255.0f, 160.0f/255.0f, 1.0f});
        super.add(lamp);

        // Create the window
        window = new Window(textures);
        super.add(window);

        // TODO: Create three shelves
        shelves = new Shelf[3];

        // Adjust the layout of the room
        pushTransform(new Transform.Scale(2.0f, 1.0f, 2.0f));
        pushTransform(new Transform.Translate(0.0f, 1.0f, 1.0f));

    }


    //****************************************
    // Node Override Methods
    //****************************************

    @Override
    protected void change(GL2 gl) { }

    @Override
    protected void depict(GL2 gl) {

        // Create cube to represent the room
        // Fill each face with a texture
        Cube.fillFace(gl, 1, getTexture(0));
        Cube.fillFace(gl, 2, getTexture(0));
        Cube.fillFace(gl, 3, getTexture(0));
        Cube.fillFace(gl, 4, getTexture(0));
        Cube.fillFace(gl, 5, getTexture(1));
    }


    //****************************************
    // Inner Classes
    //****************************************

    /**
     * An inner class used to represent a window in the room
     * Should use a transformed version of a cube
     */
    public static class Window extends Node {

        //****************************************
        // Constructors
        //****************************************
        public Window(Texture[] textures) {
            super(textures);

            pushTransform(new Transform.Translate(10.2f, 0.0f, -0.1f));
            pushTransform(new Transform.Scale(0.05f, 0.5f, 0.5f));
        }


        //****************************************
        // Node Override Methods
        //****************************************
        @Override
        protected void change(GL2 gl) { }

        @Override
        protected void depict(GL2 gl) {

            // Use an altered cube to depict the window
            Cube.fill(gl);

            // TODO: Fill each face of cube
        }
    }


    /**
     * An inner class used to represent a lamp in the room
     * Will act as the light source for the room
     */
    public static class Lamp extends Node {

        //****************************************
        // Private Variables
        //****************************************

        private Cylinder cylinder;
        private final int sides;
        private final float[] emit;


        //****************************************
        // Constructors
        //****************************************
        public Lamp(Texture[] textures, int sides, float[] emit) {
            super(textures);

            // Initialize variables
            this.sides = sides;
            this.emit = emit;

            cylinder = new Cylinder(sides, 0.0f, 1.0f);

            // Move the cylinder to the back left corner
            pushTransform(new Transform.Translate(-1.0f, 0.0f, -1.0f));
            pushTransform(new Transform.Rotate(0.0f, 0.0f, 1.0f, 90.0f));
        }


        //****************************************
        // Node Override Methods
        //****************************************
        @Override
        protected void change(GL2 gl) { }

        @Override
        protected void depict(GL2 gl) {
            Lighting.setMaterial(gl, null, null, null, null, emit);
        }
    }

    /**
     * Inner class to represent the shelves in the program
     */
    public static class Shelf extends Node {

        //****************************************
        // Constructors
        //****************************************
        public Shelf(Texture[] textures) {
            super(textures);


        }
    }
}
