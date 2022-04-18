//******************************************************************************
// Copyright (C) 2020 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Fri Apr 10 14:35:00 2020 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20200410 [weaver]:	Original file.
//
//******************************************************************************
//
//******************************************************************************

package edu.ou.cs.cg.utilities;

//import java.lang.*;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

//******************************************************************************

public final class Lighting
{
	//**********************************************************************
	// Public Class Members
	//**********************************************************************

	public static final float[]	DEFAULT_AMBIENT =
		new float[] { 0.2f, 0.2f, 0.2f, 1.0f };

	public static final float[]	DEFAULT_DIFFUSE =
		new float[] { 0.8f, 0.8f, 0.8f, 1.0f };

	public static final float[]	DEFAULT_SPECULAR =
		new float[] { 0.0f, 0.0f, 0.0f, 1.0f };

	public static final float[]	DEFAULT_SHININESS =
		new float[] { 0.0f };

	public static final float[]	DEFAULT_EMISSION =
		new float[] { 0.0f, 0.0f, 0.0f, 1.0f };

	//**********************************************************************
	// Public Class Methods
	//**********************************************************************

	// Convenience method for setting all five light parameters.
	// Pass in null to turn off the corresponding parameter.
	public static void	setLight(GL2 gl, int light, float[] lp, float[] li,
								 float[] ld, float[] lc, float[] le)
	{
		gl.glEnable(light);

		if (lp != null)
			gl.glLightfv(light, GL2.GL_POSITION, lp, 0);

		if (li != null)
			gl.glLightfv(light, GL2.GL_DIFFUSE, li, 0);

		if (ld != null)
			gl.glLightfv(light, GL2.GL_SPOT_DIRECTION, ld, 0);

		if (lc != null)
			gl.glLightfv(light, GL2.GL_SPOT_CUTOFF, lc, 0);

		if (le != null)
			gl.glLightfv(light, GL2.GL_SPOT_EXPONENT, le, 0);
	}

	// Convenience method for setting all five material parameters.
	// Pass in null to set the parameter to the OpenGL default value.
	public static void	setMaterial(GL2 gl, float[] ambi, float[] diff,
									float[] spec, float[] shin, float[] emit)
	{
		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_AMBIENT,
						((ambi == null) ? DEFAULT_AMBIENT : ambi), 0);

		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE,
						((diff == null) ? DEFAULT_DIFFUSE : diff), 0);

		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SPECULAR,
						((spec == null) ? DEFAULT_SPECULAR : spec), 0);

		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_SHININESS,
						((shin == null) ? DEFAULT_SHININESS : shin), 0);

		gl.glMaterialfv(GL.GL_FRONT_AND_BACK, GL2.GL_EMISSION,
						((emit == null) ? DEFAULT_EMISSION : emit), 0);
	}
}

//******************************************************************************
