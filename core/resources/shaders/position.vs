#version 330

layout (location = 0) in vec2 position;

out VERTEX_DATA {
  vec2 position;
} data_out;

uniform mat4 view;
uniform mat4 projection;

void main(){
  data_out.position = position;
  gl_Position = projection * view * vec4(position, 0, 1);
}
