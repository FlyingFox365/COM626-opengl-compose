attribute vec4 aVertex;
uniform mat4 uViewMtx, uProjMtx;

void main (void) {
    gl_Position = uProjMtx * uViewMtx * aVertex;
}