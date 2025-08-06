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

#define Pi 3.1415927

// Base noise function using texture
float tex(vec2 uv) {
    float n = texture(Sampler0, uv).r;
    return 1.0 - abs(2.0 * n - 1.0);
}

// Perlin turbulent noise + rotation  
float noise(vec2 uv) {
    float v = 0.0;
    float a = -SPEED * GameTime * 5.0; // Utiliser GameTime directement
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
    // Convert texture coords to galaxy space
    vec2 uv = (texCoord0 - 0.5) * 2.0; // Convert 0-1 to -1 to 1
    
    vec3 col;
    
    // Spiral stretching with distance
    float rho = length(uv);
    float ang = atan(uv.y, uv.x);
    float shear = 2.0 * log(rho + 0.01);
    float c = cos(shear);
    float s = sin(shear);
    mat2 R = mat2(c, -s, s, c);

    // Galaxy profile
    float r;
    r = rho / GALAXY_R; 
    float dens = exp(-r * r);
    r = rho / BULB_R;   
    float bulb = exp(-r * r);
    r = rho / BULB_BLACK_R; 
    float bulb_black = exp(-r * r);
    float phase = NB_ARMS * (ang - shear);
    
    // Arms = spirals compression
    ang = ang - COMPR * cos(phase) + SPEED * GameTime * 5.0; // Animation des spirales
    uv = rho * vec2(cos(ang), sin(ang));
    float spires = 1.0 + NB_ARMS * COMPR * sin(phase);
    dens *= 0.7 * spires;   
    
    // Gas texture
    float gaz = noise(0.09 * 1.2 * R * uv);
    float gaz_trsp = pow((1.0 - gaz * dens), 2.0);

    // Stars using second texture
    float ratio = 0.8;
    float stars1 = texture(Sampler1, ratio * uv + 0.5).r;
    float stars2 = texture(Sampler0, ratio * uv + 0.5).r;
    float stars = pow(1.0 - (1.0 - stars1) * (1.0 - stars2), 5.0);
    
    // Mix all components
    col = mix(SKY_COL,
              gaz_trsp * (1.7 * GALAXY_COL) + 1.2 * stars, 
              dens);
    col = mix(col, 2.0 * BULB_COL, 1.2 * bulb);
    col = mix(col, 1.2 * BULB_BLACK_COL, 2.0 * bulb_black);
    
    // TEST: Utiliser GameTime comme dans le shader END_PORTAL
    float timeEffect = sin(GameTime * 3.0) * 0.3 + 0.7; // GameTime devrait maintenant marcher !
    col *= timeEffect;
    
    vec4 color = vec4(col, 1.0);
    // Désactiver les effets de couleur vertex/lighting pour garder les vraies couleurs galaxie
    color *= ColorModulator; // Garder seulement le modulateur global
    // Désactiver overlay et lightmap pour les vraies couleurs galaxie
    // color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    // color *= lightMapColor;
    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}