package edu.ou.cs.cg.project;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * The MouseHandler Class
 * Handles user interaction with the mouse
 *
 * @author Parker Brandt
 */
public class MouseHandler extends MouseAdapter {

    //****************************************
    // Private Variables
    //****************************************
    private final View view;
    private final Model model;


    //****************************************
    // Constructors
    //****************************************
    public MouseHandler(View view, Model model) {

        // Initialize variables
        this.view = view;
        this.model = model;

        // Get window information
        Component component = view.getCanvas();

        component.addMouseListener(this);
        component.addMouseMotionListener(this);
        component.addMouseWheelListener(this);
    }


    //****************************************
    // MouseListener Override Methods
    //****************************************
    public void		mouseClicked(MouseEvent e)
    {
    }

    public void		mouseEntered(MouseEvent e)
    {
    }

    public void		mouseExited(MouseEvent e)
    {
    }

    public void		mousePressed(MouseEvent e)
    {
    }

    public void		mouseReleased(MouseEvent e)
    {
    }


    //****************************************
    // MouseMotionListener Override Methods
    //****************************************
    public void		mouseDragged(MouseEvent e)
    {
    }

    public void		mouseMoved(MouseEvent e)
    {
    }


    //****************************************
    // MouseWheelListener Override Methods
    //****************************************
    public void		mouseWheelMoved(MouseWheelEvent e)
    {
    }

}
