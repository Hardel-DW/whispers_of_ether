#version 150

#moj_import <minecraft:globals.glsl>

in vec2 texCoord0;
out vec4 fragColor;

float linstep(float a, float b, float x) { return clamp((x - a) / (b - a), 0.0, 1.0); }

float hash12(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 34.345);
    return fract(p.x * p.y);
}

vec3 skyColor(vec2 p, float t) {
    // Vertical gradient sky
    float h = clamp(p.y * 0.5 + 0.5, 0.0, 1.0);
    vec3 top = vec3(0.07, 0.12, 0.20);
    vec3 bot = vec3(0.25, 0.35, 0.55);
    vec3 col = mix(top, bot, h);

    // Procedural star field
    float scale = 22.0;
    vec2 uv = p * scale;
    vec2 id = floor(uv);
    vec2 gv = fract(uv) - 0.5;
    float n = hash12(id);
    vec2 offs = vec2(hash12(id + 11.1), hash12(id + 27.7)) - 0.5;
    float d = length(gv - offs * 0.8);
    float star = smoothstep(0.08, 0.0, d);
    star *= smoothstep(0.08, 0.0, d);
    float tw = 0.5 + 0.5 * sin(6.2831 * (n * 1.7) + t * 2.3);
    star *= 0.6 + 0.4 * tw;
    vec3 sCol = vec3(1.3, 1.25, 1.2) * star * 0.9;
    return col + sCol;
}

void main() {
    // Quad UV in [-1,1]
    vec2 uv = texCoord0 * 2.0 - 1.0;
    float r = length(uv);
    vec2 dir = normalize(uv + 1e-6);

    // Time (seconds approx). GameTime in [0,1) over a full day (~1200s)
    float iTime = GameTime * 1200.0;

    // Circular mask for the card and soft edge fade
    float mask = step(r, 1.0);
    float edgeFade = 1.0 - smoothstep(0.98, 1.0, r);

    // Parameters
    const float EVENT_HORIZON_R = 0.35;
    const float PHOTON_SPHERE_R = 0.44;
    const float PHOTON_WIDTH    = 0.035;
    const float DEFLECT_K       = 0.28;

    // Gravitational lensing: stronger near center, with slight spin modulation
    float deflect = DEFLECT_K / max(r, 0.08);
    float spin = 0.45 * exp(-pow((r - PHOTON_SPHERE_R) / (1.5 * PHOTON_WIDTH), 2.0))
               * sin(0.6 * iTime);
    float cs = cos(spin), sn = sin(spin);
    mat2 rot = mat2(cs, -sn, sn, cs);
    vec2 warped = rot * (uv + dir * deflect);

    // Background: procedural sky with gentle motion
    vec3 bg = skyColor(0.25 * warped + 0.02 * iTime * vec2(0.21, 0.17), iTime);
    float flicker = 0.5 + 0.5 * sin(9.0 * r + 2.5 * iTime);

    // Core darkness
    float core = smoothstep(EVENT_HORIZON_R - 0.015, EVENT_HORIZON_R + 0.015, r);

    // Photon sphere ring
    float ringG = exp(-pow((r - PHOTON_SPHERE_R) / PHOTON_WIDTH, 2.0));
    vec3 ringCol = vec3(1.6, 1.25, 0.85) * ringG * (0.55 + 0.60 * flicker);

    // Accretion disk aligned to the equator (horizon line across uv.y = 0)
    float thickness = 0.085;
    float diskProfile = exp(-pow(uv.y / thickness, 2.0));
    diskProfile *= smoothstep(0.20, 0.95, r); // avoid disk in the very center and near edge

    // "Reflection" across the horizon line: mirror the procedural background vertically
    vec2 warpedMirror = vec2(warped.x, -warped.y);
    vec3 bgMirror = skyColor(0.25 * warpedMirror + 0.02 * iTime * vec2(0.21, 0.17), iTime);
    vec3 diskBase = mix(bg, bgMirror, 0.65);
    vec3 diskCol = diskBase * vec3(1.25, 1.05, 0.85) * (0.65 + 0.55 * flicker) * diskProfile;

    // Base lensed sky contribution (kept subtle)
    vec3 base = bg * 0.35;
    base *= core; // darken by event horizon

    vec3 col = base + ringCol + diskCol;

    // Final alpha
    float alpha = mask * edgeFade;
    fragColor = vec4(col, alpha);
}


