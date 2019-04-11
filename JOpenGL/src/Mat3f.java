

import java.text.DecimalFormat;

/**
/**
 * A class to represent a 3x3 matrix of floating point values.
 * <p>
 * The Mat3f class contains 9 public member properties m00 through m22 which are
 * <strong>column major</strong>, so our matrix looks like this:
 * <p>
 * m00  m10  m20
 * <p>
 * m01	m11  m21
 * <p>
 * m02	m12  m22
 * <p>  
 * For each property the first digit is the column, and the second digit is the row.
 * 
 * The identity matrix specifies a left-handed coordinate system whereby if you put your left
 * hand in front of you with your thumb pointing towards yourself, your index finger pointing
 * directly up and your middle finger pointing to the right, then: 
 * <ul>
 * <li>The direction of the middle finger is the positive X-axis,</li>
 * <li>The direction of the index  finger is the positive Y-axis, and</li>
 * <li>The direction of the thumb         is the positive Z-axis.</li> 
 * </ul>
 * <p>
 * This corresponds to the coordinate system used by OpenGL.
 * <p>
 * Rotations are performed in accordance with the right-hand rule, that is, if you were to grip
 * an axis with your thumb pointing towards the positive end of the axis, rotating in a positive
 * direction would follow the curl of your fingers in an anti-clockwise direction, and rotation
 * by a negative amount would go in the opposite (clockwise) direction. 
 * 
 * @author Al Lansley
 * @version 0.3 - 28/12/2014
 */
public class Mat3f
{
	public float m00, m01, m02; // First  column
	public float m10, m11, m12; // Second column
	public float m20, m21, m22; // Third  column

	/**
	 * Default constructor
	 * 
	 * All properties will be implicitly initialised to 0.0f by default, as that's the default
	 * initial value for Java primitives of type float.
	 */
	public Mat3f() { }

	/**
	 * One parameter constructor.
	 * <p>
	 * Sets the provided value across the diagonal of the matrix, which can be used, for example,
	 * to create an identity matrix by specifying: {@code Mat3f identityMatrix = new Mat3df(1.0f);.
	 * 
	 * @param	value	The value to set across the diagonal (top-left to bottom-right) of the matrix.
	 */
	public Mat3f(float value)
	{
		// Set diagonal
		m00 = m11 = m22 = value;

		// Zero rest of matrix
		m01 = m02 = m10 = m12 = m20 = m21 = 0.0f;
	}
	
	/**
	 * Nine parameter constructor.
	 * 
	 * @param	m00	The first  element of the first  column
	 * @param	m01	The second element of the first  column
	 * @param	m02	The third  element of the first  column
	 * @param	m10	The first  element of the second column
	 * @param	m11 The second element of the second column
	 * @param	m12 The third  element of the second column
	 * @param	m20 The first  element of the third  column
	 * @param	m21 The second element of the third  column
	 * @param	m22 The third  element of the thir   column
	 */
	public Mat3f(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22)
	{
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
	}

	/** Zero all elements of the matrix. */
	public void zero()
	{
		m00 = m01 = m02 = m10 = m11 = m12 = m20 = m21 = m22 = 0.0f;
	}

	/**
	 * Set the matrix to an identity matrix.
	 * <p>
	 * The resulting matrix is:
	 * <p>
	 * 1.0f 0.0f 0.0f
	 * <p>
	 * 0.0f 1.0f 0.0f
	 * <p>
	 * 0.0f 0.0f 1.0f
	 */
	public void setIdentity()
	{
		// Set diagonal
		m00 = m11 = m22 = 1.0f;

		// Zero rest of matrix
		m01 = m02 = m10 = m12 = m20 = m21 = 0.0f;
	}

	/**
	 * Transpose this Mat3f.
	 * <p>
	 * This method does not change this Mat3f - instead a new Mat3f which is the transposed
	 * version of this Mat3f is created and returned.
	 * 
	 * @return	The transposed version of this matrix.
	 */ 
	public Mat3f transpose()
	{
		return new Mat3f(this.m00, this.m10, this.m20,
				         this.m01, this.m11, this.m21,
				         this.m02, this.m12, this.m22);
		
		//TODO: Check this is all good and delete below code
		/*
		// ----- Take copies of values where required -----

		//float m00copy = m00; // Not required - value does not change position
		float m01copy = m01;
		float m02copy = m02;

		float m10copy = m10;
		//float m11copy = m11; // Not required - value does not change position
		float m12copy = m12;

		float m20copy = m20;
		float m21copy = m21;
		//float m22copy = m22; // Not required - value does not change position

		// ----- Replace values in-place where required -----

		//this.m00 = m00copy; // No-op!
		this.m01 = m10copy;
		this.m02 = m20copy;

		this.m10 = m01copy;
		//this.m11 = m11copy; // No-op!
		this.m12 = m21copy;

		this.m20 = m02copy;
		this.m21 = m12copy;
		//this.m22 = m22copy; // No-op!

		// Return 'this' so that the method can be chained i.e. someMat3f.transpose().inverse().etc...
		return this;
		*/
	}

	/**
	 * Multiply this Mat3f by another Mat3f.
	 * <p>
	 * <em>This</em> matrix is not modified by this method - instead a new matrix is created and returned.
	 * 
	 * @param	m	The Mat3f to multiply this Mat3f by.
	 * @return		The resulting Mat3f.
	 */
	public Mat3f times(Mat3f m)
	{
		Mat3f temp = new Mat3f();

		temp.m00 = this.m00 * m.m00 + this.m10 * m.m01 + this.m20 * m.m02;
		temp.m01 = this.m01 * m.m00 + this.m11 * m.m01 + this.m21 * m.m02;
		temp.m02 = this.m02 * m.m00 + this.m12 * m.m01 + this.m22 * m.m02;


		temp.m10 = this.m00 * m.m10 + this.m10 * m.m11 + this.m20 * m.m12;
		temp.m11 = this.m01 * m.m10 + this.m11 * m.m11 + this.m21 * m.m12;
		temp.m12 = this.m02 * m.m10 + this.m12 * m.m11 + this.m22 * m.m12;

		temp.m20 = this.m00 * m.m20 + this.m10 * m.m21 + this.m20 * m.m22;
		temp.m21 = this.m01 * m.m20 + this.m11 * m.m21 + this.m21 * m.m22;
		temp.m22 = this.m02 * m.m20 + this.m12 * m.m21 + this.m22 * m.m22;

		return temp;
	}

	/**
	 * Multiply a Vec3f by this Mat3f.
	 * <p>
	 * This is typically used to change the coordinate space in which a vertex location is specified in.
	 * For example:
	 * <p>
	 * {@code Vec3f worldVertexLocation = modelMatrix.times(modelVertexLocation);}
	 * <p>
	 * or
	 * {@code Vec3f normalisedDeviceCoordsLocation = modelViewProjectionMatrix.times(modelVertexLocation);} 
	 * 
	 * @param	source	The Vec3f to multiply by this matrix.
	 * @return			The resulting Vec3f.
	 */
	public Vec3f times(Vec3f source)
	{
		return new Vec3f(this.m00 * source.x + this.m10 * source.y + this.m20 * source.z,
				this.m01 * source.x + this.m11 * source.y + this.m21 * source.z,
				this.m02 * source.x + this.m12 * source.y + this.m22 * source.z);
	}

	/**
	 * Return the determinant of this matrix.
	 * <p>
	 * If the determinant of a matrix is zero then the matrix cannot be inverted.
	 * @return		The determinant of this matrix.
	 */
	public float determinant()
	{
		return m20 * m01 * m12 - m20  * m02 * m11 - m10 * m01 * m22 + m10 * m02 * m21 + m00 * m11 * m22 - m00 * m12 * m21;
	}

	/*public static float determinant(Mat3f m)
	{
		return m.m20 * m.m01 * m.m12 - m.m20  * m.m02 * m.m11 - m.m10 * m.m01 * m.m22 + m.m10 * m.m02 * m.m21 + m.m00 * m.m11 * m.m22 - m.m00 * m.m12 * m.m21;
	}*/

	/**
	 * Calculate and return a Mat3f which is the inverse of this Mat3f;
	 * <p>
	 * If the determinant of this matrix is zero then a RuntimeException is thrown to avoid
	 * a divide by zero.
	 * 
	 * @return		A Mat3f which is the inverse of this Mat3f.
	 */
	public Mat3f inverse()
	{	
		// Calculate the determinant of this matrix
		float d = this.determinant();

		// As long as the determinant is not zero...
		if (d != 0.0f)
		{
			//...then calculate and return the inverse of this matrix.
			Mat3f temp = new Mat3f();
			
			temp.m00 =  (m11  * m22 - m12 * m21) / d;
			temp.m01 = -(m01  * m22 - m02 * m21) / d;
			temp.m02 =  (m01  * m12 - m02 * m11) / d;
			temp.m10 = -(-m20 * m12 + m10 * m22) / d;
			temp.m11 =  (-m20 * m02 + m00 * m22) / d;
			temp.m12 = -(-m10 * m02 + m00 * m12) / d;
			temp.m20 =  (-m20 * m11 + m10 * m21) / d;
			temp.m21 = -(-m20 * m01 + m00 * m21) / d;
			temp.m22 =  (-m10 * m02 + m00 * m11) / d;

			return temp;
		}
		else // If the determinant is zero then throw a runtime exception.
		{
			throw new RuntimeException("Cannot invert a matrix whose determinant is zero.");
		}
	}

	// TODO: I should compare the output of the below inverse method to the inverse method above.
	/*
	// Method to calculate and the inverse of a Mat4f
	// Note: Adapted from: http://stackoverflow.com/questions/2624422/efficient-4x4-matrix-inverse-affine-transform
	//       However.....: http://www.johndcook.com/blog/2010/01/19/dont-invert-that-matrix/
	public Mat3f inverse()
	{
	    float s0 = m00 * m11 - m10 * m01;
	    float s1 = m00 * m12 - m10 * m02;
	    float s2 = m00 * m13 - m10 * m03;
	    float s3 = m01 * m12 - m11 * m02;
	    float s4 = m01 * m13 - m11 * m03;
	    float s5 = m02 * m13 - m12 * m03;

	    float c5 = m22 * m33 - m32 * m23;
	    float c4 = m21 * m33 - m31 * m23;
	    float c3 = m21 * m32 - m31 * m22;
	    float c2 = m20 * m33 - m30 * m23;
	    float c1 = m20 * m32 - m30 * m22;
	    float c0 = m20 * m31 - m30 * m21;

	    // Should check for 0 determmnant

	    float invdet  = 1.0f / (s0 * c5 - s1 * c4 + s2 * c3 + s3 * c2 - s4 * c1 + s5 * c0);

	    Mat3f mat = new Mat3f();

	    mat.m00 = ( m11 * c5 - m12 * c4 + m13 * c3) * invdet;
	    mat.m01 = (-m01 * c5 + m02 * c4 - m03 * c3) * invdet;
	    mat.m02 = ( m31 * s5 - m32 * s4 + m33 * s3) * invdet;
	    mat.m03 = (-m21 * s5 + m22 * s4 - m23 * s3) * invdet;

	    mat.m10 = (-m10 * c5 + m12 * c2 - m13 * c1) * invdet;
	    mat.m11 = ( m00 * c5 - m02 * c2 + m03 * c1) * invdet;
	    mat.m12 = (-m30 * s5 + m32 * s2 - m33 * s1) * invdet;
	    mat.m13 = ( m20 * s5 - m22 * s2 + m23 * s1) * invdet;

	    mat.m20 = ( m10 * c4 - m11 * c2 + m13 * c0) * invdet;
	    mat.m21 = (-m00 * c4 + m01 * c2 - m03 * c0) * invdet;
	    mat.m22 = ( m30 * s4 - m31 * s2 + m33 * s0) * invdet;
	    mat.m23 = (-m20 * s4 + m21 * s2 - m23 * s0) * invdet;

	    mat.m30 = (-m10 * c3 + m11 * c1 - m12 * c0) * invdet;
	    mat.m31 = ( m00 * c3 - m01 * c1 + m02 * c0) * invdet;
	    mat.m32 = (-m30 * s3 + m31 * s1 - m32 * s0) * invdet;
	    mat.m33 = ( m20 * s3 - m21 * s1 + m22 * s0) * invdet;

	    // Overwrite this matrix with the inverse matrix we just calculated
	    this.m00 = mat.m00;
		this.m01 = mat.m10;
		this.m02 = mat.m20;
		this.m03 = mat.m30;

		this.m10 = mat.m01;
		this.m11 = mat.m11;
		this.m12 = mat.m21;
		this.m13 = mat.m31;

		this.m20 = mat.m02;
		this.m21 = mat.m12;
		this.m22 = mat.m22;
		this.m23 = mat.m32;

		this.m30 = mat.m03;
		this.m31 = mat.m13;
		this.m32 = mat.m23;
		this.m33 = mat.m33;

		return this;
	}*/

	/*
	// Method to translate a matrix
	// Note: We simply apply the movement components to the x/y/z values in the final column!
	public void translate(Vec3f movement)
	{
		this.m30 += movement.x;
		this.m31 += movement.y;
		this.m32 += movement.z;
	}
	 */

	// TODO: Check if the axis normalisation matters - i.e. is does rotating about the axis 1,2,3 result in the same
	// rotation as rotating about 2,4,6?
	/**
	 * Rotate a Mat3f by a given angle around a specified axis and return the rotated matrix.
	 * <p>
	 * This matrix is not modified by this method - a new matrix which contains the rotated version
	 * of this matrix is returned.
	 * 
	 * @param	angleDegs	The angle to rotate the matrix about specified in degrees.
	 * @param	axis		The axis to rotate the matrix about.
	 * @return				The rotated matrix.
	 */
	public Mat3f rotateDegs(float rotDegs, Vec3f axis)
	{
		Mat3f dest = new Mat3f();

		float cos = (float) Math.cos(rotDegs);
		float sin = (float) Math.sin(rotDegs);

		float nivCos = 1.0f - cos;

		float xy = axis.x * axis.y;
		float yz = axis.y * axis.z;
		float xz = axis.x * axis.z;
		float xs = axis.x * sin;
		float ys = axis.y * sin;
		float zs = axis.z * sin;

		float f00 = axis.x * axis.x * nivCos + cos;
		float f01 = xy * nivCos + zs;
		float f02 = xz * nivCos - ys;

		float f10 = xy * nivCos - zs;
		float f11 = axis.y * axis.y * nivCos + cos;
		float f12 = yz * nivCos + xs;

		float f20 = xz * nivCos + ys;
		float f21 = yz * nivCos - xs;
		float f22 = axis.z * axis.z * nivCos + cos;

		float t00 = m00 * f00 + m10 * f01 + m20 * f02;
		float t01 = m01 * f00 + m11 * f01 + m21 * f02;
		float t02 = m02 * f00 + m12 * f01 + m22 * f02;

		float t10 = m00 * f10 + m10 * f11 + m20 * f12;
		float t11 = m01 * f10 + m11 * f11 + m21 * f12;
		float t12 = m02 * f10 + m12 * f11 + m22 * f12;


		// Construct rotate matrix
		dest.m20 = m00 * f20 + m10 * f21 + m20 * f22;
		dest.m21 = m01 * f20 + m11 * f21 + m21 * f22;
		dest.m22 = m02 * f20 + m12 * f21 + m22 * f22;

		dest.m00 = t00;
		dest.m01 = t01;
		dest.m02 = t02;

		dest.m10 = t10;
		dest.m11 = t11;
		dest.m12 = t12;

		return dest;
	}

	public void setXBasis(Vec3f v)
	{
		if (v.length() != 1.0f)
		{
			v.normalise();
		}

		m00 = v.x;
		m01 = v.y;
		m02 = v.z;
	}

	public void setYBasis(Vec3f v)
	{
		if (v.length() != 1.0f)
		{
			v.normalise();
		}

		m10 = v.x;
		m11 = v.y;
		m12 = v.z;
	}

	public void setZBasis(Vec3f v)
	{
		if (v.length() != 1.0f)
		{
			v.normalise();
		}

		m20 = v.x;
		m21 = v.y;
		m22 = v.z;
	}

	/*

	// From: http://potatoland.org/code/gl/source/glmodel/GL_Matrix.java
	// Note: Code from above goes m00 to m33
	public Mat4f rotateDegs(float angleDegs, Vec3f rotationAxis)
	{
		public static GL_Matrix rotateMatrix(float dx, float dy, float dz)
		{
			GL_Matrix out=new GL_Matrix();
			float SIN;
			float COS;

			if (dx!=0)
			{
				GL_Matrix m =new GL_Matrix();
				SIN = (float)Math.sin(dx);
				COS = (float)Math.cos(dx);
				m.m11=COS;
				m.m12=SIN;
				m.m21=-SIN;
				m.m22=COS;
				out.transform(m);
			}
			if (dy!=0)
			{
				GL_Matrix m =new GL_Matrix();
				SIN = (float)Math.sin(dy);
				COS = (float)Math.cos(dy);
				m.m00=COS;
				m.m02=SIN;
				m.m20=-SIN;
				m.m22=COS;
				out.transform(m);
			}
			if (dz!=0)
			{
				GL_Matrix m =new GL_Matrix();
				SIN = (float)Math.sin(dz);
				COS = (float)Math.cos(dz);
				m.m00=COS;
				m.m01=SIN;
				m.m10=-SIN;
				m.m11=COS;
				out.transform(m);
			}
			return out;
		}
	}
	 */

	public float[] toArray()
	{
		float[] floatArray = new float[9];

		floatArray[0] = m00;
		floatArray[1] = m01;
		floatArray[2] = m02;

		floatArray[3] = m10;
		floatArray[4] = m11;
		floatArray[5] = m12;

		floatArray[6] = m20;
		floatArray[7] = m21;
		floatArray[8] = m22;

		return floatArray;
	}

	// Note: Displays output in COLUMN-MAJOR format!
	@Override
	public String toString()
	{
		DecimalFormat df = new DecimalFormat("0.000");

		StringBuilder sb = new StringBuilder();
		sb.append("m00: " + df.format(m00) + ",\tm10: " + df.format(m10) + ",\tm20: " + df.format(m20) + "\n");
		sb.append("m01: " + df.format(m01) + ",\tm11: " + df.format(m11) + ",\tm21: " + df.format(m21) + "\n");
		sb.append("m02: " + df.format(m02) + ",\tm12: " + df.format(m12) + ",\tm22: " + df.format(m22) + "\n");

		return sb.toString();
	}
}
