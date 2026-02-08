#version 150

in vec3 Position;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 texCoord0;

void main() {
    vec3 pos = Position * 0.9995;
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);
    texCoord0 = UV0;
}
