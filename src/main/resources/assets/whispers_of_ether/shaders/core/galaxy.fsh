#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0; // Noise texture (iChannel0)
uniform sampler2D Sampler1; // Stars texture (iChannel1)

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;

out vec4 fragColor;

// Galaxy constants from Shadertoy
const float RETICULATION = 3.0;
const float NB_ARMS = 5.0;
const float COMPR = 0.1;
const float SPEED = 0.1;
const float GALAXY_R = 0.5;
const float BULB_R = 0.4;
const vec3 GALAXY_COL = vec3(0.9, 0.9, 1.0);
const vec3 BULB_COL = vec3(1.0, 1.0, 1.0);
const float BULB_BLACK_R = 0.25;
const vec3 BULB_BLACK_COL = vec3(0.0, 0.0, 0.0);
const vec3 SKY_COL = 0.5 * vec3(0.1, 0.3, 0.5);

float tex(vec2 uv) {
    float n = texture(Sampler0, uv).r;
    return 1.0 - abs(2.0 * n - 1.0);
}

float noise(vec2 uv) {
    float v = 0.0;
    float a = -SPEED * GameTime * 5.0;
    float co = cos(a);
    float si = sin(a); 
    mat2 M = mat2(co, -si, si, co);
    const int L = 7;
    float s = 1.0;
    for (int i = 0; i < L; i++) {
        uv = M * uv;
        float b = tex(uv * s);
        v += (1.0 / s) * pow(b, RETICULATION); 
        s *= 2.0;
    }
    return v / 2.0;
}

void main() {
    // TEST GAMETIME DIRECT
    float r = sin(GameTime * 2.0) * 0.5 + 0.5;  
    float g = cos(GameTime * 3.0) * 0.5 + 0.5;  
    float b = sin(GameTime * 4.0) * 0.5 + 0.5;  
    fragColor = vec4(r, g, b, 1.0);
}