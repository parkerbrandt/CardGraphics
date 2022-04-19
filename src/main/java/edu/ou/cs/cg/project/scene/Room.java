package edu.ou.cs.cg.project.scene;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import edu.ou.cs.cg.utilities.Cube;
import edu.ou.cs.cg.utilities.Node;

public class Room extends Node {

    //****************************************
    // Private Variables
    //****************************************
    private Cube room;


    //****************************************
    // Constructors
    //****************************************

    /**
     * Default constructor
     */
    public Room() {

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

    }
}
