//******************************************************************************
// Copyright (C) 2022 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Sat Apr 16 10:25:11 2022 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20220416 [weaver]:	Original file.
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

//******************************************************************************

/**
 * The <CODE>Transform</CODE> class is the base class for classes that manage
 * affine transform parameters and apply forward/inverse transforms as needed. 
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public abstract class Transform
{
	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	protected Transform()
	{
	}

	//**********************************************************************
	// Public Methods (Abstract)
	//**********************************************************************

	// Applies the forward transform.
	public abstract void	applyForward(GL2 gl);

	// Applies the inverse transform.
	public abstract void	applyInverse(GL2 gl);

	//**********************************************************************
	// Inner Classes
	//**********************************************************************

	public static final class Translate extends Transform
	{
		//**************************************************************
		// Private Members
		//**************************************************************

		private final float		dx;
		private final float		dy;
		private final float		dz;

		//**************************************************************
		// Constructors and Finalizer
		//**************************************************************

		public Translate(float dx, float dy, float dz)
		{
			this.dx = dx;
			this.dy = dy;
			this.dz = dz;
		}

		//**************************************************************
		// Override Methods (Transform)
		//**************************************************************

		public void		applyForward(GL2 gl)
		{
			gl.glTranslatef(dx, dy, dz);
		}

		public void		applyInverse(GL2 gl)
		{
			gl.glTranslatef(-dx, -dy, -dz);
		}
	}

	public static final class Scale extends Transform
	{
		//**************************************************************
		// Private Members
		//**************************************************************

		private final float		sx;
		private final float		sy;
		private final float		sz;

		//**************************************************************
		// Constructors and Finalizer
		//**************************************************************

		public Scale(float sx, float sy, float sz)
		{
			this.sx = sx;
			this.sy = sy;
			this.sz = sz;
		}

		//**************************************************************
		// Override Methods (Transform)
		//**************************************************************

		public void		applyForward(GL2 gl)
		{
			gl.glScalef(sx, sy, sz);
		}

		public void		applyInverse(GL2 gl)
		{
			gl.glScalef(1.0f / sx, 1.0f / sy, 1.0f / sz);
		}
	}

	public static final class Rotate extends Transform
	{
		//**************************************************************
		// Private Members
		//**************************************************************

		private final float		ux;
		private final float		uy;
		private final float		uz;
		private final float		beta;

		//**************************************************************
		// Constructors and Finalizer
		//**************************************************************

		public Rotate(float ux, float uy, float uz, float beta)
		{
			this.ux = ux;
			this.uy = uy;
			this.uz = uz;
			this.beta = beta;
		}

		//**************************************************************
		// Override Methods (Transform)
		//**************************************************************

		public void		applyForward(GL2 gl)
		{
			gl.glRotatef(beta, ux, uy, uz);
		}

		public void		applyInverse(GL2 gl)
		{
			gl.glRotatef(-beta, ux, uy, uz);
		}
	}

	public static final class Shear extends Transform
	{
		//**************************************************************
		// Private Members
		//**************************************************************

		private final float		k_xy;
		private final float		k_yx;
		private final float		k_xz;
		private final float		k_zx;
		private final float		k_yz;
		private final float		k_zy;

		private final float[]	kforward;
		//private final float[]	kinverse;

		//**************************************************************
		// Constructors and Finalizer
		//**************************************************************

		public Shear(float k_xy, float k_yx, float k_xz,
					 float k_zx, float k_yz, float k_zy)
		{
			this.k_xy = k_xy;
			this.k_yx = k_yx;
			this.k_xz = k_xz;
			this.k_zx = k_zx;
			this.k_yz = k_yz;
			this.k_zy = k_zy;

			kforward = new float[]
			{
	   			1.0f, k_yx, k_zx, 0.0f,
	   			k_xy, 1.0f, k_zy, 0.0f,
	   			k_xz, k_yz, 1.0f, 0.0f,
	   			0.0f, 0.0f, 0.0f, 1.0f,
			};

			//kinverse = new float[16]
			//{
	   		//	****, ****, ****, 0.0f,
	   		//	****, ****, ****, 0.0f,
	   		//	****, ****, ****, 0.0f,
	   		//	0.0f, 0.0f, 0.0f, 1.0f,
			//};
		}

		//**************************************************************
		// Override Methods (Transform)
		//**************************************************************

		public void		applyForward(GL2 gl)
		{
			gl.glMultMatrixf(kforward, 0);
		}

		// Not implemented. (Need to code up 4x4 matrix inverse calculation;
		// see for instance https://semath.info/src/inverse-cofactor-ex4.html.)
		public void		applyInverse(GL2 gl)
		{
			//gl.glMultMatrixf(kinverse, 0);
		}
	}
}

//******************************************************************************
