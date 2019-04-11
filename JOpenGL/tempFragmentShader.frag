#version 430 core

smooth in vec4 fragColour;
out vec4 vOutputColour; 

void main()
{
	vOutputColour = fragColour;
}