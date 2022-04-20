//******************************************************************************
// Copyright (C) 2019-2020 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Fri Apr 10 20:26:16 2020 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190424 [weaver]:	Original file.
// 20200410 [weaver]:	Changed name from Horizon to Cylinder.
//
//******************************************************************************
// Notes:
//
//******************************************************************************

package edu.ou.cs.cg.utilities;

//import java.lang.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.Quaternion;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;

//******************************************************************************

/**
 * The <CODE>Cylinder</CODE> class provides a mesh of a cylinder, with
 * parameters to set the number of sides (slices) and the coordinates of
 * the ends along the long (y) axis.<P>
 *
 * Quaternions represent point and vectors in homogeneous coordinates.
 * (The "w" coordinate is 1 for points and 0 for vectors.)<P>
 *
 * The mesh is defined as an array of vertices as point Quaternions and an
 * array of face normals as vector Quaternions. The face polygons are
 * represented as a two-dimensional array of integer indicies into the vertex
 * array; faces[i][j] is the jth vertex (ordered CCW) of the ith face polygon.
 * The edge segments are also represented as a two-dimensional array of integer
 * indicies into the vertex array, but edges[i][j] is the jth endpoint (so j=0
 * or 1) of the ith edge segment.<P>
 *
 * Note: HW06 doesn't require edge drawing. If you choose Option A, you don't
 * need to populate the edge array in your mesh class.<P>
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class Cylinder
{
	//**********************************************************************
	// Private Members
	//**********************************************************************

	// State (internal) variables
	private final int				slices;	// Number of sides around
	private final float			ymin;		// Axial coordinate of bottom
	private final float			ymax;		// Axial coordinate of top

	private final Quaternion[]		vertices;	// Vertex points
	private final int[][]			faces;		// Face[polygon#][vertex#]
	private final Quaternion[]		normals;	// Face normal vectors
	private final int[][]			edges;		// Edge[segment#][start|end]

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public Cylinder(int slices, float ymin, float ymax)
	{
		this.slices = slices;
		this.ymin = ymin;
		this.ymax = ymax;

		vertices = new Quaternion[slices * 2];
		faces = new int[slices][4];
		normals = new Quaternion[slices];
		edges = new int[slices * 3][2];

		calc();
	}

	public Cylinder()
	{
		this(4, 0.0f, 1.0f);
	}

	//**********************************************************************
	// Private Methods
	//**********************************************************************

	private void	calc()
	{
		double		inv = 2.0 * Math.PI / slices;

		// Calculate vertices as a circle extruded down and up along the yaxis
		for (int i=0; i<slices; i++)
		{
			double	theta = i * inv;
			float	x = (float)Math.cos(theta);
			float	z = (float)Math.sin(theta);

			// Circle vertices in xz plane extruded down to ymin
			vertices[(i * 2) + 0] = new Quaternion(x, ymin, -z, 1.0f);
			//System.out.println(vertices[(i * 2) + 0]);

			// Circle vertices in xz plane extruded up to ymax
			vertices[(i * 2) + 1] = new Quaternion(x, ymax, -z, 1.0f);
			//System.out.println(vertices[(i * 2) + 1]);
		}

		int		k = slices * 2;

		// Face vertices in counterclockwise order, facing inward, starting
		// with top left corner for alignment with texture coordinates.
		for (int i=0; i<slices; i++)
		{
			int	v0 = ((i * 2) + 0) % k;
			int	v1 = ((i * 2) + 1) % k;
			int	v2 = ((i * 2) + 2) % k;
			int	v3 = ((i * 2) + 3) % k;

			//System.out.println(v0 + " " + v1 + " " + v2 + " " + v3);

			faces[i][0] = v3;
			faces[i][1] = v2;
			faces[i][2] = v0;
			faces[i][3] = v1;

			// Calculate normals for the side faces. The normals have no Y
			// component, so simply use base circle going counterclockwise.
			float	dx = vertices[v2].getX() - vertices[v0].getX();
			float	dz = vertices[v2].getZ() - vertices[v0].getZ();

			// To point inward, use counterclockwise perp vector in xz plane
			normals[i] = new Quaternion(-dz, 0.0f, dx, 0.0f);		// Inward
			//normals[i] = new Quaternion(dz, 0.0f, -dx, 0.0f);		// Outward

			// Left edge of quad (ordered CCW looking from inside)
			edges[i + 0 * slices][0] = v0;
			edges[i + 0 * slices][1] = v1;

			// Bottom edge of quad (ordered CCW looking from inside)
			edges[i + 1 * slices][0] = v2;
			edges[i + 1 * slices][1] = v0;

			// Top edge of quad (ordered CCW looking from inside)
			edges[i + 2 * slices][0] = v1;
			edges[i + 2 * slices][1] = v3;
		}
	}

	//**********************************************************************
	// Public Methods
	//**********************************************************************

	public void	fill(GL2 gl, Texture texture)
	{
		texture.enable(gl);
		texture.bind(gl);

		TextureCoords	coords = texture.getImageTexCoords();
		float			cl = coords.left();
		float			cr = coords.right();
		float			cb = coords.bottom();
		float			ct = coords.top();
		float			step = (cr - cl) / slices;

		gl.glBegin(GL2.GL_QUADS);

		for (int i=0; i<slices; i++)
		{
			Quaternion	v;
			Quaternion	n = normals[i];

			// Faces go counterclockwise, so progress right to left in image
			float		fcl = cr - (i + 1) * step;
			float		fcr = cr - (i + 0) * step;

			v = vertices[faces[i][0]];
			gl.glTexCoord2f(fcl, ct);			// top left
			gl.glNormal3f(n.getX(), n.getY(), n.getZ());
			gl.glVertex3f(v.getX(), v.getY(), v.getZ());

			v = vertices[faces[i][1]];
			gl.glTexCoord2f(fcl, cb);			// bottom left
			gl.glNormal3f(n.getX(), n.getY(), n.getZ());
			gl.glVertex3f(v.getX(), v.getY(), v.getZ());

			v = vertices[faces[i][2]];
			gl.glTexCoord2f(fcr, cb);			// bottom right
			gl.glNormal3f(n.getX(), n.getY(), n.getZ());
			gl.glVertex3f(v.getX(), v.getY(), v.getZ());

			v = vertices[faces[i][3]];
			gl.glTexCoord2f(fcr, ct);			// top right
			gl.glNormal3f(n.getX(), n.getY(), n.getZ());
			gl.glVertex3f(v.getX(), v.getY(), v.getZ());
		}

		gl.glEnd();

		texture.disable(gl);
	}

	public void	edge(GL2 gl)
	{
		gl.glBegin(GL2.GL_LINES);

		for (int i=0; i<edges.length; i++)
		{
			Quaternion	v1 = vertices[edges[i][0]];
			Quaternion	v2 = vertices[edges[i][1]];

			gl.glVertex3f(v1.getX(), v1.getY(), v1.getZ());
			gl.glVertex3f(v2.getX(), v2.getY(), v2.getZ());
		}

		gl.glEnd();
	}

	public void	fillFoot(GL2 gl, Texture texture)
	{
		fillEnd(gl, texture, 0, 1.0f);
	}

	public void	fillHead(GL2 gl, Texture texture)
	{
		fillEnd(gl, texture, 1, -1.0f);
	}

	//**********************************************************************
	// Private Methods
	//**********************************************************************

	private void	fillEnd(GL2 gl, Texture texture, int parity, float ynormal)
	{
		texture.enable(gl);
		texture.bind(gl);

		TextureCoords	coords = texture.getImageTexCoords();
		float			cl = coords.left();
		float			cr = coords.right();
		float			cb = coords.bottom();
		float			ct = coords.top();

		//System.out.println(cl + " " + cr + " " + cb + " " + ct);

		float			winv = cr - cl;
		float			hinv = ct - cb;

		gl.glBegin(GL2.GL_POLYGON);

		for (int i=0; i<slices; i++)
		{
			Quaternion	v = vertices[(i * 2) + parity];
			float		cx = cl + 0.5f * (v.getX() + 1.0f) * winv;
			float		cz = cb + 0.5f * (v.getZ() + 1.0f) * hinv;

			gl.glTexCoord2f(cx, cz);
			gl.glNormal3f(0.0f, ynormal, 0.0f);
			gl.glVertex3f(v.getX(), v.getY(), v.getZ());
		}

		gl.glEnd();

		texture.disable(gl);
	}
}

//******************************************************************************
