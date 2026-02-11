#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_aspectRatio;
uniform float u_time;

void main() {
    float cA = texture2D(u_texture, v_texCoords).a;

    float thickness = 0.001;

    // Calculate offset based on our thickness uniform
    // Higher thickness = wider search = fills bigger collision gaps
    vec2 offset = vec2(thickness, thickness * u_aspectRatio);

    // 8-way density sampling
    float density = cA;
    density += texture2D(u_texture, v_texCoords + vec2(-offset.x, -offset.y)).a;
    density += texture2D(u_texture, v_texCoords + vec2(0.0, -offset.y)).a;
    density += texture2D(u_texture, v_texCoords + vec2(offset.x, -offset.y)).a;
    density += texture2D(u_texture, v_texCoords + vec2(-offset.x, 0.0)).a;
    density += texture2D(u_texture, v_texCoords + vec2(offset.x, 0.0)).a;
    density += texture2D(u_texture, v_texCoords + vec2(-offset.x, offset.y)).a;
    density += texture2D(u_texture, v_texCoords + vec2(0.0, offset.y)).a;
    density += texture2D(u_texture, v_texCoords + vec2(offset.x, offset.y)).a;

    float avgDensity = density / 9.0;

    // Thresholds:
    // 0.05: Anything that isn't purely empty space
    // 0.95: Anything that is almost entirely surrounded by other solid pixels
    if (avgDensity > 0.01 && avgDensity < 0.99) {
        gl_FragColor = vec4(1.0, 0.1, 0.1, 0.5); // Border
    }
    else if (avgDensity >= 0.95) {
        // Stripe Pattern
        float stripes = sin((v_texCoords.x + v_texCoords.y + u_time * 0.05) * 200.0);
        vec4 color = vec4(1, 0.1, 0.1, 0.2);
        if (stripes < 0.3) color.rgb += 0.4;
        else color.rgb -= 0.02;
        gl_FragColor = color;
    }
    else {
        discard;
    }
}
