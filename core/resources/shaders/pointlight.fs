#version 330

in VERTEX_DATA {
	vec2 texcoord;
} data_in;

struct Light{
	vec3 position;
	vec3 color;
	float radius;
};

uniform sampler2D texture0; //Positions
uniform sampler2D normals;
uniform vec3 cameraPos;
uniform float cameraRot;
uniform Light light;

const float CUTOFF = 0.005;

const float METALNESS = 0.40;

out vec4 fragColor;


void main(){
	vec2 position = texture2D(texture0, data_in.texcoord).rg;
	vec3 normal	= texture2D(normals, data_in.texcoord).rgb;

	vec3 fragToLight = vec3(light.position - vec3(position, 0));
	float D = length(fragToLight);

	vec3 N = normalize(normal * 2.0 - 1.0);
	vec3 L = normalize(fragToLight);
	vec3 V = normalize(vec3(0, 0, cameraPos.z));
	vec3 R = -reflect(L, N);
	D = clamp(D, 0, light.radius);

	vec3 diffuse = light.color * max(dot(N, L), 0);
	vec3 specular = light.color * pow(max(0, dot(R, V)), 4);
	//vec3 specular = vec3(0, 0, 0);
	
	//float a = 1.0 / pow((D/light.radius) + 1, 2);
	float a = 1.0 - (D / light.radius);
	a = (a - CUTOFF) / (1 - CUTOFF);
	
	diffuse = diffuse * (1.0 - METALNESS) * a;
	specular = specular * METALNESS * (a);
	

	fragColor = vec4(diffuse + specular, 1);
}
