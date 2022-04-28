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

            case KeyEvent.VK_2:
                break;

            case KeyEvent.VK_3:
                break;

            case KeyEvent.VK_4:
                break;

            case KeyEvent.VK_5:
                break;

            case KeyEvent.VK_6:
                break;

            case KeyEvent.VK_7:
                break;

            case KeyEvent.VK_8:
                break;

            case KeyEvent.VK_9:
                break;

            // Move the selected tree to the left
            case KeyEvent.VK_A:
                if(model.isEditMode()) {

                }
                break;

            // If in edit mode, shift through some predetermined colors for outside of the card
            case KeyEvent.VK_C:
                model.switchColor();
                break;

            // Delete the current card and set to be default
            // If in edit mode, move the tree to the right
            case KeyEvent.VK_D:
                if(model.isEditMode()) {

                } else {

                }
                break;

            // Enable edit mode
            case KeyEvent.VK_E:
                model.setEditMode(!model.isEditMode());
                break;

            // Save card to the next open slot, if none available delete #1
            // If edit mode is enabled, move the tree
            case KeyEvent.VK_S:
                if(model.isEditMode()) {

                } else {

                }
                break;

            // Edit text if in edit mode
            case KeyEvent.VK_T:
                if(model.isEditMode()) {
                    model.changeText();
                }
                break;

            // Move the selected tree up
            case KeyEvent.VK_W:
                if(model.isEditMode()) {

                }
                break;

            // Open/Close the card
            case KeyEvent.VK_SPACE:
                model.setCardOpen(!model.isCardOpen());
                break;

            // Open/Close the instructions
            case KeyEvent.VK_ESCAPE:
                model.setShowInstructions(!model.showInstructions());
                break;

            // Loop through each tree on the card and allow the user to move the
            case KeyEvent.VK_RIGHT:
                model.switchSelectedTree();
                break;

            default:
                break;
        }
    }

}
