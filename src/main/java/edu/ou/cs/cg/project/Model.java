package edu.ou.cs.cg.project;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLRunnable;
import edu.ou.cs.cg.utilities.Utilities;

import java.awt.*;

/**
 * The Model Class
 * Handles user interaction with data
 *
 * @author Parker Brandt
 */
public class Model {

    //****************************************
    // Private Variables
    //****************************************
    private final View view;


    //****************************************
    // Constructors
    //****************************************
    public Model(View view) {
        // Initialize variables
        this.view = view;
    }


    //****************************************
    // Getters and Setters
    //****************************************



    //****************************************
    // Inner Classes
    //****************************************
    // Convenience class to simplify the implementation of most updaters.
    private abstract class BasicUpdater implements GLRunnable
    {
        public final boolean	run(GLAutoDrawable drawable)
        {
            GL2 gl = drawable.getGL().getGL2();

            update(gl);

            return true;	// Let animator take care of updating the display
        }

        public abstract void	update(GL2 gl);
    }

    // Convenience class to simplify updates in cases in which the input is a
    // single point in view coordinates (integers/pixels).
    private abstract class ViewPointUpdater extends BasicUpdater
    {
        private final Point q;

        public ViewPointUpdater(Point q)
        {
            this.q = q;
        }

        public final void	update(GL2 gl)
        {
            int		    h = view.getHeight();
            double[]	p = Utilities.mapViewToScene(gl, q.x, h - q.y, 0.0);

            update(p);
        }

        public abstract void	update(double[] p);
    }
}
