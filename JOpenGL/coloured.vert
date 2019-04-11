#version 430 core

layout (location = 0) in vec4 vertexLocation;
layout (location = 1) in vec4 vertexColour;
uniform mat4 mvpMatrix;

smooth out vec4 fragColour;

void main(){
	fragColour = vertexColour;
	gl_Position = mvpMatrix * vertexLocation;
}