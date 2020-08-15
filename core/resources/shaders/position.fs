#version 330

in VERTEX_DATA {
  vec2 position;
} data_in;

out vec4 frag_color;

void main(){
  frag_color = vec4(data_in.position.x, data_in.position.y, 0, 1);
}
