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

            // Open the card at the number location on the shelves (1-9) if in edit mode
            case KeyEvent.VK_1:
                if(model.getDisplayCards().size() >= 1 && model.isEditMode()) {
                    // view.getMainCard().copyCard(model.getDisplayCards().get(0));
                }
                break;

            case KeyEvent.VK_2:
                if(model.getDisplayCards().size() >= 2 && model.isEditMode()) {
                    // view.getMainCard().copyCard(model.getDisplayCards().get(1));
                }
                break;

            case KeyEvent.VK_3:
                if(model.getDisplayCards().size() >= 3 && model.isEditMode()) {
                    // view.getMainCard().copyCard(model.getDisplayCards().get(2));
                }
                break;

            case KeyEvent.VK_4:
                if(model.getDisplayCards().size() >= 4 && model.isEditMode()) {
                    // view.getMainCard().copyCard(model.getDisplayCards().get(3));
                }
                break;

            case KeyEvent.VK_5:
                if(model.getDisplayCards().size() >= 5 && model.isEditMode()) {
                    // view.getMainCard().copyCard(model.getDisplayCards().get(4));
                }
                break;

            case KeyEvent.VK_6:
                if(model.getDisplayCards().size() >= 6 && model.isEditMode()) {
                    // view.getMainCard().copyCard(model.getDisplayCards().get(5));
                }
                break;

            case KeyEvent.VK_7:
                if(model.getDisplayCards().size() >= 7 && model.isEditMode()) {
                    // view.getMainCard().copyCard(model.getDisplayCards().get(6));
                }
                break;

            case KeyEvent.VK_8:
                if(model.getDisplayCards().size() >= 8 && model.isEditMode()) {
                    // view.getMainCard().copyCard(model.getDisplayCards().get(7));
                }
                break;

            case KeyEvent.VK_9:
                if(model.getDisplayCards().size() >= 9 && model.isEditMode()) {
                    // view.getMainCard().copyCard(model.getDisplayCards().get(8));
                }
                break;

            // Move the selected tree to the left
            case KeyEvent.VK_A:
                if(model.isEditMode()) {
                    model.moveTreeRight(-0.1f);
                }
                break;

            // If in edit mode, shift through some predetermined colors for outside of the card
            case KeyEvent.VK_C:
                model.switchColor();
                break;

            // Delete the current card and set to be default
            // If in edit mode, move the tree to the right
            // If in edit mode AND shift is held down, reset the card
            case KeyEvent.VK_D:
                if(model.isEditMode()) {
                    if(b)
                        model.resetCard(true);
                    else
                        model.moveTreeRight(0.1f);
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
                    model.moveTreeUp(-0.1f);
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
                    model.moveTreeUp(0.1f);
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
