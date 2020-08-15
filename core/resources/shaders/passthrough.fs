#version 330

in vec2 v_texCoord0;
in vec4 v_color;

out vec4 fragColor;

uniform sampler2D texture0;

void main(){
	vec4 sample = texture2D(texture0, v_texCoord0.st);
	fragColor = sample * v_color;
}