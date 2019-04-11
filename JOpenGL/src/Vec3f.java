

import java.text.DecimalFormat;
import java.util.Iterator;

/***
 *
 * 
 * Class  : Simple vec3 class which doesn't have bullshit conventions like Vector3f.add(left, right, result);
 *
 *          So we can actually go...
 *
 *              Vec3 foo = new Vec3(1, 2, 3);
 *              Vec3 bar = new Vec3(4, 5, 6);
 *              Vec3 baz = foo.plus(bar);
 *
 * Version: 0.1
 * Date   : 14/08/2014
 */

/*INTERESTING FUNCTION FROM UNITY3D
float SignedAngleBetween(Vector3 a, Vector3 b, Vector3 n){
    // angle in [0,180]
    float angle = Vector3.Angle(a,b);
    float sign = Mathf.Sign(Vector3.Dot(n,Vector3.Cross(a,b)));

    // angle in [-179,180]
    float signed_angle = angle * sign;

    // angle in [0,360] (not used but included here for completeness)
    //float angle360 =  (signed_angle + 180) % 360;

    return signed_angle;
}*/

public class Vec3f
{
	private static final boolean DEBUG = true;

	// Define a private static DecimalFormat to be used by our toString() method.
	// Note: '0' means put a 0 there if it's zero, '#' means omit if zero.
	private static DecimalFormat df = new DecimalFormat("0.000");

	// A Vec3 simply has three properties called x, y and z - these are public so we can access them directly for speed
	public float x, y, z;

	// ------------ Constructors ------------

	// Default constructor
	public Vec3f()
	{
		// x, y and z are initialised to zero by default as that's how primitives are initialised in Java
	}

	// Single parameter constructor sets the same value across all three components
	public Vec3f(float value)
	{
		x = y = z = value;
	}

	// Three parameter constructor
	public Vec3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// Copy constructor
	public Vec3f(Vec3f source)
	{
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
	}
	
	// ---------- Public Methods ----------
	
	/**
	 * Set the values of this Vec3f from three floats.
	 * 
	 * @param	x	The x value to set.
	 * @param	y	The y value to set.
	 * @param	z	The z value to set.
	 */
	public void set(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Set the values of this Vec3f from a source Vec3f.
	 * 
	 * @param	source	The Vec3f from which to copy the component values.
	 */
	public void set(Vec3f source)
	{
		this.x = source.x;
		this.y = source.y;
		this.z = source.z;
	}
	
//	public Vec3f minus(Vec3f source)
//	{
//		return new Vec3f(this.x - source.x,this.y - source.y, this.z - source.z);
//	}
	
	
	/**
	 * Return whether this Vec3f is approximately equal to the destination Vec3f.
	 * <p>
	 * For two Vec3fs to be considered approximately equal, each component must be within the
	 * provided epsilon.
	 * 
	 * @param	dest	The destination Vec3f to compare to this Vec3f
	 * @param	epsilon	The maximum difference between components in the vectors
	 * @return			A true or false value, indicating whether the two vectors are approximately equal.
	 */
	public boolean approximatelyEquals(Vec3f dest, float epsilon)
	{	
		float xDiff = Math.abs(x - dest.x);
		float yDiff = Math.abs(y - dest.y);
		float zDiff = Math.abs(z - dest.z);
		
		if (xDiff <= epsilon && yDiff <= epsilon && zDiff <= epsilon)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static Vec3f clone(Vec3f source)
	{
		return new Vec3f(source.x, source.y, source.z);
	}	

	// Deliberately no property getters and setters for performance reasons - access members directly! */

	// ------------ Helper methods ------------

	/**
	 * Return whether the length of this Vec3f is approximately equal to a given value to within seven decimal places.
	 * <p>
	 * It is common for us to want to determine whether a Vec3f is normalised or not, and if it's not normalised then
	 * normalise it. A normalised Vec3f will have a length (i.e. magnitude) of 1.0f - but we cannot use floating point
	 * comparison such as '== 1.0f' or '!= 1.0f' to do so, as small rounding errors in the normalisation process mean
	 * that the length of a normalised Vec3f may not be precisely 1.0f, and instead may be a value such as 0.99999994f
	 * or 1.0000005.
	 * <p>
	 * This method returns true if the difference between the length of this vector and the provided value are less than
	 * or equal to 0.000_000_9f, otherwise it returns false.
	 * @param	value	The value to compare the length of this vector to.
	 * @return			A boolean indicating whether the length of this vector is approximately the same as the provided value.
	 */
	public boolean lengthIsApproximately(float value)
	{
		if ( Math.abs(this.length() - 1.0f) < 0.000_000_9f)
		{
			return true;
		}
		
		return false;
	}
	
	/** Set each component of this Vec3f to 0.0f. */
	public void zero()
	{
		x = y = z = 0.0f;
	}

	/**
	 * Negate this Vec3f.
	 * <p>
	 * Each component is negated, so for example, the Vec3f (1, -2, 3) would become (-1, 2, -3).
	 * This can be useful for, amongst other things, reversing the direction of a vector.
	 * <p>
	 * This method does not return 'this' for chaining so the negation cannot be an unintended
	 * side-effect of calling this method in a chain. If you want to return a negated version
	 * of a Vec3f without modifying 'this' Vec3f then use the {@link #negated()} method instead.
	 */
	public void negate()
	{
		x = -x;
		y = -y;
		z = -z;
	}

	/**
	 * Return a negated version of this Vec3f - no changes are made to this Vec3f itself.
	 * <p>
	 * Each component is negated, so for example, running this method on the Vec3f (1, -2, 3) would
	 * return a new Vec3f with component values (-1, 2, -3).
	 * 
	 * @return	A negated version of this Vec3f
	 */
	public Vec3f negated()
	{
		return new Vec3f(-x, -y, -z);
	}

	
	/**
	 * Normalise this Vec3f.
	 * <p>
	 * If this Vec3f has a magnitude of zero then the unmodified Vec3f is returned.
	 * 
	 * @return	This normalised Vec3f, or the original Vec3f if the magnitude is zero.
	 */
	public Vec3f normalise()
	{
		// Calculate the magnitude of our vector
		float magnitude = (float) Math.sqrt((x * x) + (y * y) + (z * z));

		// As long as the magnitude is greater then zero, divide each element by the
		// magnitude to get the normalised value between -1 and +1.
		// Note: If the vector has a magnitude of zero we simply return it - we
		// could instead throw a RuntimeException here but
		if (magnitude > 0.0f)
		{
			x /= magnitude;
			y /= magnitude;
			z /= magnitude;
		}

		// Return this for chaining
		return this;
	}

	// FIXME: This is the SCALAR PRODUCT of two vectors - to be the DOT PRODUCT the input vectors must be normalised, which
	// makes the output between -1 and +1. For the scalar product the result will not be normalised in this manner!
	//
	// Static method to calculate and return the scalar dot product of two
	// vectors.
	//
	// Note: The dot product of two vectors tells us things about the angle
	// between the vectors. That is, it tells us if they are pointing in the
	// same direction (i.e. are they parallel? If so, the dot product will be
	// 1), or if they're perpendicular (i.e. at 90 degrees to each other) the
	// dot product will be 0, or if they're pointing in opposite directions
	// then the dot product will be -1.
	//
	// Usage example: float foo = Vec3.dotProduct(vectorA, vectorB);
	//
	public static float dotProduct(Vec3f a, Vec3f b)
	{
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	// Non-static method to calculate and return the scalar dot product of this
	// vector and another vector
	//
	// Usage example: double foo = vectorA.dot(vectorB);
	//
	public float dot(Vec3f vec)
	{
		return x * vec.x + y * vec.y + z * vec.z;
	}

	// Static method to calculate and return a vector which is the cross product
	// of two vectors
	//
	// Note: The cross product is simply a vector which is perpendicular to the
	// plane formed by the first two vectors. Think of a desk like the one your
	// laptop or keyboard is sitting on. If you put one pencil pointing directly
	// away from you, and then another pencil pointing to the right so they form
	// an "L" shape, the vector perpendicular to the plane made by these two
	// pencils points directly upwards.
	//
	// Whether the vector is perpendicularly pointing "up" or "down" depends on
	// the "handedness" of the/ coordinate system that you're using.
	//
	// Further reading: http://en.wikipedia.org/wiki/Cross_product
	//
	// Usage example: Vec3 crossVector = Vec3.crossProduct(vectorA, vectorB);
	//
	public static Vec3f crossProduct(Vec3f vec1, Vec3f vec2)
	{
		return new Vec3f(vec1.y * vec2.z - vec1.z * vec2.y,
				         vec1.z * vec2.x - vec1.x * vec2.z,
			             vec1.x * vec2.y - vec1.y * vec2.x);
	}

	// Non-static method to return the cross product of this vector and another vector
	public Vec3f cross(Vec3f vec)
	{
		return new Vec3f(y * vec.z - z * vec.y,
				z * vec.x - x * vec.z,
				x * vec.y - y * vec.x);
	}

	// Method to return the distance between two vectors in 3D space
	//
	// Note: This is accurate, but not especially fast - depending on your needs
	// you might like to use the Manhattan Distance instead, see:
	//
	//     http://en.wikipedia.org/wiki/floataxicab_geometry
	//
	// There's also a good discussion of it here:
	//
	//     http://stackoverflow.com/questions/3693514/very-fast-3d-distance-check
	//
	// The gist is, to find if we're within a given distance between two vectors
	// you can use:
	//
	// bool within3DManhattanDistance(Vec3 v1, Vec3 v2, float distance)
	// {
	// 		float dx = Math.abs(v2.x - v1.x);
	// 		if (dx > distance) return false; // too far in x direction
	//
	// 		float dy = Math.abs(v2.y - v1.y);
	// 		if (dy > distance) return false; // too far in y direction
	//
	// 		float dz = abs(v2.z - v1.z);
	// 		if (dz > distance) return false; // too far in z direction
	//
	// 		return true; // we're within the cube
	// }
	//
	// Or to just calculate the straight Manhattan distance you could use:
	//
	// float getManhattanDistance(Vec3 v1, Vec3 v2)
	// {
	// 		float dx = abs(v2.x - v1.x);
	// 		float dy = abs(v2.y - v1.y);
	// 		float dz = abs(v2.z - v1.z);
	//
	// 		return dx + dy + dz;
	// }
	//
	public static float distanceBetween(Vec3f v1, Vec3f v2)
	{
		// Pre-calculate values used more than once
		float dx = v2.x - v1.x;
		float dy = v2.y - v1.y;
		float dz = v2.z - v1.z;

		return (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	// Method to return the length / magnitude of the a vector
	public float length()
	{
		return (float)Math.sqrt(x * x + y * y + z * z);
	}

	public static Vec3f abs(Vec3f source)
	{
		Vec3f absVector = new Vec3f();

		if (source.x < 0.0f) { absVector.x = -source.x; } else { absVector.x = source.x; }
		if (source.y < 0.0f) { absVector.y = -source.y; } else { absVector.y = source.y; }
		if (source.z < 0.0f) { absVector.z = -source.z; } else { absVector.z = source.z; }

		return absVector;
	}

	// Method to quickly generate a vector perpendicular to another one
	// Further reading: http://blog.selfshadow.com/2011/10/17/perp-vectors/
	public static Vec3f genPerpendicularVectorQuick(Vec3f u)
	{
		if (Math.abs(u.y) < 0.99)
		{
			return new Vec3f(-u.z, 0.0f, u.x); // cross(u, UP)
		}
		else {
			return new Vec3f(0.0f, u.z, -u.y); // cross(u, RIGHT)
		}
	}

	// Method to generate a vector perpendicular to another one using the Hughes-Muller method
	// Further reading: Hughes, J. F., Muller, T., "Building an Orthonormal Basis from a Unit Vector", Journal of Graphics Tools 4:4 (1999), 33-35.
	public static Vec3f genPerpendicularVectorHM(Vec3f u)
	{
		// Get the absolute source vector
		Vec3f a = Vec3f.abs(u);

		if (a.x <= a.y && a.x <= a.z)
		{
			return new Vec3f(0.0f, -u.z, u.y);
		} else if (a.y <= a.x && a.y <= a.z)
		{
			return new Vec3f(-u.z, 0, u.x);
		} else
		{
			return new Vec3f(-u.y, u.x, 0);
		}
	}

	// Further reading: Stark, M. M., "Efficient Construction of Perpendicular Vectors without Branching", Journal of Graphics Tools 14:1 (2009), 55-61.
	/*Vec3f genPerpendicularVectorStark(Vec3f u)
	{
		// Get the absolute source vector
	    Vec3f a = Vec3f.abs(u);

	    unsigned int = SIGNBIT(a.x - a.y);
	    uint uzx = SIGNBIT(a.x - a.z);
	    uint uzy = SIGNBIT(a.y - a.z);

	    uint xm = uyx & uzx;
	    uint ym = (1^xm) & uzy;
	    uint zm = 1^(xm & ym);

	    float3 v = cross(u, float3(xm, ym, zm));
	    return v;
	}*/


	/**
	 * Return the direction unit vector between two Vec3f objects.
	 * 
	 * @param	start	The start location
	 * @param	end		The end location
	 * @return			The direction unit vector between the start and end Vec3f objects
	 */
	public static Vec3f getDirectionUV(Vec3f start, Vec3f end)
	{
		return end.minus(start).normalise();
	}

	// Method to calculate and return the angle between two vectors in radians
	public static float getAngleBetweenRads(Vec3f a, Vec3f b)
	{
		// Normalise our vectors
		// Note: If we do NOT normalise the vectors then we must use the calculation:
		// float result = (float)Math.acos( a.dot(b) ) / ( a.length() * b.length() );
		a.normalise();
		b.normalise();

		return (float)Math.acos( a.dot(b) );
	}
	
	// Method to calculate and return the angle between two vectors in radians
	public static float getAngleBetweenDegs(Vec3f a, Vec3f b)
	{
		// Normalise our vectors
		// Note: If we do NOT normalise the vectors then we must use the calculation:
		// float result = (float)Math.acos( a.dot(b) ) / ( a.length() * b.length() );
		a.normalise();
		b.normalise();

		return (float)Math.acos( a.dot(b) ) * Utils.RADS_TO_DEGS;
	}

	/*public static Vec3f getAlignedAngleBetweenDegs(Vec3f v1, Vec3f v2)
    {
    	v1 = new Vec3f(1.0f, 1.0f, -1.0f).normalise();
		v2 = new Vec3f(2.0f, 1.0f, -4.0f).normalise();

    	Vec3f axis = v1.cross(v2).normalise();

		float angleDegs = Vec3f.getAngleBetweenDegs(v1,  v2);

		Vec3f v3 = Vec3f.rotateVectorAboutAxisDegs(v1, angleDegs, axis);

		Vec3f v4 = Vec3f.rotateVectorAboutAxisDegs(v3, angleDegs, new Vec3f(1.0f, 0.0f, 0.0f) );




		Mat4f mvp = Application.projectionMatrix.times(Application.viewMatrix);

		Line line = new Line();
		line.draw(new Vec3f(0.0f), v1.times(100.0f), new Vec3f(1.0f, 1.0f, 0.0f), 5.0f, mvp);
		line.draw(new Vec3f(0.0f), v2.times(100.0f), new Vec3f(1.0f, 0.0f, 0.0f), 5.0f, mvp);

		line.draw(new Vec3f(0.0f), v3.times(100.0f), new Vec3f(0.0f, 0.0f, 1.0f), 5.0f, mvp);
		line.draw(new Vec3f(0.0f), v4.times(100.0f), new Vec3f(0.0f, 1.0f, 1.0f), 5.0f, mvp);



		return v3;
    }
	 */

	/*public static Vec3f getAlignedAngleBetweenDegsCRAZY_BROKEN(Vec3f v1, Vec3f v2)
    {
    	//Vec3f v1 = new Vec3f(1.0f, -0.2f, 0.0f).normalise();
		//Vec3f v2 = new Vec3f(1.0f, 1.0f, 1.0f).normalise();

		Vec3f v3 = v1.cross(v2).normalise();
		Vec3f v4 = v3.cross(v1).normalise();

		Mat3f m1 = new Mat3f();
		m1.setXBasis(v1);
		m1.setYBasis(v4);
		m1.setZBasis(v3);

		float cosAngle = v2.dot(v1);
		float sinAngle = v2.dot(v4);

		Mat3f m2 = new Mat3f();
		m2.setXBasis( new Vec3f(cosAngle, -sinAngle, 0.0f)  );
		m2.setYBasis( new Vec3f(sinAngle, cosAngle, 0.0f) );
		m2.setZBasis( new Vec3f(0.0f, 0.0f, 1.0f) );

		Mat3f m1Inverse = m1.inverse();

		Mat3f transformMatrix = m1Inverse.times(m2).times(m1);

		Vec3f v1Transformed = transformMatrix.times(v1);
		//System.out.println("v1 transformed: " + v1Transformed);

		Vec3f v2Transformed = transformMatrix.times(v2);
		//System.out.println("v2 transformed: " + v2Transformed);


		Mat4f mvp = Application.projectionMatrix.times(Application.viewMatrix);

		Line line = new Line();
		line.draw(new Vec3f(0.0f), v1.times(100.0f), new Vec3f(1.0f, 0.0f, 0.0f), 5.0f, mvp);
		line.draw(new Vec3f(0.0f), v2.times(100.0f), new Vec3f(1.0f, 0.0f, 0.0f), 5.0f, mvp);

		line.draw(new Vec3f(0.0f), v1Transformed.times(100.0f), new Vec3f(0.0f, 0.0f, 1.0f), 5.0f, mvp);
		line.draw(new Vec3f(0.0f), v2Transformed.times(100.0f), new Vec3f(0.0f, 1.0f, 1.0f), 5.0f, mvp);

		//Vec3f correctedAlignedVector = v2.plus(v1Transformed); NO, THIS DOESN'T WORK

		Vec3f correctedAlignedVector = v1Transformed;//.plus(v1Transformed);

		//line.draw(new Vec3f(0.0f), correctedAlignedVector.times( v1.dot(v2) * 100.0f), new Vec3f(1.0f, 1.0f, 0.0f), 5.0f, projectionMatrix.times(viewMatrix));

		float v1Andv2AngleDegs = Vec3f.getAngleBetweenDegs(v1, v2);
		float correctedAlignedVectorAndv2AngleDegs = Vec3f.getAngleBetweenDegs(correctedAlignedVector, v2);

		System.out.println("v1 to v2 angle       : " + v1Andv2AngleDegs);
		System.out.println("corrected to v2 angle: " + correctedAlignedVectorAndv2AngleDegs);

		//return correctedAlignedVectorAndv2AngleDegs;

		return correctedAlignedVector;//Andv2AngleDegs;
    }
	 */

	//TODO: Requires testing!!!!
	public static Vec3f rotateZRads(Vec3f source, float angleRads)
	{
		// Rotation about the z-axis:
		// x' = x*cos q - y*sin q
		// y' = x*sin q + y*cos q
		// z' = z

		float cosTheta = (float)Math.cos(angleRads);
		float sinTheta = (float)Math.sin(angleRads);

		Vec3f result = new Vec3f();

		result.x = source.x * cosTheta - source.y * sinTheta;
		result.y = source.x * sinTheta + source.y * cosTheta;
		result.z = source.z;

		return result;
	}

	//TODO: Requires testing!!!!
	public static Vec3f rotateYRads(Vec3f source, float angleRads)
	{
		// Rotation about the y axis:
		// x' = z*sin q + x*cos q
		// y' = y
		// z' = z*cos q - x*sin q

		float cosTheta = (float)Math.cos(angleRads);
		float sinTheta = (float)Math.sin(angleRads);

		Vec3f result = new Vec3f();

		result.x = source.z * sinTheta + source.x * cosTheta;
		result.y = source.y;
		result.z = source.z * cosTheta - source.x * sinTheta;

		return result;
	}

	// WORKS WELL!
	public static Vec3f rotateXRads(Vec3f source, float angleRads)
	{
		// Rotation about the x-axis:
		// x' = x
		// y' = y*cos q - z*sin q
		// z' = y*sin q + z*cos q

		float cosTheta = (float)Math.cos(angleRads);
		float sinTheta = (float)Math.sin(angleRads);

		Vec3f result = new Vec3f();

		result.x = source.x;
		result.y = source.y * cosTheta - source.z * sinTheta;
		result.z = source.y * sinTheta + source.z * cosTheta;

		return result;
	}

	public static Vec3f rotateVectorAboutAxisRads(Vec3f source, float angleRads, Vec3f axis)
	{
		Mat3f rotationMatrix = new Mat3f();

		float sinTheta         = (float)Math.sin(angleRads);
		float cosTheta         = (float)Math.cos(angleRads);
		float oneMinusCosTheta = 1.0f - cosTheta;

		// Calculate rotated x-axis
		rotationMatrix.m00 = axis.x * axis.x * oneMinusCosTheta + cosTheta;
		rotationMatrix.m01 = axis.x * axis.y * oneMinusCosTheta + axis.z * sinTheta;
		rotationMatrix.m02 = axis.x * axis.z * oneMinusCosTheta - axis.y * sinTheta;

		// Calculate rotated y-axis
		rotationMatrix.m10 = axis.y * axis.x * oneMinusCosTheta - axis.z * sinTheta;
		rotationMatrix.m11 = axis.y * axis.y * oneMinusCosTheta + cosTheta;
		rotationMatrix.m12 = axis.y * axis.z * oneMinusCosTheta + axis.x * sinTheta;

		// Calculate rotated z-axis
		rotationMatrix.m20 = axis.z * axis.x * oneMinusCosTheta + axis.y * sinTheta;
		rotationMatrix.m21 = axis.z * axis.y * oneMinusCosTheta - axis.x * sinTheta;
		rotationMatrix.m22 = axis.z * axis.z * oneMinusCosTheta + cosTheta;

		return rotationMatrix.times(source);
	}

	// THIS WORKS GREAT!
	public static Vec3f rotateVectorAboutAxisDegs(Vec3f source, float angleDegs, Vec3f axis)
	{
		Mat3f rotationMatrix = new Mat3f();

		float sinTheta         = (float)Math.sin(angleDegs * Utils.DEGS_TO_RADS);
		float cosTheta         = (float)Math.cos(angleDegs * Utils.DEGS_TO_RADS);
		float oneMinusCosTheta = 1.0f - cosTheta;

		// Calculate rotated x-axis
		rotationMatrix.m00 = axis.x * axis.x * oneMinusCosTheta + cosTheta;
		rotationMatrix.m01 = axis.x * axis.y * oneMinusCosTheta + axis.z * sinTheta;
		rotationMatrix.m02 = axis.x * axis.z * oneMinusCosTheta - axis.y * sinTheta;

		// Calculate rotated y-axis
		rotationMatrix.m10 = axis.y * axis.x * oneMinusCosTheta - axis.z * sinTheta;
		rotationMatrix.m11 = axis.y * axis.y * oneMinusCosTheta + cosTheta;
		rotationMatrix.m12 = axis.y * axis.z * oneMinusCosTheta + axis.x * sinTheta;

		// Calculate rotated z-axis
		rotationMatrix.m20 = axis.z * axis.x * oneMinusCosTheta + axis.y * sinTheta;
		rotationMatrix.m21 = axis.z * axis.y * oneMinusCosTheta - axis.x * sinTheta;
		rotationMatrix.m22 = axis.z * axis.z * oneMinusCosTheta + cosTheta;

		return rotationMatrix.times(source);
	}


	// Overloaded toString method
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("x: " + df.format(x) + ",\ty: " + df.format(y) + ",\tz: " + df.format(z) );
		return sb.toString();
	}

	// ------------ Utility methods ------------

	// Method to add a vector to an existing vector and return the resulting vector
	public Vec3f plus(Vec3f vec)
	{
		return new Vec3f(x + vec.x, y + vec.y, z + vec.z);
	}

	/*** DO NOT DO THIS: doing 'c = a.add(b)' will increment a when you wanted the RESULT OF adding these together to be in c! ***/
	// Method to add a vector to an existing vector
	/*public Vec3f add(Vec3f vec)
	{
		x += vec.x;
		y += vec.y;
		z += vec.z;

		// Return this for chaining
		return this;
	}*/

	// Operator to subtract a vector from an existing vector and return the resulting vector
	public Vec3f minus(Vec3f vec)
	{
		return new Vec3f(x - vec.x, y - vec.y, z - vec.z);
	}

	// Method to multiply a vector by another vector and return the resulting vector
	public Vec3f times(Vec3f vec)
	{
		return new Vec3f(x * vec.x, y * vec.y, z * vec.z);
	}

	// Method to multiply an existing vector by a scalar value and return the resulting vector
	public Vec3f times(float scale)
	{
		return new Vec3f(x * scale, y * scale, z * scale);
	}

	// Method to divide an existing vector by a value and return the resulting
	// Vec3
	public Vec3f divide(float value)
	{
		return new Vec3f(x / value, y / value, z / value);
	}

	

	private static int zcross(Vec3f u, Vec3f v)
	{
		// This was:
		//float p = u.x * v.y - v.x * u.y;

		float p = u.z * v.y - v.z * u.y;

		if (p < 0.0f)
		{
			System.out.println("ZCROSS SAYS NEGATIVE!");
			return -1;
		}

		if (p > 0.0f)
		{
			System.out.println("ZCROSS SAYS POSITIVE!");
			return 1;
		}

		System.out.println("ZCROSS SAYS PERPENDICULAR!");
		return 0;
	}
	
	/**
	 * Validate a 3D direction unit vector to ensure that it does not have a magnitude of zero.
	 * <p>
	 * If the direction unit vector has a magnitude of zero then an IllegalArgumentException is thrown.
	 * @param	directionUV	The direction unit vector to validate
	 */
	public static void validateDirectionUV(Vec3f directionUV)
	{
		// Ensure that the magnitude of this direction unit vector is not zero
		if ( !(directionUV.length() > 0.0f) )
		{
			throw new IllegalArgumentException("Direction unit vector cannot be zero.");
		}
	}

} // End of Vec3f class
