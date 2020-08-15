#version 330

layout (location = 0) in vec2 position;
layout (location = 1) in vec2 texcoord;

out VERTEX_DATA {
	vec2 texcoord;
} data_out;

uniform mat4 projection;
uniform mat4 view;

void main(){
	data_out.texcoord = texcoord;
	gl_Position = projection * view * vec4(position, 1, 1);
}
