import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

// Perform import of all static constants and methods from the following packages
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * A class to represent an OpenGL shader program.
 * <p>
 * This class is adapted (with thanks!) from: http://schabby.de/opengl-shader-example/
 * 
 * This Class is a cut down version of a class written by Al Lansley. This version was /
 * prepared by Richard Dazeley.
 * 
 * @author	Al Lansley
 * @version  0.6 - 21/12/2014
 */
public class ShaderProgram {
	// ---------- Properties ----------
	
	/** The newline character for this system */
	private static final String newLine = System.lineSeparator();
	
	/** The id value for our OpenGL shader program */
	private int mProgramId;
	
	/** A flag to keep track of whether this shader program has been successfully initialised. */
	private boolean mInitialised = false;
	
	/**
	 * Constructor.
	 */
	public ShaderProgram()
	{
		// Create the shader program
		// Note: We need to have a valid OpenGL context for this and any further shader operations to succeed.
		mProgramId = glCreateProgram();
		
		// initialise this program to be the active program.
		glUseProgram(mProgramId);
	}
	
	/**
	 * Load and compile vertex and fragment shaders from file, then link them to this shader program
	 * and validate its status.
	 * <p>
	 * If the loading, compilation, linkage or validation processes fail then a RuntimeException is thrown.
	 * 
	 * @param  vertexShaderFilename
	 * @param  fragmentShaderFilename
	 */
	public void initFromFiles(String vertexShaderFilename, String fragmentShaderFilename)
	{
		// Load and compile the two shaders
		int vertShaderId = compileShaderFromFile(vertexShaderFilename,   GL_VERTEX_SHADER);
		int fragShaderId = compileShaderFromFile(fragmentShaderFilename, GL_FRAGMENT_SHADER);

		// Link the shaders to the shader program and validate it
		linkAndValidateProgram(vertShaderId, fragShaderId);
		
		// Set the flag to indicate our shader program creation was successful
		mInitialised = true;
	}
	
	/**
	 * Compile vertex and fragment shaders from Strings, then link them to this shader program
	 * and validate its status.
	 * <p>
	 * If the compilation, linkage or validation processes fail then a RuntimeException is thrown.
	 * 
	 * @param  vertexShaderFilename
	 * @param  fragmentShaderFilename
	 */
	public void initFromStrings(String vertexShaderSource, String fragmentShaderSource)
	{
		// Load and compile the two shaders
		int vertShaderId = compileShaderFromString(vertexShaderSource,   GL_VERTEX_SHADER);
		int fragShaderId = compileShaderFromString(fragmentShaderSource, GL_FRAGMENT_SHADER);

		// Link the shaders to the shader program and validate it
		linkAndValidateProgram(vertShaderId, fragShaderId);
		
		// Set the flag to indicate our shader program creation was successful
		mInitialised = true;
	}
	/**
	 * Get the shader program id.
	 * 
	 * @return  The program id.
	 */
	public int getProgramId()
	{
		return mProgramId;
	}

	/**
	 * Set this ShaderProgram to be the one in use.
	 * <p>
	 * If the shader program is not initialised before attempting to be used then a RuntimeException is thrown.
	 */
	public void use()
	{
		if (mInitialised)
		{
			glUseProgram(mProgramId);
		}
		else
		{
			throw new RuntimeException("Shader with program id " + mProgramId + " is not initialised -  initialise with initFromFiles() or initFromStrings() first.");
		}
	}

	/** 
	 * Disable this shader program so that it is no longer in use.
	 */
	public void disable() 
	{ 
		glUseProgram(0); 
	}
	
	// ---------- Private Methods ----------
	
	
	/**
	 * Load a shader from file.
	 * <p>
	 * If loading the file fails at any point a RuntimeException is thrown.
	 * 
	 * @param  filename  The name of the file to load
	 */
	private String loadFile(String filename)
	{	
		StringBuilder shaderSourceSB = new StringBuilder();
		String line = null;

		// Open the file specified
		// Note: By enclosing what we are trying in parenthesis after the the 'try' statement,
		// we are engaging the AutoClosable interface which BufferedReader implements, so
		// we don't need to worry about closing it - whether there's an exception or if we
		// finish clean, as soon as the object goes out of scope it will close itself.
		try ( BufferedReader reader = new BufferedReader(new FileReader(filename)) )
		{

			// Read lines from the file and append them to the StringBuilder while there are lines to read
			while( (line = reader.readLine()) != null )
			{
				shaderSourceSB.append(line + newLine);
			}

			// No need to close the reader - it's specified as Auto-Closable in try block.
		}
		catch (Exception e)
		{
			throw new RuntimeException("Unable to load shader from file: " + filename + ".", e);
		}

		// Return the shader source as a String
		return shaderSourceSB.toString();
	}
	
	/**
	 *  Load and compile a shader from file.
	 *  <p>
	 *  If an absolute path to the shader file is not used, then the relative path to the file will
	 *  be dependent on the configuration of your Java project. Typically, placing the shader file
	 *  in the top level of the project (above any packages) will allow it to be loaded by specifying
	 *  only its filename.
	 *  <p>
	 *  If the shader could not be created, or if there was a error compiling the GLSL source code
	 *  for the shader then a RuntimeException is thrown.
	 *  
	 *  @param  filename    The name of the file to load.
	 *  @param  shaderType  The type of shader to create, typically GL_VERTEX_SHADER or GL_FRAGMENT_SHADER
	 *  @return             The shader id
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glCreateShader.xml">glCreateShader</a>
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glShaderSource.xml">glShaderSource</a>
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glCompileShader.xml">glCompileShader</a>
	 */ 
	private int compileShaderFromFile(String filename, int shaderType)
	{
		// Load the shader source from file into a String...
		String shaderSource = loadFile(filename);

		// ...then compile it and return the shader id using our compile from string method.
		return compileShaderFromString(shaderSource, shaderType);
	}

	// Method to load a shader from file and compile it
	/**
	 *  Load and compile a shader from GLSL source code provided as a String.
	 *  <p>	 *  
	 *  If the shader could not be created, or if there was a error compiling the GLSL source code
	 *  for the shader then a RuntimeException is thrown.
	 *  
	 *  @param  shaderSource  The GLSL source code for the shader.
	 *  @param  shaderType    The type of shader to create, typically GL_VERTEX_SHADER or GL_FRAGMENT_SHADER.
	 *  @return               The shader id
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glCreateShader.xml">glCreateShader</a>
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glShaderSource.xml">glShaderSource</a>
	 *  @see  <a href="https://www.opengl.org/sdk/docs/man/docbook4/xhtml/glCompileShader.xml">glCompileShader</a>
	 */ 
	private int compileShaderFromString(String shaderSource, int shaderType)
	{
		// The shaderId will be non-zero if successfully created
		int shaderId = glCreateShader(shaderType);

		// Throw a RuntimeException is there was a problem creating the shader
		if (shaderId == 0)
		{	
			throw new RuntimeException( "Shader creation failed: " + glGetProgramInfoLog(mProgramId, 1000) );
		}
		// Attach the GLSL source code to the shader
		glShaderSource(shaderId, shaderSource);

		// Compile the shader
		glCompileShader(shaderId);

		// We no longer need to keep the shader source around, so mark it as null so the GC can free the memory
		shaderSource = null;

		// Get the shader compilation status
		int shaderStatus = glGetShaderi(shaderId, GL_COMPILE_STATUS);

		// If the shader failed to compile then throw a RuntimeException with debug info 
		if (shaderStatus == GL11.GL_FALSE)
		{
			String prefixString = "";
			if (shaderType == GL_VERTEX_SHADER) {
				prefixString = "Vertex shader compilation failed: ";
			}
			else {
				prefixString = "Fragment shader compilation failed: ";
			}			
			throw new RuntimeException(prefixString + glGetShaderInfoLog(shaderId, 1000) );
		}

		return shaderId;
	}
	
	/**
	 * Link the vertex and fragment shaders to a shader program and validate it.
	 * <p>
	 * As this same code is required from both the {@link #initFromFiles(String, String)} and
	 * {@link #initFromStrings(String, String)} method, it has been separated out into this
	 * private method.
	 * <p>
	 * If the program link or validation fails then a RuntimeException is thrown.
	 * 
	 * @param  vertShaderId  The vertex   shader Id to attach to the shader program
	 * @param  fragShaderId  The fragment shader Id to attach to the shader program
	 */
	private void linkAndValidateProgram(int vertShaderId, int fragShaderId)
	{
		// Attach the compiled shaders to the program by their id values
		glAttachShader(mProgramId, vertShaderId);
		glAttachShader(mProgramId, fragShaderId);

		// Link the shader program
		glLinkProgram(mProgramId);
		
		// Validate shader linking - bail on failure
		if (glGetProgrami(mProgramId, GL_LINK_STATUS) == GL_FALSE)
		{
			throw new RuntimeException( "Could not link shader program: " + glGetProgramInfoLog(mProgramId, 1000) );
		}

		// Detach the compiled shaders from the program
		// Note: Now that the shader programs are linked, the shader program keeps a copy of the linked shader code
		glDetachShader(mProgramId, vertShaderId);
		glDetachShader(mProgramId, fragShaderId);		

		// Perform general validation that the shader program is usable
		glValidateProgram(mProgramId);
		if (glGetProgrami(mProgramId, GL_VALIDATE_STATUS) == GL_FALSE)
		{
			throw new RuntimeException( "Could not validate shader program: " + glGetProgramInfoLog(mProgramId, 1000) );
		}
	}
}
