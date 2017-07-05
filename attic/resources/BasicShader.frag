attribute vec2 a_pos;
attribute vec4 a_color;

varying vec4 v_color;

uniform mat3 u_transform;

void main() {
    vec3 pos = u_transform * vec3(a_pos.x, a_pos.y, 1);
    v_color = a_color;

    gl_Position = pos;
}