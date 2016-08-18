package mindmelt;

import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class Face 
{
    // Shapes to draw
    static final int FIRST = 0;
    static final int NORTH = 0;//
    static final int EAST = 1;
    static final int SOUTH = 2;//
    static final int WEST = 3;
    static final int UP = 4;
    static final int DOWN = 5;
    static final int X1 = 6;
    static final int X2 = 7;
    static final int SLOPE_S = 8; // SOUTH FACING SLOPE
    static final int SLOPE_E = 9; // EAST  FACING SLOPE
    static final int SLOPE_N = 10;// WEST  FACING SLOPE
    static final int SLOPE_W = 11;// NORTH FACING SLOPE
    static final int MIDFRONT = 12;
    static final int MIDTOP = 13;
    static final int MIDRIGHT = 14;
    static final int MIDFRONTTOP = 15;
    static final int MIDFRONTBOT = 16;
    static final int MIDFRONTRGT = 17;
    static final int MIDFRONTLFT = 18;
    static final int MIDTOPFRONT = 19;
    static final int MIDTOPBACK = 20;
    static final int MIDTOPRGT = 21;
    static final int MIDTOPLFT = 22;
    static final int MIDRIGHTFRONT = 23;
    static final int MIDRIGHTBACK = 24;
    static final int MIDRIGHTTOP = 25;
    static final int MIDRIGHTBOT = 26;
    static final int QUAD = 26;
    
    static final int TRI = 27;
    static final int TRIANG_E_S = 27; // ON EAST POINT SOUTH
    static final int TRIANG_N_E = 28;
    static final int TRIANG_W_N = 29;
    static final int TRIANG_S_W = 30; // ON SOUTH POINT WEST
    static final int TRIANG_W_S = 31; // ON WEST POINT SOUTH
    static final int TRIANG_S_E = 32;
    static final int TRIANG_E_N = 33;
    static final int TRIANG_N_W = 34; // ON NORTH POINT WEST
    static final int CORNER_E_S = 35;
    static final int CORNER_E_N = 36;
    static final int CORNER_N_W = 37;
    static final int CORNER_S_W = 38;
    static final int CORNER_S_E = 39;
    static final int CORNER_N_E = 40;
    static final int CORNER_W_N = 41;
    static final int CORNER_W_S = 42;
    
    // Faces  to cull - whether there are full square sides or triangles etc..
    static final int NONE = 0;
    static final int FULL = 1;
    static final int PART = 2;
    static final int TRIANGLE_LEFT = 3; // POINTS LEFT
    static final int TRIANGLE_RIGHT = 4; // POOINTS RIGHT
    
    static final float wi = 1f/16f;
}
