//******************************************************************************
// Copyright (C) 2020-2022 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Sat Apr 16 14:56:27 2022 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20200410 [weaver]:	Original file.
// 20220416 [weaver]:	Replaced fixed transform trio with full transform stack.
// 20220416 [weaver]:	Made access to texture array read-only.
//
//******************************************************************************
// Notes:
//
// Warning! This code uses depricated features of OpenGL, including immediate
// mode vertex attribute specification, for sake of easier classroom learning.
// See www.khronos.org/opengl/wiki/Legacy_OpenGL
//
//******************************************************************************

package edu.ou.cs.cg.utilities;

//import java.lang.*;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import edu.ou.cs.cg.utilities.Lighting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

//******************************************************************************

/**
 * The <CODE>Node</CODE> class. New and not very sophisticated! Use with care.
 *
 * TODO: Create subclasses (sub-subclasses, ...) for each type of object in your
 * scene graph. The key methods to override are change() and depict(), which do
 * the parameter updating and actual drawing at the node's branch of the graph.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public class Node
{
	//**********************************************************************
	// Private Members
	//**********************************************************************

	// Scene graph structure
	private final List<Node>		nodes;	// Children in scene graph

	// Transformation parameters
	private final Deque<Transform>	xforms;	// Transform sequence

	// Textures (reference copied from View)
	protected Texture[]				textures;

	// Lighting
	private int						light;	// -1 or GL2.GL_LIGHT#, #=[0,7]

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public Node(Texture[] textures, int light)
	{
		this.textures = textures;
		this.light = light;

		this.nodes = new ArrayList<Node>();
		this.xforms = new ArrayDeque<Transform>();
	}

	public Node(Texture[] textures)
	{
		this(textures, -1);
	}

	public Node(int light)
	{
		this(null, light);
	}

	public Node()
	{
		this(null, -1);
	}

	//**********************************************************************
	// Getters and Setters
	//**********************************************************************

	// Add a transform to the end of the transform list.
	public void		pushTransform(Transform t)
	{
		xforms.push(t);
	}

	// Remove and return the transform from the end of the transform list.
	public Transform	popTransform()
	{
		return xforms.pop();
	}

	//**********************************************************************
	// Public Methods (Textures)
	//**********************************************************************

	// These are here to provide textures to Nodes that need them to draw.

	public final Texture	getTexture(int index)
	{
		return textures[index];
	}

	//public final Texture[]	getTextures()
	//{
	//	return textures;
	//}

	//public final void	setTextures(Texture[] textures)
	//{
	//	this.textures = textures;
	//}

	//**********************************************************************
	// Public Methods (Scene Graph)
	//**********************************************************************

	public final void	add(Node node)
	{
		if (!nodes.contains(node))
			nodes.add(node);
	}

	public final boolean	remove(Node node)
	{
		return nodes.remove(node);
	}

	public final Node	get(int index)
	{
		return nodes.get(index);
	}

	//**********************************************************************
	// Public Methods (Updating)
	//**********************************************************************

	public final void	update(GL2 gl)
	{
		for (Node node : nodes)
			node.update(gl);

		change(gl);
	}

	// TODO: Override this method in subclasses that change parameters.
	protected void	change(GL2 gl)
	{
	}

	//**********************************************************************
	// Public Methods (Rendering)
	//**********************************************************************

	public final void	render(GL2 gl)
	{
		gl.glPushMatrix();

		for (Transform t : xforms)
			t.applyForward(gl);

		depict(gl);

		for (Node node : nodes)
			node.render(gl);

		gl.glPopMatrix();
	}

	// Override this method in subclasses that do actual drawing.
	protected void	depict(GL2 gl)
	{
	}

	//**********************************************************************
	// Public Methods (Lighting)
	//**********************************************************************

	public final void	enable(GL2 gl)
	{
		enableLighting(gl);

		for (Node node : nodes)
			node.enable(gl);
	}

	// Override this method in subclasses that provide lighting.
	protected void	enableLighting(GL2 gl)
	{
	}

	public final void	disable(GL2 gl)
	{
		disableLighting(gl);

		for (Node node : nodes)
			node.disable(gl);
	}

	// Override this method in subclasses that provide lighting.
	protected void	disableLighting(GL2 gl)
	{
	}

	//**********************************************************************
	// Public Methods (Lighting Support)
	//**********************************************************************

	// Make the Node into a source of diffuse light for the entire scene. (The
	// sun in the texture program is an example of doing that.) This is NOT the
	// same as setting lighting or material properties of geometries drawn by
	// this node! Use the Lighting class to do those things.

	// Use one of GL2.GL_LIGHT0 to GL2.GL_LIGHT7, or -1 for none. Be careful to
	// use each light only once in your scene. Call the methods enable() and
	// disable() methods to turn the light on and off while drawing.

	public final int		getLight()
	{
		return light;
	}

	public final void	setLight(int light)
	{
		if ((GL2.GL_LIGHT0 <= light) && (light <= GL2.GL_LIGHT7))
			this.light = light;
		else
			this.light = -1;
	}

	// Turn on diffuse light of color rgb at position (lpx, lpy, lpz).
	public final void	enableLightDiffuse(GL2 gl, float[] rgb,
										   float lpx, float lpy, float lpz)
	{
		if (light == -1)
			return;

		gl.glEnable(light);

		float[]	lp0 = new float[] { lpx, lpy, lpz, 0.0f };
		float[]	li0 = new float[] { rgb[0], rgb[1], rgb[2], 1.0f };

		Lighting.setLight(gl, light, lp0, li0, null, null, null);
	}

	public final void	disableLightDiffuse(GL2 gl)
	{
		if (light == -1)
			return;

		gl.glDisable(light);
	}
}

//******************************************************************************
