#version 150

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>

in vec3 Position;
in vec2 UV0;

out vec2 texCoord0;
out float sphericalVertexDistance;
out float cylindricalVertexDistance;

void main() {
    // Slight shrink to reduce edge bleeding when billboarding
    vec3 pos = Position * 0.9995;
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);
    texCoord0 = UV0;
    sphericalVertexDistance = fog_spherical_distance(Position);
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
}


