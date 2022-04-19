package edu.ou.cs.cg.project.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

import edu.ou.cs.cg.utilities.Cube;
import edu.ou.cs.cg.utilities.Node;
import edu.ou.cs.cg.utilities.Transform;


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
    // Constructors
    //****************************************

    /**
     * Constructor add textures with
     * @param textures
     */
    public Room(Texture[] textures) {
        super(textures);

        pushTransform(new Transform.Scale(2.0f, 1.0f, 2.0f));
        pushTransform(new Transform.Translate(0.0f, 1.0f, 1.0f));
    }


    //****************************************
    // Node Override Methods
    //****************************************

    @Override
    protected void change(GL2 gl) {

    }

    @Override
    protected void depict(GL2 gl) {

        // Create cube to represent the room
        // Fill each face with a texture
        Cube.fillFace(gl, 1, getTexture(0));
        Cube.fillFace(gl, 2, getTexture(0));
        Cube.fillFace(gl, 3, getTexture(0));
        Cube.fillFace(gl, 4, getTexture(0));

        // TODO:
    }


    //****************************************
    // Inner Classes
    //****************************************

    /**
     * An inner class used to represent a window in the room
     * Should use a transformed version of a cube
     */
    public static class Window extends Node {

    }


    /**
     * An inner class used to represent a lamp in the room
     * Will act as the light source for the room
     */
    public static class Lamp extends Node {

    }
}
