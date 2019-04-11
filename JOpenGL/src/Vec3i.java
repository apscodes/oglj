

public class Vec3i
{
	// --- Properties ---

	public int x, y, z;

	// --- Methods ---

	// Default constructor
	public Vec3i()
	{
		// The default value of x, y and z will be zero as that's how Java primitives are initialised
	}

	// Three parameter constructor
	public Vec3i(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/* Deliberately no getters and setters for performance reasons - access member properties directly! */

	@Override
	public String toString()
	{
		String s = "(" + x + ", " + y + ", " + z + ")";
		return s;
	}

}
