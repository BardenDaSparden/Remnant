#version 400

in VERTEX_DATA {
	vec2 texcoord;
} data_in;

uniform sampler2D diffuse;
uniform sampler2D light;
uniform sampler2D bloom;

const float GAMMA = 2.2;
const float WHITE_CUTOFF = 2.4;
const vec3 RGB_TO_Y = vec3(0.2126, 0.7152, 0.0722);
const float BLOOM_SCALE = 1;

out vec4 frag_color;

float tonemap(float L){
	float ld = L * (1.0 + (L / WHITE_CUTOFF / WHITE_CUTOFF));
	ld /= L + 1;
	return ld;
}

vec3 decodeGamma(vec3 color){
	vec3 G = vec3(1.0 / GAMMA);
	return color * G;
}

vec3 encodeGamma(vec3 color){
	vec3 G = vec3(GAMMA);
	return color * G;
}

void main(){
	vec3 s_diffuse = texture2D(diffuse, data_in.texcoord).rgb;
	vec3 s_light = texture2D(light, data_in.texcoord).rgb;
	vec3 s_bloom = texture2D(bloom, data_in.texcoord).rgb;
	s_diffuse = decodeGamma(s_diffuse);
	//vec3 linearColor = encodeGamma(s_diffuse * s_light);
	vec3 linearColor = encodeGamma(s_light * s_diffuse + (s_bloom * BLOOM_SCALE));
	float L = dot(RGB_TO_Y, linearColor);
	float nL = tonemap(L);
	float scale = nL / L;
	linearColor *= scale;
	frag_color = vec4(linearColor, 1);
	//frag_color = vec4(linearColor + s_bloom, 1);
}
