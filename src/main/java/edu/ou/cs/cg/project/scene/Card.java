package edu.ou.cs.cg.project.scene;

import edu.ou.cs.cg.utilities.Node;

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
    private final int id;           // The unique ID of the card


    //****************************************
    // Private Variables
    //****************************************




    //****************************************
    // Constructor
    //****************************************

    /**
     * Default Constructor
     * Creates a base card in the user's hand
     */
    public Card() {

        // Initialize variables
        Random rand = new Random();
        this.id = rand.nextInt(1000);
    }

    /**
     * Loads a card from a CSV file
     * @param filename the file location/name
     */
    public Card(String filename) {

    }


    //****************************************
    // Node Override Methods
    //****************************************

    @Override
    


    //****************************************
    // Private Methods
    //****************************************


}
