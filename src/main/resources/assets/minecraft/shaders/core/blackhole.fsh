#version 150

#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0; // not bound, kept for future scene sampling

in vec2 texCoord0;
out vec4 fragColor;

// Fake lensing: radial distortion + dark center + accretion ring glow.
float linstep(float a, float b, float x) { return clamp((x - a) / (b - a), 0.0, 1.0); }

void main() {
    // 0..1 quad uv mapped on [-1,1]
    vec2 uv = texCoord0 * 2.0 - 1.0;
    float r = length(uv);

    // circular mask
    float mask = step(r, 1.0);

    // simple gravitational lensing: pull uv inward near the center
    float distort = 0.5 * (1.0 - smoothstep(0.0, 1.0, r));
    vec2 duv = normalize(uv) * distort * 0.25;
    vec2 warped = uv + duv;

    // background color approximation (no scene sample yet): vignette sky
    float bg = 0.2 + 0.5 * (1.0 - linstep(0.0, 1.0, length(warped)));
    vec3 col = vec3(bg * 0.6, bg * 0.7, bg);

    // shadow core
    float core = smoothstep(0.0, 0.35, r);
    col *= core;

    // thin bright ring (photon sphere)
    float ring = 1.0 - smoothstep(0.42, 0.46, abs(r - 0.44));
    col += vec3(1.5, 1.2, 0.8) * ring;

    // outer fade
    float alpha = mask * (1.0 - smoothstep(0.95, 1.0, r));
    fragColor = vec4(col, alpha);
}


