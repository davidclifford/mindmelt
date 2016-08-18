package mindmelt;

import com.jme3.bullet.BulletAppState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

public class SubRender 
{
    static final float SC = 4f; //MASTER SCALE
    static final float S = SC/2f;
    static final Vector3f SCALE = new Vector3f(S,S,S);
    static final Vector3f ZERO = new Vector3f(0,0,0);
    static final float WI = 1f/16f;

    ArrayList<Vector3f> vertices; // points
    ArrayList<Vector2f> texCoord; // tex cords
    ArrayList<Float> light;
    ArrayList<Integer> indexes; // indexes
    int index = 0;
    int material = TextureMats.OPAQUE;
    String name;
    BulletAppState bulletAppState;
    Collisions coll = null;

    SubRender(int mat, String name)
    {
        vertices = new ArrayList<Vector3f>();
        texCoord = new ArrayList<Vector2f>();
        light = new ArrayList<Float>();
        indexes = new ArrayList<Integer>();
        material = mat;
        index = 0;
        this.name = name;
    }
    
    public void addFace(int tex, Vector3f v1, Vector3f v2, Vector3f v3, Vector3f v4, int x, int y, int z, int wid, int hi, int dep, int light, int textureDir, boolean mirror, boolean object, Collisions collision)
    {
        float w = wid/32f;
        float h = hi/32f;
        float d = dep/32f;
        Vector3f off = object ? ZERO : SCALE;
        coll = collision;
        Vector3f c1 = new Vector3f(-w+v1.x,-h+v1.y,-d+v1.z).mult(SC).add(off);
        Vector3f c2 = new Vector3f(-w+v2.x,-h+v2.y,-d+v2.z).mult(SC).add(off);
        Vector3f c3 = new Vector3f(-w+v3.x,-h+v3.y,-d+v3.z).mult(SC).add(off);
        Vector3f c4 = new Vector3f(-w+v4.x,-h+v4.y,-d+v4.z).mult(SC).add(off);
        addQuad(c1,c2,c3,c4,wid,hi,tex,light,textureDir,mirror);
    }   
    
    public void addFace(int tex, int face, int x, int y, int z, int wid, int hi, int dep, int light, int blockDir, int textureDir, boolean mirror, boolean object, Collisions collision)
    { 
        addFace(tex, face, x, y, z, wid, hi, dep, 8, 0, 8, light, blockDir, textureDir, mirror, object, collision);
    }  
    
    public void addObjectFace(int tex, int face, int wid, int hi, int dep, int light, boolean mirror)
    { 
        addFace(tex, face, 0,0,0, wid, hi, dep, 8, 0, 8, light, Direction.south,  Direction.south, mirror, true, null);
    }
    
    public void addFace(int tex, int face, int x, int y, int z, int wid, int hi, int dep, int left, int up, int back, int light, int blockDir, int textureDir, boolean mirror, boolean object, Collisions collision)
    { 
        float w = wid/32f;
        float h = hi/32f;
        float d = dep/32f;
        float yy = y-(16-hi)/32f;
        Vector3f off = (object ? ZERO : SCALE).add(new Vector3f(left/16f-0.5f,up/16f,back/16f-0.5f).mult(SC));
        coll = collision;
        
        Vector3f[] v = {
            new Vector3f(-w+x,-h+yy,-d+z).mult(SC).add(off),
            new Vector3f(w+x,-h+yy,-d+z).mult(SC).add(off),
            new Vector3f(-w+x,h+yy,-d+z).mult(SC).add(off),
            new Vector3f(w+x,h+yy,-d+z).mult(SC).add(off),
            new Vector3f(-w+x,-h+yy,d+z).mult(SC).add(off),
            new Vector3f(w+x,-h+yy,d+z).mult(SC).add(off),
            new Vector3f(-w+x,h+yy,d+z).mult(SC).add(off),
            new Vector3f(w+x,h+yy,d+z).mult(SC).add(off),
            
            new Vector3f(-w+x,-h+yy,0+z).mult(SC).add(off),//8
            new Vector3f(w+x,-h+yy,0+z).mult(SC).add(off),//9
            new Vector3f(-w+x,h+yy,0+z).mult(SC).add(off),//10
            new Vector3f(w+x,h+yy,0+z).mult(SC).add(off),//11
            new Vector3f(-w+x,0+yy,-d+z).mult(SC).add(off),//12
            new Vector3f(w+x,0+yy,-d+z).mult(SC).add(off),//13
            new Vector3f(-w+x,0+yy,d+z).mult(SC).add(off),//14
            new Vector3f(w+x,0+yy,d+z).mult(SC).add(off),//15
            new Vector3f(0+x,-h+yy,-d+z).mult(SC).add(off),//16
            new Vector3f(0+x,h+yy,-d+z).mult(SC).add(off),//17
            new Vector3f(0+x,-h+yy,d+z).mult(SC).add(off),//18
            new Vector3f(0+x,h+yy,d+z).mult(SC).add(off),//19
        };

        Vector3f[][] faces = {
            {v[1],v[0],v[3],v[2]}, //back
            {v[5],v[1],v[7],v[3]}, //right
            {v[4],v[5],v[6],v[7]}, //front
            {v[0],v[4],v[2],v[6]}, //left
            {v[6],v[7],v[2],v[3]}, //top
            {v[5],v[4],v[1],v[0]}, //bottom

            {v[4],v[1],v[6],v[3]}, //x1
            {v[0],v[5],v[2],v[7]}, //x2

            {v[4],v[5],v[2],v[3]}, //slope 1
            {v[5],v[1],v[6],v[2]}, //slope 2
            {v[1],v[0],v[7],v[6]}, //slope 3
            {v[0],v[4],v[3],v[7]}, //slope 4

            {v[8],v[9],v[10],v[11]}, //mid front
            {v[12],v[13],v[14],v[15]}, //mid top
            {v[16],v[18],v[17],v[19]}, //mid right
 
            {v[0],v[0],v[0],v[0]}, //mid frnt top
            {v[0],v[0],v[0],v[0]}, // mid frnt bot
            {v[0],v[0],v[0],v[0]}, // mid frnt rgt
            {v[0],v[0],v[0],v[0]}, // mid frnt lft
            {v[0],v[0],v[0],v[0]}, // mid top frnt
            {v[0],v[0],v[0],v[0]}, // mid top back
            {v[0],v[0],v[0],v[0]}, // mid top rght
            {v[0],v[0],v[0],v[0]}, // mid top left
            {v[0],v[0],v[0],v[0]}, // mid right frnt
            {v[0],v[0],v[0],v[0]}, // mid right back
            {v[0],v[0],v[0],v[0]}, // mid right top
            {v[0],v[0],v[0],v[0]}, // mid right bot
        };
        Vector3f[][] tri = {
            {v[1],v[5],v[3]}, //tri right 1
            {v[0],v[1],v[2]}, //tri right 2
            {v[4],v[0],v[6]}, //tri right 3
            {v[5],v[4],v[7]}, //tri right 4
            {v[0],v[4],v[2]}, //tri left 1
            {v[4],v[5],v[6]}, //tri left 2
            {v[5],v[1],v[7]}, //tri left 3
            {v[1],v[0],v[3]}, //tri left 4
            {v[1],v[5],v[2]}, //corner right 1
            {v[0],v[1],v[6]}, //corner right 2
            {v[4],v[0],v[7]}, //corner right 3
            {v[5],v[4],v[3]}, //corner right 4
            {v[4],v[5],v[2]}, //corner left 1
            {v[5],v[1],v[6]}, //corner left 2
            {v[1],v[0],v[7]}, //corner left 3
            {v[0],v[4],v[3]}, //corner left 4            
        };
        
        if (face>=Face.FIRST && face<=Face.QUAD)
        {
            Vector3f v1 = faces[face][0];
            Vector3f v2 = faces[face][1];
            Vector3f v3 = faces[face][2];
            Vector3f v4 = faces[face][3];

            if (face==Face.UP || face==Face.DOWN ) 
            {
                hi = dep;
                if(blockDir == Direction.east || blockDir == Direction.west)
                {
                    hi = wid;
                    wid = dep;
                }
            }
            if (face==Face.WEST || face==Face.EAST ) 
                wid = dep;
            addQuad(v1,v2,v3,v4,wid,hi,tex,light,textureDir,mirror);
        }
        else if (face>=Face.TRIANG_E_S && face<=Face.TRIANG_S_W)
        {
            Vector3f v1 = tri[face-Face.TRI][0];
            Vector3f v2 = tri[face-Face.TRI][1];
            Vector3f v3 = tri[face-Face.TRI][2];
            addTri2(v1,v2,v3,wid,hi,tex,light,textureDir,mirror);
        }
        else if (face>=Face.TRIANG_W_S && face<=Face.TRIANG_N_W)
        {
            Vector3f v1 = tri[face-Face.TRI][0];
            Vector3f v2 = tri[face-Face.TRI][1];
            Vector3f v3 = tri[face-Face.TRI][2];
            addTri1(v1,v2,v3,wid,hi,tex,light,textureDir,mirror);
        }
        else if (face>=Face.CORNER_E_S && face<=Face.CORNER_S_W)
        {
            Vector3f v1 = tri[face-Face.TRI][0];
            Vector3f v2 = tri[face-Face.TRI][1];
            Vector3f v3 = tri[face-Face.TRI][2];
            addTri2(v1,v2,v3,wid,hi,tex,light,textureDir,mirror);
        }
        else if (face>=Face.CORNER_S_E && face<=Face.CORNER_W_S)
        {
            Vector3f v1 = tri[face-Face.TRI][0];
            Vector3f v2 = tri[face-Face.TRI][1];
            Vector3f v3 = tri[face-Face.TRI][2];
            addTri1(v1,v2,v3,wid,hi,tex,light,textureDir,mirror);
        }
    }
    
    public void addQuad(Vector3f v1,Vector3f v2,Vector3f v3,Vector3f v4, int wid, int height, int texture, int light, int textureDir, boolean mirror)
    {
        addTexCoords(texture,wid,height,textureDir,mirror);
        addLight(light,4);
        addIndexes();
        addVertex(v1);
        addVertex(v2);
        addVertex(v3);
        addVertex(v4);
    }
    
    public void addTri1(Vector3f v1,Vector3f v2,Vector3f v3,int wid, int height, int texture, int light, int textureDir, boolean mirror)
    {
        addTexCoordsTri(texture,wid,height,textureDir,mirror);
        addLight(light,3);
        addIndexesTri1();
        addVertex(v1);
        addVertex(v2);
        addVertex(v3);
    }
    
    public void addTri2(Vector3f v1,Vector3f v2,Vector3f v3,int wid, int height, int texture, int light, int textureDir, boolean mirror)
    {
        addTexCoordsTri(texture,wid,height,textureDir,mirror);
        addLight(light,3);
        addIndexesTri2();
        addVertex(v1);
        addVertex(v2);
        addVertex(v3);
    }
    
    private void addTexCoords(int tex, float w, float h,int textureDir,boolean mirror)
    {
        float tu = WI*(tex&15);
        float tv = 1f - WI*(tex/16+1);
        float x = w/256;
        float y = h/256f;
        if (mirror)
        {
            if (textureDir==Direction.east)//
            {
                texCoord.add(new Vector2f(tu,tv));
                texCoord.add(new Vector2f(tu,tv+y));
                texCoord.add(new Vector2f(tu+x,tv));
                texCoord.add(new Vector2f(tu+x,tv+y));
            }
            else if (textureDir==Direction.north)
            {
                texCoord.add(new Vector2f(tu,tv+y));
                texCoord.add(new Vector2f(tu+x,tv+y));
                texCoord.add(new Vector2f(tu,tv));
                texCoord.add(new Vector2f(tu+x,tv));
            }
            else if (textureDir==Direction.west)//
            {
                texCoord.add(new Vector2f(tu+x,tv+y));
                texCoord.add(new Vector2f(tu+x,tv));
                texCoord.add(new Vector2f(tu,tv+y));
                texCoord.add(new Vector2f(tu,tv));
            } 
            else // Default South
            {
                texCoord.add(new Vector2f(tu+x,tv));
                texCoord.add(new Vector2f(tu,tv));
                texCoord.add(new Vector2f(tu+x,tv+y));
                texCoord.add(new Vector2f(tu,tv+y));
            }            
        }
        else
        {
            if (textureDir==Direction.east)//
            {
                texCoord.add(new Vector2f(tu+x,tv));
                texCoord.add(new Vector2f(tu+x,tv+y));
                texCoord.add(new Vector2f(tu,tv));
                texCoord.add(new Vector2f(tu,tv+y));
            }
            else if (textureDir==Direction.north)
            {
                texCoord.add(new Vector2f(tu+x,tv+y));
                texCoord.add(new Vector2f(tu,tv+y));
                texCoord.add(new Vector2f(tu+x,tv));
                texCoord.add(new Vector2f(tu,tv));
            }
            else if (textureDir==Direction.west)//
            {
                texCoord.add(new Vector2f(tu,tv+y));
                texCoord.add(new Vector2f(tu,tv));
                texCoord.add(new Vector2f(tu+x,tv+y));
                texCoord.add(new Vector2f(tu+x,tv));
            } 
            else // Default south
            {
                texCoord.add(new Vector2f(tu,tv));
                texCoord.add(new Vector2f(tu+x,tv));
                texCoord.add(new Vector2f(tu,tv+y));
                texCoord.add(new Vector2f(tu+x,tv+y));
            }
        }
    }    
    
    private void addTexCoordsTri(int tex, float w, float h,int textureDir,boolean mirror)
    {
        float tu = WI*(tex&15);
        float tv = 1f - WI*(tex/16+1);
        float x = w/256;
        float y = h/256f;
        if (mirror)
        {
            if (textureDir==Direction.west)//
            {
                texCoord.add(new Vector2f(tu,tv));
                texCoord.add(new Vector2f(tu,tv+y));
                texCoord.add(new Vector2f(tu+x,tv));
                //texCoord.add(new Vector2f(tu+x,tv+y));
            }
            else if (textureDir==Direction.north)
            {
                texCoord.add(new Vector2f(tu,tv+y));
                texCoord.add(new Vector2f(tu+x,tv+y));
                texCoord.add(new Vector2f(tu,tv));
                //texCoord.add(new Vector2f(tu+x,tv));
            }
            else if (textureDir==Direction.east)//
            {
                texCoord.add(new Vector2f(tu+x,tv+y));
                texCoord.add(new Vector2f(tu+x,tv));
                texCoord.add(new Vector2f(tu,tv+y));
                //texCoord.add(new Vector2f(tu,tv));
            } 
            else // Default South
            {
                texCoord.add(new Vector2f(tu+x,tv));
                texCoord.add(new Vector2f(tu,tv));
                texCoord.add(new Vector2f(tu+x,tv+y));
                //texCoord.add(new Vector2f(tu,tv+y));
            }            
        }
        else
        {
            if (textureDir==Direction.west)//
            {
                texCoord.add(new Vector2f(tu+x,tv));
                texCoord.add(new Vector2f(tu+x,tv+y));
                texCoord.add(new Vector2f(tu,tv));
                //texCoord.add(new Vector2f(tu,tv+y));
            }
            else if (textureDir==Direction.north)
            {
                texCoord.add(new Vector2f(tu+x,tv+y));
                texCoord.add(new Vector2f(tu,tv+y));
                texCoord.add(new Vector2f(tu+x,tv));
                //texCoord.add(new Vector2f(tu,tv));
            }
            else if (textureDir==Direction.east)//
            {
                texCoord.add(new Vector2f(tu,tv+y));
                texCoord.add(new Vector2f(tu,tv));
                texCoord.add(new Vector2f(tu+x,tv+y));
                //texCoord.add(new Vector2f(tu+x,tv));
            } 
            else // Default South
            {
                texCoord.add(new Vector2f(tu,tv));
                texCoord.add(new Vector2f(tu+x,tv));
                texCoord.add(new Vector2f(tu,tv+y));
                //texCoord.add(new Vector2f(tu+x,tv+y));
            }
        }
    }

    private void addLight(int li,int num)
    {
        float b = li/256f;
        for (int i=0;i<num;i++)
        {
            light.add(b);
            light.add(b);
            light.add(b);
            light.add(1.0f);
        }
    }
    
    private void addVertex(Vector3f vert)
    {
        vertices.add(vert);
        if (coll!=null)
            coll.addVert(vert);
    }
    
    private void addIndexes()
    {
        indexes.add(index+2);
        indexes.add(index+0);
        indexes.add(index+1);
        indexes.add(index+1);
        indexes.add(index+3);
        indexes.add(index+2);
        index += 4;
        if (coll!=null)
            coll.addQuadIndex();
    }    
    
    private void addIndexesTri1()
    {
        indexes.add(index+2);
        indexes.add(index+0);
        indexes.add(index+1);
        index += 3;
        if (coll!=null)
            coll.addTri1Index();
    }    
    
    private void addIndexesTri2()
    {
        indexes.add(index+1);
        indexes.add(index+0);
        indexes.add(index+2);
        index += 3;
        if (coll!=null)
            coll.addTri2Index();
    }

    public static int[] convertIntegers(ArrayList<Integer> integers) 
    {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
    
    public static float[] convertFloats(ArrayList<Float> floats) 
    {
        float[] ret = new float[floats.size()];
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = floats.get(i).floatValue();
        }
        return ret;
    }
    
    public Geometry render()
    {
        if (indexes.isEmpty()) return null;
        
        Vector3f[] vec = vertices.toArray(new Vector3f[vertices.size()]);
        Vector2f[] tex = texCoord.toArray(new Vector2f[texCoord.size()]);
        float[] li = convertFloats(light);
        int ind[] = convertIntegers(indexes);
        
        Mesh mesh = new Mesh();
        mesh.setBuffer(Type.Color, 4, BufferUtils.createFloatBuffer(li));
        mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vec));
        mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
        mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(ind));
        mesh.updateBound();
        mesh.updateCounts();
        mesh.setMode(Mesh.Mode.Triangles);
        mesh.setStatic();
        // Creating a geometry, and apply a single color material to it
        Geometry geom = new Geometry(name, mesh);
        geom.setMaterial(TextureMats.getMaterialType(material));
        if (material == TextureMats.TRANS || material == TextureMats.WATERANIM )
            geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        else
            geom.setQueueBucket(RenderQueue.Bucket.Opaque);
        
        if (material == TextureMats.ANIM)
        {
            AnimControl control = new AnimControl();
            control.setNumAnims(4);
            control.setSpeed(8);
            geom.addControl(control);
        }   

        if (material == TextureMats.WATERANIM)
        {
            AnimControl control = new AnimControl();
            control.setNumAnims(4);
            control.setSpeed(2);
            geom.addControl(control);
        }   

        return geom;
    }
}
