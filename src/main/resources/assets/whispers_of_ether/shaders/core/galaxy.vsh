#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec2 UV0;

out vec2 texCoord0;
out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec3 vDir;
out vec3 vPos;

void main() {
    // Slightly push vertices inward to reduce seam visibility when zoomed
    vec3 pos = Position * 0.9995;
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);
    texCoord0 = UV0;
    vDir = normalize(Position);
    vPos = Position;
    sphericalVertexDistance = fog_spherical_distance(Position);
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
}