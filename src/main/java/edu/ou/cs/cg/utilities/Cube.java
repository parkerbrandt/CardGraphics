//******************************************************************************
// Copyright (C) 2019 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Wed Apr 24 15:52:55 2019 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20190424 [weaver]:	Original file.
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
 * The <CODE>Cube/CODE> class provides a mesh of a cube. No parameters are
 * required for a unit cube, so this class follows the singleton pattern.<P>
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
 * need to populate the edge array in your mesh class. Also for HW06: It would
 * be better to start from a duplicate of Cylinder to implement your new
 * mesh.<P>
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class Cube
{
	//**********************************************************************
	// Public Class Members
	//**********************************************************************

	public static final Quaternion[]	VERTICES = new Quaternion[]
	{
		new Quaternion(0.0f, 0.0f, 1.0f,  1.0f),
		new Quaternion(1.0f, 0.0f, 1.0f,  1.0f),
		new Quaternion(1.0f, 1.0f, 1.0f,  1.0f),
		new Quaternion(0.0f, 1.0f, 1.0f,  1.0f),

		new Quaternion(0.0f, 0.0f, 0.0f,  1.0f),
		new Quaternion(1.0f, 0.0f, 0.0f,  1.0f),
		new Quaternion(1.0f, 1.0f, 0.0f,  1.0f),
		new Quaternion(0.0f, 1.0f, 0.0f,  1.0f),
	};

	public static final int[][]		FACES = new int[][]
	{
		{ 3, 0, 1, 2 },		// front
		{ 6, 5, 4, 7 },		// back
		{ 2, 1, 5, 6 },		// right
		{ 7, 4, 0, 3 },		// left
		{ 7, 3, 2, 6 },		// top
		{ 5, 1, 0, 4 },		// bottom
	};

	public static final Quaternion[]	NORMALS = new Quaternion[]
	{
		new Quaternion( 0.0f,  0.0f,  1.0f,  0.0f),
		new Quaternion( 0.0f,  0.0f, -1.0f,  0.0f),
		new Quaternion( 1.0f,  0.0f,  0.0f,  0.0f),
		new Quaternion(-1.0f,  0.0f,  0.0f,  0.0f),
		new Quaternion( 0.0f,  1.0f,  0.0f,  0.0f),
		new Quaternion( 0.0f, -1.0f,  0.0f,  0.0f),
	};

	public static final int[][]		EDGES = new int[][]
	{
		{ 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 0 },
		{ 4, 7 }, { 7, 6 }, { 6, 5 }, { 5, 4 },
		{ 1, 5 }, { 6, 2 }, { 3, 7 }, { 4, 0 },
	};

	//**********************************************************************
	// Public Class Methods
	//**********************************************************************

	public static void	fill(GL2 gl)
	{
		for (int i=0; i<FACES.length; i++)
		{
			Quaternion	n = NORMALS[i];

			gl.glBegin(GL2.GL_POLYGON);

			for (int j=0; j<FACES[i].length; j++)
			{
				Quaternion	v = VERTICES[FACES[i][j]];

				gl.glNormal3f(n.getX(), n.getY(), n.getZ());
				gl.glVertex3f(v.getX(), v.getY(), v.getZ());
			}

			gl.glEnd();
		}
	}

	public static void	edge(GL2 gl)
	{
		gl.glBegin(GL2.GL_LINES);

		for (int i=0; i<EDGES.length; i++)
		{
			Quaternion	v1 = VERTICES[EDGES[i][0]];
			Quaternion	v2 = VERTICES[EDGES[i][1]];

			gl.glVertex3f(v1.getX(), v1.getY(), v1.getZ());
			gl.glVertex3f(v2.getX(), v2.getY(), v2.getZ());
		}

		gl.glEnd();
	}

	public static void	fillFace(GL2 gl, int face, Texture texture)
	{
		texture.enable(gl);
		texture.bind(gl);

		TextureCoords	coords = texture.getImageTexCoords();
		float			cl = coords.left();
		float			cr = coords.right();
		float			ct = coords.top();
		float			cb = coords.bottom();

		gl.glBegin(GL2.GL_QUADS);

		Quaternion	v;
		Quaternion	n = NORMALS[face];

		v = VERTICES[FACES[face][0]];
		gl.glTexCoord2f(cl, ct);			// top left
		gl.glNormal3f(n.getX(), n.getY(), n.getZ());
		gl.glVertex3f(v.getX(), v.getY(), v.getZ());

		v = VERTICES[FACES[face][1]];
		gl.glTexCoord2f(cl, cb);			// bottom left
		gl.glNormal3f(n.getX(), n.getY(), n.getZ());
		gl.glVertex3f(v.getX(), v.getY(), v.getZ());

		v = VERTICES[FACES[face][2]];
		gl.glTexCoord2f(cr, cb);			// bottom right
		gl.glNormal3f(n.getX(), n.getY(), n.getZ());
		gl.glVertex3f(v.getX(), v.getY(), v.getZ());

		v = VERTICES[FACES[face][3]];
		gl.glTexCoord2f(cr, ct);			// top right
		gl.glNormal3f(n.getX(), n.getY(), n.getZ());
		gl.glVertex3f(v.getX(), v.getY(), v.getZ());

		gl.glEnd();

		texture.disable(gl);
	}
}

//******************************************************************************
