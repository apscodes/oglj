/**
 * @apscodes
 */
//*! Static Imports
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;

//*! Standard Imports
import org.lwjgl.opengl.GLContext;
import static org.lwjgl.system.MemoryUtil.*;
import org.lwjgl.glfw.GLFW;
import java.io.*;
import java.nio.FloatBuffer;
import java.math.*;

/**
 * @author 30123809
 * 
 */
public class Source 
{
	/**
	 * @param args
	 */
	static long mWindowId = 0;
	static boolean gameLoopRunning = false;
	static int mVaoId;
	static int mVboId;
	static int mShaderProgramId;
	static final int NUM_VERTICES = 3;
	static final int VERTEX_COMPONENTS = 4;
	static final int COMPONENT_COUNT = 8;
	static final int COLOUR_COMPONENTS = 4;
	static final int windowWidth = 400;
	static final int windowHeight = 400;
	static FloatBuffer mProjectionMatrixBuffer; 
	
	public static void main(String[] args) 
	{
		//ApproxPI();
		
		gameLoopRunning = (SetupGLFW()) ? true : false;
	
		while(gameLoopRunning == true && glfwWindowShouldClose(mWindowId) == GL_FALSE )
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); 
			
			Draw();
			
			glfwSwapBuffers(mWindowId); 
			
			glfwPollEvents(); 
		}
		
		ShutdownGLFW();		
	}
	
	private static void Draw()
	{
		glUseProgram(mShaderProgramId); // GL20
		
		glBindVertexArray(mVaoId); // GL30
		
		glDrawArrays(GL_TRIANGLES, 0, 3); // GL11 
	}
	

	private static void ShutdownGLFW() 
	{
		glfwDestroyWindow(mWindowId); 
		
		glfwTerminate(); 	
	}


	private static boolean SetupGLFW() 
	{
		glfwInit();
		
		//GLFW.glfwSetErrorCallback(null);			
		//GLFW.GLFWErrorCallback(null);
		
		glfwDefaultWindowHints();
		
		mWindowId = glfwCreateWindow(windowWidth, windowHeight, "JOpenGL Window", NULL, NULL);
		
		glfwMakeContextCurrent(mWindowId);
		
		GLContext.createFromCurrent();
		
		glClearColor(0.25f, 0.25f, 0.25f, 0.0f);
	
		glViewport(0,0,windowWidth, windowHeight);
		
		//PVM - projection view matrix
		
		//*! Othographic
		Mat4f mProjectionMatrix = Mat4f.createOrthographicProjectionMatrix(0.0f, windowWidth, 0.0f, windowHeight, -1.0f, 1.0f);
		//*! Perspective
		//Mat4f mProjectionMatrix = Mat4f.createPerspectiveProjectionMatrix(90, 16/9, -1.0f, 1.0f);
		
		mProjectionMatrixBuffer = Utils.createFloatBuffer(16);
		
		mProjectionMatrixBuffer.put(mProjectionMatrix.toArray());
		
		mProjectionMatrixBuffer.flip();
		
		Shader_Setup();
		
		return true;
	}
		
	private static void Shader_Setup()
	{
		
		final String newLine = System.lineSeparator();
		String vertexShaderSource = "#version 430 core" 			+ newLine +
		"layout (location = 0) in vec4 vertexLocation;"				+ newLine +
		"layout (location = 1) in vec4 vertexColour;" 				+ newLine +
		"uniform mat4 projectionMatrix;" 							+ newLine +
		"out vec4 fragmentColour;" 									+ newLine +
		"void main(){" 												+ newLine +
		" 	fragmentColour = vertexColour;"							+ newLine +
		"	gl_Position = projectionMatrix * vertexLocation;" 		+ newLine +
		"}";
		
		String fragmentShaderSource = "#version 430 core"	+ newLine + 
		"in vec4 fragmentColour;" 							+ newLine +
		"out vec4 vOutputColour;" 							+ newLine +
		"void main(void)" 									+ newLine +
		"{"													+ newLine +
			"vOutputColour = fragmentColour;"				+ newLine +
		"}";
		

		
		int mVertexShaderId = glCreateShader(GL_VERTEX_SHADER); // GL20
		if (mVertexShaderId == 0) { // if 0 it failed
			throw new RuntimeException("Failed to create vertex shader."); 
		}
		
		glShaderSource(mVertexShaderId, vertexShaderSource);//link source
		glCompileShader(mVertexShaderId); // compile shader
		vertexShaderSource = null; // clear memory

		int mFragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER); // GL20
		if (mFragmentShaderId == 0) { // if 0 it failed
			throw new RuntimeException("Failed to create fragment shader."); 
		}
		
		glShaderSource(mFragmentShaderId, fragmentShaderSource);//link source
		glCompileShader(mFragmentShaderId); // compile shader
		vertexShaderSource = null; // clear memory
		
		mShaderProgramId = glCreateProgram(); // GL20

		if (mShaderProgramId == 0) { // if 0 it failed
			throw new RuntimeException("Failed to create shader program."); 
		}
		
		glAttachShader(mShaderProgramId, mVertexShaderId); // GL20
		glAttachShader(mShaderProgramId, mFragmentShaderId); // GL20
		
		glLinkProgram(mShaderProgramId); // GL20
		
		if (glGetProgrami(mShaderProgramId, GL_LINK_STATUS) == GL_FALSE) { // GL20
			throw new RuntimeException( "Could not link shader program:" 	+ glGetProgramInfoLog(mShaderProgramId, 1000) );
		}
		
		glDetachShader(mShaderProgramId, mVertexShaderId); // GL20
		glDetachShader(mShaderProgramId, mFragmentShaderId);
		glDeleteShader(mVertexShaderId);
		glDeleteShader(mFragmentShaderId);
		
		glValidateProgram(mShaderProgramId); // GL20
		if (glGetProgrami(mShaderProgramId, GL_VALIDATE_STATUS) == GL_FALSE) 
		{// GL20
			throw new RuntimeException( "Could not validate shader program:" 			+ glGetProgramInfoLog(mShaderProgramId, 1000) );
		}
		
		mVaoId = glGenVertexArrays(); // GL30
		glBindVertexArray(mVaoId); // GL30
		
		VBO_Setup();
			
		glBindVertexArray(0); // GL30
		
	}

	private static void VBO_Setup()
	{
		mVboId = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, mVaoId);
		
		float[] mTriangleData = {0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f,
								-0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f,
								 0.0f, 0.5f, 0.0f, 1.0f,  0.0f, 0.0f, 1.0f, 1.0f}; // Replace with object data
		
		FloatBuffer mVertexFloatBuffer;
		
		mVertexFloatBuffer = Utils.createFloatBuffer(NUM_VERTICES * COMPONENT_COUNT);
	
		mVertexFloatBuffer.put(mTriangleData);
		
		mVertexFloatBuffer.flip();
		
		glBufferData(GL_ARRAY_BUFFER, mVertexFloatBuffer,  GL_STATIC_DRAW);
		
		//*! Vertex Positions
		int location = glGetAttribLocation(mShaderProgramId, "vertexLocation");	
		glVertexAttribPointer(location, VERTEX_COMPONENTS, GL_FLOAT, false, COMPONENT_COUNT * Float.BYTES, 0);
		glEnableVertexAttribArray(location);
		
		//*! Vertex Colours
		location = glGetAttribLocation(mShaderProgramId, "vertexColour");	
		glVertexAttribPointer(location, COLOUR_COMPONENTS, GL_FLOAT, false, COMPONENT_COUNT * Float.BYTES, VERTEX_COMPONENTS * Float.BYTES);
		glEnableVertexAttribArray(location);
	
		//*! Projection Matrix
		location = glGetUniformLocation(mShaderProgramId, "projectionMatrix");
		glUniformMatrix4(location, false, mProjectionMatrixBuffer);
		
	
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	
	private static void ApproxPI()
	{
		float pi_est = 1;
	
		
		for(int index = 0; index <= 100000; index++)
		{
			int den = index * 2 + 3; 	// increasing denominator
			
			if(index % 2 == 0)
			{
				pi_est = pi_est - (1.0f / den);	// even
			}
			else
			{
				pi_est = pi_est + (1.0f / den);	// odd
			}
			
			//*! Iterative guess
			System.out.println(pi_est * 4);
		}
		
		//*! Final guess
		System.out.println(pi_est * 4);
	}
}
