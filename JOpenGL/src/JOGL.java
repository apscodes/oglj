
import java.nio.ByteBuffer;
//import java.nio.FloatBuffer;

import java.nio.FloatBuffer;
import java.util.Random;


// Import libraries
import org.lwjgl.Sys;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

// Import static members to avoid having to use long prefixes
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.*;

public class JOGL {
	private FloatBuffer mModelMatrixFB;
	private Mat4f mModelMatrix;
	private int mIndexBufferId;
	private ByteBuffer mIndexByteBuffer;
	private static GLFWErrorCallback	errorCallback;
	private static long mWindowId;
	private static float mClearColorRed = 0.0f;
	private static float mClearColorGreen = 0.0f;
	private static float mClearColorBlue = 0.0f;
	private static float mClearColorAlpha = 0.0f;
	private static String mWindowTitle = "Lab03 Stage 2 Program";
	private static int mWindowWidth = 800;
	private static int mWindowHeight = 600;
	// Vertex storage constants
    final int VERTEX_COMPONENTS = 4; // x and y in 2D
	final int TEMP_COMPONENTS   = 4; // r,g,b,a
	final int COMPONENT_COUNT   = 8; 
	final int NUM_VERTICES      = 17; 
	final int TOTAL_FLOATS      = NUM_VERTICES * COMPONENT_COUNT;
	final int NUM_INDICES 		= 16;
	
	
	private ShaderProgram shaderProgram;
	private String vertShaderFilename = "tempVertexShader.vert";
	private String fragShaderFilename = "tempFragmentShader.frag";
	private FloatBuffer mVertexFloatBuffer;
	FloatBuffer mvpMatrixFloatBuffer = Utils.createFloatBuffer(16);
	private Mat4f mModelViewProjectionMatrix = Mat4f.createPerspectiveProjectionMatrix(90.0f, 1.0f, 0.1f, 100.0f);
	private int mTriangleVaoId, mTriangleVboId; 

	float[] mTriangleData = { 
			0.0f, 0.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            // Top
            -0.2f, 0.8f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            0.2f, 0.8f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.0f, 0.8f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 1.0f, -1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            // Bottom
            -0.2f, -0.8f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.2f, -0.8f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            0.0f, -0.8f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            0.0f, -1.0f, -1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            // Left
            -0.8f, -0.2f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            -0.8f, 0.2f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            -0.8f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            -1.0f, 0.0f, -1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
            // Right
            0.8f, -0.2f, -1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
            0.8f, 0.2f, -1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
            0.8f, 0.0f, -1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f,
            1.0f, 0.0f, -1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f};

    // indices to make triangles out of the vertex data

    byte indicesData[] = {
            // Top
            0, 3, 1,
            0, 2, 3,
            3, 4, 1,
            3, 2, 4,
            // Bottom
            0, 5, 7,
            0, 7, 6,
            7, 5, 8,
            7, 8, 6,
            // Left
            0, 11, 9,
            0, 10, 11,
            11, 12, 9,
            11, 10, 12,
            // Right
            0, 13, 15,
            0, 15, 14,
            15, 13, 16,
            15, 16, 14};
    
	
	
	public JOGL(){
		System.out.println( "LWJGL version: " + Sys.getVersion() ); 
	}
	
	public void init(){
		// register method for error messages
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		
		// Initialize glfw
		if (glfwInit() != GL_TRUE)
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
		
		// Set up Window Hints
		glfwDefaultWindowHints(); 					// Load the default Window Hints
		//glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); 	// The window will not be resizable
		//glfwWindowHint(GLFW_VISIBLE, GL_FALSE);		// Window will not be visible initially
		//glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);	// Ask for OpenGL 4.2 - lower the major/minor versions if context creation fails.
        //glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        
		
        // Get the size of the screen then calculate the centre.
		//ByteBuffer vidmode = glfwGetVideoMode( glfwGetPrimaryMonitor() );
		//int windowHorizOffset = (GLFWvidmode.width(vidmode)  - mWindowWidth)  / 2;
        //int windowVertOffset  = (GLFWvidmode.height(vidmode) - mWindowHeight) / 2;
        
        // code for windowed window
        mWindowId = glfwCreateWindow(mWindowWidth, mWindowHeight, mWindowTitle, NULL, NULL);
        
        // Check the window was successfully created before we try to use it.
        if (mWindowId == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        // set the windows position on the screen
        //glfwSetWindowPos(mWindowId, windowHorizOffset, windowVertOffset);
        
        // Make the window visible
        glfwShowWindow(mWindowId);
        
		glfwMakeContextCurrent(mWindowId);			// Make the glfw context current
		GLContext.createFromCurrent();				// Generate an OpenGL context from the current glfw context
		
		// set the Clear colour
		glClearColor(mClearColorRed, mClearColorGreen, mClearColorBlue, mClearColorAlpha);
		
		// Create a new Shader Program
		shaderProgram = new ShaderProgram();
		
		// Load the shader files, compile them and link them. 
		shaderProgram.initFromFiles(vertShaderFilename, fragShaderFilename);
		
		// Arrays in Java are not guaranteed to store the data in a single continuous lump of data.
		// Therefore we need to store our data in a structure that stores the data all together. 
		// a FloatBuffer allows us when using allocate direct to store the data in a sequence.  
		// FloatBuffer to hold our mix of vertex location and colour data
		mVertexFloatBuffer = Utils.createFloatBuffer(NUM_VERTICES * COMPONENT_COUNT);
		mVertexFloatBuffer.put(mTriangleData);
		mVertexFloatBuffer.flip();
		
		// Generate a new Vertex Array Object. We dont add anything yet
		mTriangleVaoId = glGenVertexArrays(); 
		glBindVertexArray(mTriangleVaoId);
			
			// Generate an id for the VBO and bind to it
			mTriangleVboId = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, mTriangleVboId);
			// Place the triangle data into the VBO...
			glBufferData(GL_ARRAY_BUFFER, mVertexFloatBuffer, GL_STATIC_DRAW);
				
				
				
				int loc = glGetAttribLocation(shaderProgram.getProgramId(), "vertexLocation");
				// ...and specify the data format.
				glVertexAttribPointer(loc, 
		                               VERTEX_COMPONENTS, // Number of normal components per vertex
		                               GL_FLOAT, // Data type
		                               false, // Normalised?
		                               COMPONENT_COUNT * Float.BYTES, // Stride - step by 24 bytes (i.e. 6 floats)
		                               0 * Float.BYTES); // Offset - vertex location data starts at the beginning
				glEnableVertexAttribArray(loc); // "vertexLocation" attribute index - 0
				
				loc = glGetAttribLocation(shaderProgram.getProgramId(), "vertexColour");
				
				glVertexAttribPointer(loc, // Vertex location attribute index - 0
										TEMP_COMPONENTS, // Number of normal components per vertex
				                        GL_FLOAT, // Data type
				                        false, // Normalised?
				                        COMPONENT_COUNT * Float.BYTES, // Stride - step by 24 bytes (i.e. 6 floats)
				                        VERTEX_COMPONENTS * Float.BYTES); // Offset - vertex location data starts at the beginning
				glEnableVertexAttribArray(loc); // "vertexLocation" attribute index - 0
				
			// Unbind VBO
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
		glBindVertexArray(0);
		
		
		mModelMatrix = new Mat4f(1.0f);
		//...		
		mModelMatrixFB = Utils.createFloatBuffer(16);

		
		mIndexBufferId = glGenBuffers();
		ByteBuffer mIndexByteBuffer;
		mIndexByteBuffer = Utils.createByteBuffer(NUM_INDICES); 
		mIndexByteBuffer.put(indicesData);
		mIndexByteBuffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBufferId);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, mIndexByteBuffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		
		//glfwSetInputMode(mWindowId, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);	

		
	}
	public void mainLoop(){
		// Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
		while ( glfwWindowShouldClose(mWindowId) == GL_FALSE ){
			// Clear the frame buffer
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			float deltaTime = (float)glfwGetTime() / 60;
			
			System.out.println(mModelMatrix.toString());
  
			mModelMatrix.rotateAboutLocalAxisDegs(0.01f, new Vec3f(0,0,(float)Math.sin(glfwGetTime()) * (32 * deltaTime)));
			mModelMatrix.translate(new Vec3f(0,0, ((float)Math.sin(glfwGetTime())) / 2000));
			mModelMatrixFB.put( mModelMatrix.toArray());
			mModelMatrixFB.flip();
			
			/*mvpMatrixFloatBuffer = Utils.createFloatBuffer(16);
			mvpMatrixFloatBuffer.put( mModelMatrix );
			mvpMatrixFloatBuffer.flip();*/
			
			//  Place the MVP in its float buffer and flip it ready for use
    		mvpMatrixFloatBuffer.put( mModelViewProjectionMatrix.toArray() );
    		mvpMatrixFloatBuffer.flip();
    		
    		
			// Make our only Shader program the active Shader
			shaderProgram.use();
			// Provide the projection matrix uniform
			glUniformMatrix4(glGetUniformLocation(shaderProgram.getProgramId(), "modelMatrix"), false, mModelMatrixFB);
			glUniformMatrix4(glGetUniformLocation(shaderProgram.getProgramId(), "projectionMatrix"), false, mvpMatrixFloatBuffer);	
			// Indicate to OpenGL which Vertex Array Object it should use 
			glBindVertexArray(mTriangleVaoId);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBufferId);
			// for subsequent glDrawArrays calls.
			glDrawElements(GL_TRIANGLES, 48, GL_UNSIGNED_BYTE, 0);
			

			
				
			// unbind the VAO 
			glBindVertexArray(0);
			// Unbind the vertex array buffer
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

 
			
			// Tell OpenGL that we have finished with this shader for the moment.
			shaderProgram.disable();
			
			// Swap the color buffers  
			glfwSwapBuffers(mWindowId);
						
			// Poll for window events.
			glfwPollEvents();
		}
	}
	public void cleanUp(){
		// Destroy the window created
		glfwDestroyWindow(mWindowId);

		// Release the error Call back
		errorCallback.release();

		// terminate the GLFW context
		glfwTerminate();
	}
	public static void main(String[] args) {
		
		// Create instance of our Lab
		JOGL jOpenGL = new JOGL();
		
		try
        {        
			// Initialize the lab
			jOpenGL.init();
			
			// Run the main loop
			jOpenGL.mainLoop();
			
			// Clean up after our lab
			jOpenGL.cleanUp();
			
			//*! Memory Clean up
			jOpenGL = null;		
        }
        finally
        {
            // in case there was an exception terminate the GLFW context and release the call back function
            glfwTerminate();
            errorCallback.release();
        }
	}

}
