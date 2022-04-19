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
     * Default constructor
     */
    public Room() {
        pushTransform(new Transform.Scale(4.0f, 2.0f, 4.0f));
    }

    /**
     * Constructor add textures with
     * @param textures
     */
    public Room(Texture[] textures) {
        super(textures);


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

        // TODO: Adjust to fill each face with a texture
        Cube.fill(gl);
    }
}
