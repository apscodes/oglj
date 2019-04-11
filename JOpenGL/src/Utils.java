

import java.io.Console;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.Random;

public class Utils
{
	public static final float DEGS_TO_RADS = (float)Math.PI / 180.0f;
	public static final float RADS_TO_DEGS = 180.0f / (float)Math.PI;

	public static final Vec3f X_AXIS = new Vec3f(1.0f, 0.0f, 0.0f);
	public static final Vec3f Y_AXIS = new Vec3f(0.0f, 1.0f, 0.0f);
	public static final Vec3f Z_AXIS = new Vec3f(0.0f, 0.0f, 1.0f);

	// Define a private static DecimalFormat to be used by our toString() method.
	// Note: '0' means put a 0 there if it's zero, '#' means omit if zero.
	public static DecimalFormat df = new DecimalFormat("0.000");
	
	public static String newLine = System.lineSeparator();

	/**
	 * A Random object used to generate random numbers in the randRange methods.
	 * @see #randRange(float, float)
	 * @see #randRange(int, int)
	 */ 
	public static final Random random = new Random();

	/**
	 * Return a random floating point value between the half-open range [min..max).
	 * <p>
	 * This means that, for example, a call to {@code randRange(-5.0f, 5.0f) may return a value between -5.0f up to a maximum of 4.999999f.
	 * @param	min	The minimum value
	 * @param	max The maximum value
	 * @return		A random float within the range specified.
	 */
	public static float randRange(float min, float max)
	{
		return random.nextFloat() * (max - min) + min;
	}
	
	/**
	 * Return a random integer value between the half-open range (min..max]
	 * <p>
	 * This means that, for example, a call to {@code randRange(-5, 5) will return a value between -5 up to a maximum of +5.
	 * @param	min	The minimum value
	 * @param	max The maximum value
	 * @return		A random int within the range specified.
	 */
	public static int randRange(int min, int max)
	{
		return random.nextInt( (max - min) + 1 ) + min;
	}

	/**
	 * Return the co-tangent of an angle specified in radians.
	 * <p>
	 * The co-tangent is simply 1.0f / tan(angleRads).
	 * @param	angleRads	The angle specified in radians to return the co-tangent of.
	 * @return				The co-tangent of the specified angle.
	 */  
	public static float cot(float angleRads)
	{
		return (float)( 1.0f / Math.tan(angleRads) );
	}
	
	/**
	 * Convert an angle specified in radians into degrees and return it.
	 * @param	angleRads	The angle to convert specified in radians.
	 * @return				The converted angle in degrees.
	 */
	public static float radiansToDegrees(float angleRads)
	{
		return angleRads * RADS_TO_DEGS;
	}

	/**
	 * Convert an angle specified in degrees into radians and return it.
	 * @param	angleDegs	The angle to convert specified in degrees.
	 * @return				The converted angle in radians.
	 */
	public static float degreesToRadians(float degrees)
	{
		return degrees * DEGS_TO_RADS;
	}	

	public static FloatBuffer createFloatBuffer(int numFloats)
	{
		// Create a ByteBuffer to store a number of floats.
		//
		// Note: Float.BYTES is currently 4 (i.e. that is, each float is stored as 4 bytes, so
		// if we ask for a buffer to hold 2 floats, it'll allocate a ByteBuffer of size 8 bytes).
		//
		// Also: OpenGL requires direct access to our memory, so we must use 'allocateDirect' rather than just 'allocate' or our code will fail.
		// Further reading: http://stackoverflow.com/questions/5670862/bytebuffer-allocate-vs-bytebuffer-allocatedirect
		ByteBuffer bb = ByteBuffer.allocateDirect(numFloats * Float.BYTES);

		// We MUST specify the 'endian-ness' of the data in the ByteBuffer or our code will fail silently (and nothing will get drawn!)
		bb.order( ByteOrder.nativeOrder() );

		// Create a FloatBuffer version of our ByteBuffer
		FloatBuffer fb = bb.asFloatBuffer();

		// Set the original ByteBuffer to null so that the garbage collector can reclaim its memory
		bb = null;

		// Finally, return the FloatBuffer version of our ByteBuffer
		return fb;

		// Note: This entire procedure could be done in one hit with the following line, but I've broken it up above for ease of understanding.
		//return ByteBuffer.allocateDirect(numFloats * Float.BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
	}
	
	public static DoubleBuffer createDoubleBuffer(int numDoubles)
	{
		// Create a ByteBuffer to store a number of floats.
		//
		// Note: Double.BYTES is currently 8 (i.e. that is, each double is stored as 8 bytes, so
		// if we ask for a buffer to hold 2 doubles, it'll allocate a ByteBuffer of size 16 bytes).
		//
		// Also: OpenGL requires direct access to our memory, so we must use 'allocateDirect' rather than just 'allocate' or our code will fail.
		// Further reading: http://stackoverflow.com/questions/5670862/bytebuffer-allocate-vs-bytebuffer-allocatedirect
		ByteBuffer bb = ByteBuffer.allocateDirect(numDoubles * Double.BYTES);

		// We MUST specify the 'endian-ness' of the data in the ByteBuffer or our code will fail silently (and nothing will get drawn!)
		bb.order( ByteOrder.nativeOrder() );

		// Create a FloatBuffer version of our ByteBuffer
		DoubleBuffer db = bb.asDoubleBuffer();

		// Set the original ByteBuffer to null so that the garbage collector can reclaim its memory
		bb = null;

		// Finally, return the FloatBuffer version of our ByteBuffer
		return db;

		// Note: This entire procedure could be done in one hit with the following line, but I've broken it up above for ease of understanding.
		//return ByteBuffer.allocateDirect(numDoubles * Double.BYTES).order(ByteOrder.nativeOrder()).asDoubleBuffer();
	}
	

	/** Print a message to the console and make the user press Enter to continue. */
	public static void waitForEnter()
	{
		Console c = System.console();
		if (c != null)
		{
			c.format("Press ENTER to proceed." + newLine);
			c.readLine();
		}
	}

	public static ByteBuffer createByteBuffer(int nUM_INDICES) {
		ByteBuffer bb = ByteBuffer.allocateDirect(nUM_INDICES * Double.BYTES);

		// We MUST specify the 'endian-ness' of the data in the ByteBuffer or our code will fail silently (and nothing will get drawn!)
		bb.order( ByteOrder.nativeOrder() );
		
		return bb;
	}

} // End of Utils class
