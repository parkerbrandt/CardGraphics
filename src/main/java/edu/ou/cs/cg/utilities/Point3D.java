package edu.ou.cs.cg.utilities;

/**
 * A class to represent a 3-Dimensional point
 * Uses double values to represent x, y, and z
 *
 * @author Parker Brandt
 */
public class Point3D {

    // Properties
    public double x, y, z;


    // Constructor
    /**
     * The constructor for the Point 3D class
     * @param x the x location of the point
     * @param y the y location of the point
     * @param z the z location of the point
     */
    public Point3D(double x, double y, double z) {
        // Initialize variables
        this.x = x;
        this.y = y;
        this.z = z;
    }


    // Override Methods
    /**
     * @return a string representation of the points in (x, y, z) format
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
