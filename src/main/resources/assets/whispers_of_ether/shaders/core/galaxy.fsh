#version 150

#moj_import <minecraft:globals.glsl>

// Textures: Sampler0 = noise, Sampler1 = stars
uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

// From vertex shader
in vec2 texCoord0;
in vec3 vDir;
in vec3 vPos;

out vec4 fragColor;

// Port minimal du shader Shadertoy "Galaxy" (Fabrice NEYRET) vers core shader MC
// Sampler0 = bruit (noise.png), Sampler1 = étoiles (stars.png)

const float RETICULATION = 3.0;
const float NB_ARMS      = 5.0;
const float COMPR        = 0.1;
const float SPEED        = 0.1;
const float GALAXY_R     = 1.0/2.0;
const float BULB_R       = 1.0/2.5;
const float BULB_BLACK_R = 1.0/4.0;
const vec3  GALAXY_COL   = vec3(0.9, 0.9, 1.0);
const vec3  BULB_COL     = vec3(1.0);
const vec3  BULB_BLACK_COL = vec3(0.0);
const vec3  SKY_COL      = 0.5 * vec3(0.1, 0.3, 0.5);

// Repeat-aware sampling preserving derivatives to avoid seam on wrap
vec4 sampleRepeatGrad(in sampler2D tex, vec2 uv, float eps) {
    vec2 st = uv;
    vec2 dx = dFdx(st);
    vec2 dy = dFdy(st);
    st = fract(st);
    st = st * (1.0 - 2.0 * eps) + eps;
    return textureGrad(tex, st, dx, dy);
}

float baseTex(vec2 uv) {
    float n = sampleRepeatGrad(Sampler0, uv, 0.002).r;
    // MODE 3 ("wires"): 1 - |2n-1|
    return 1.0 - abs(2.0*n - 1.0);
}

float noiseTurb(vec2 uv, float t) {
    float v = 0.0;
    float a = -SPEED * t;
    float co = cos(a), si = sin(a);
    mat2 M = mat2(co, -si, si, co);
    const int L = 7;
    float s = 1.0;
    for (int i = 0; i < L; i++) {
        uv = M * uv;
        float b = baseTex(uv * s);
        v += (1.0 / s) * pow(b, RETICULATION);
        s *= 2.0;
    }
    return v * 0.5;
}

void main() {
    // GameTime ∈ [0,1) sur une journée (~1200s). Pas de fract -> pas de bouclage court.
    float iTime = GameTime * 1200.0;

    // Coords indépendantes de la caméra: projection planaire à partir de la position locale
    // Utilise la direction normalisée dans le plan XY (aucun wrap → aucune couture, pas d'étirement).
    vec3 n = normalize(vPos);
    vec2 uv = n.xy * 1.8;

    // Profil spiralé
    float rho = max(length(uv), 1e-4);
    float ang = atan(uv.y, uv.x);
    float shear = 2.0 * log(rho);
    float c = cos(shear), s = sin(shear);
    mat2 R = mat2(c, -s, s, c);

    // Densités
    float r;
    r = rho / GALAXY_R;  float dens = exp(-r*r);
    r = rho / BULB_R;    float bulb = exp(-r*r);
    r = rho / BULB_BLACK_R; float bulb_black = exp(-r*r);

    float phase = NB_ARMS * (ang - shear);
    ang = ang - COMPR * cos(phase) + SPEED * iTime;
    vec2 uv2 = rho * vec2(cos(ang), sin(ang));
    float spires = 1.0 + NB_ARMS * COMPR * sin(phase);
    dens *= 0.7 * spires;

    // Gaz turbulent
    float gaz = noiseTurb(0.09 * 1.2 * (R * uv2), iTime);
    float gaz_trsp = pow(1.0 - gaz * dens, 2.0);

    // Étoiles
    float ratio = 0.8; // approximation de iResolution.y / iChannelRes.y
    vec2 tuv = ratio * uv2 + 0.5;
    float stars1 = sampleRepeatGrad(Sampler1, tuv, 0.002).r;
    float stars2 = sampleRepeatGrad(Sampler0, tuv, 0.002).r;
    float stars  = pow(1.0 - (1.0 - stars1) * (1.0 - stars2), 5.0);

    // Mix final
    vec3 col = mix(SKY_COL, gaz_trsp * (1.7 * GALAXY_COL) + 1.2 * stars, dens);
    col = mix(col, 2.0 * BULB_COL, 1.2 * bulb);
    col = mix(col, 1.2 * BULB_BLACK_COL, 2.0 * bulb_black);

    fragColor = vec4(col, 1.0);
}