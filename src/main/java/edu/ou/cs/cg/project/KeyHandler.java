package edu.ou.cs.cg.project;

import edu.ou.cs.cg.utilities.Utilities;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * The KeyHandler Class
 * Handles user interaction input through the keyboard
 *
 * @author Parker Brandt
 */
public class KeyHandler extends KeyAdapter {

    //****************************************
    // Private Variables
    //****************************************
    private final View view;
    private final Model model;


    //****************************************
    // Constructor
    //****************************************

    /**
     * Adds key listening functionality to the application
     * @param view
     * @param model
     */
    public KeyHandler(View view, Model model) {

        // Initialize variables
        this.view = view;
        this.model = model;

        // Add key listening capabilities to the canvas
        Component component = view.getCanvas();
        component.addKeyListener(this);
    }


    //****************************************
    // KeyListener Override Methods
    //****************************************
    @Override
    public void keyPressed(KeyEvent e) {
        boolean b = Utilities.isShiftDown(e);

        switch(e.getKeyCode()) {

            // Open the card at the number location on the shelves (1-9)
            case KeyEvent.VK_1:
                break;

            // Enable edit mode
            case KeyEvent.VK_E:
                break;

            // If in edit mode, shift through some predetermined colors for outside of the card
            case KeyEvent.VK_C:
                break;

            // Save card to the next open slot, if none available delete #1
            case KeyEvent.VK_S:
                break;

            // Delete the current card and set to be default
            case KeyEvent.VK_D:
            case KeyEvent.VK_DELETE:
                break;

            // Open/Close the card
            case KeyEvent.VK_SPACE:
                model.setCardOpen(!model.isCardOpen());
                break;

            default:
                break;
        }
    }

}
