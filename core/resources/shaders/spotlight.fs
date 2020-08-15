#version 330

in VERTEX_DATA {
	vec2 texcoord;
} data_in;

struct Light{
	vec3 position;
	vec3 color;
  vec3 direction;
	float radius;
  float cutoff;
};

uniform sampler2D texture0; //Positions
uniform sampler2D normals;
uniform Light light;

out vec4 fragColor;

void main(){
	vec2 position = texture2D(texture0, data_in.texcoord).rg;
	vec3 normal	= texture2D(normals, data_in.texcoord).rgb;

	vec3 fragToLight = vec3(light.position - vec3(position, 0));
	float D = length(fragToLight);

	vec3 N = normalize(normal * 2.0 - 1.0);
	vec3 L = normalize(fragToLight);

  float angle = dot(light.direction, L);
  if(angle > light.cutoff){
    vec3 diffuse = light.color * max(dot(N, L), 0);
    float a = 1.0 / pow((D/light.radius) + 1, 2);
		diffuse *= a;
    fragColor = vec4(diffuse, 1);
  } else {
    fragColor = vec4(0, 0, 0, 1);
  }
}
// #version 330
//
// in VERTEX_DATA {
// 	vec2 texcoord;
// } data_in;
//
// struct Light{
// 	vec3 position;
// 	vec3 color;
// 	vec3 falloff;
// 	float radius;
// };
//
// uniform sampler2D texture0; //Positions
// uniform sampler2D normals;
// uniform Light light;
//
// out vec4 fragColor;
//
// void main(){
// 	vec2 position = texture2D(texture0, data_in.texcoord).rg;
// 	vec3 normal	= texture2D(normals, data_in.texcoord).rgb;
//
// 	vec3 fragToLight = vec3(light.position - vec3(position, 0));
// 	float D = length(fragToLight);
//
// 	vec3 N = normalize(normal * 2.0 - 1.0);
// 	vec3 L = normalize(fragToLight);
//
// 	vec3 diffuse = light.color * max(dot(N, L), 0);
// 	float a = 1.0 / pow((D/light.radius) + 1, 2);
// 	diffuse *= a;
//
// 	fragColor = vec4(diffuse, 1);
// }
