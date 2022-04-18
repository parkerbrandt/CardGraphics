package edu.ou.cs.cg.project;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * The Postcard Project
 * Main class, start of program logic
 * Handles creation of window and base elements
 *
 * @author Parker Brandt
 * @version Alpha 1.0
 */
public class Application implements Runnable {

    //****************************************
    // Public Class Members
    //****************************************
    public static final String      DEFAULT_NAME = "Card Viewing";
    public static final Dimension   DEFAULT_SIZE = new Dimension(1280, 720);


    //****************************************
    // Private Variables
    //****************************************
    private View view;


    //****************************************
    // Constructors
    //****************************************

    /**
     * Base Constructor for Application class
     * @param args command-line arguments (unused right now)
     */
    public Application(String[] args) { }


    //****************************************
    // Main Function
    //****************************************

    /**
     * Main Method
     * Start of Program Logic
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        // Begin running the application
        SwingUtilities.invokeLater(new Application(args));
    }


    //****************************************
    // Runnable Override Methods
    //****************************************

    /**
     * Runs the application
     */
    @Override
    public void run() {
        // Get the OpenGL information and crease window objects
        GLProfile profile = GLProfile.getDefault();

        System.out.println("Running with OpenGL version " + profile.getName());

        GLCapabilities  capabilities =  new GLCapabilities(profile);
        GLJPanel        canvas =        new GLJPanel(capabilities);
        JFrame          frame =         new JFrame(DEFAULT_NAME);

        // Set starting dimensions of the window
        canvas.setPreferredSize(DEFAULT_SIZE);

        // Populate and show the frame
        frame.setBounds(50, 50, 1280, 720);
        frame.getContentPane().add(canvas);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Exit when user clicks the frame's close button
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Initialize the view class to handle rendering and management of the canvas
        view = new View(canvas);
    }
}
