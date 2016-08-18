package mindmelt;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class ObjType {
    protected int type = 1;
    protected int subtype = 1;
    protected int w,h,d;
    protected String name="nothing";
    protected Node shape=null;
    protected int texture[] = {0,0,0,0,0,0};
    protected boolean mirror[] = {false,false,false,false,false,false};
    static ObjType types[] = new ObjType[256];
    
    public static final int OBJ = 1;
    public static final int PERSON = 2;
    public static final int MONSTER = 3;
    public static final int PLAYER = 4;
    
    public static final ObjType player = new ObjType(0,PLAYER,"player").setTexture(0).setSize(1,1,1);
    public static final ObjType box = new ObjType(1,OBJ,"box").setTexture(0).setSize(1,1,1);
    public static final ObjType key = new ObjType(2,OBJ,"key").setTexture6(1,2,1,2,1,1).setMirror().setSize(8,1,4);
    public static final ObjType scroll = new ObjType(3,OBJ,"scroll").setTexture6(3,4,3,4,3,3).setSize(16,4,4);
    public static final ObjType gremlin = new ObjTypeMonster(4,MONSTER,"gremlin").setTexture6(0,1,1,1,1,1).setSize(12, 16, 12);
    public static final ObjType blob = new ObjTypeMonster(5,MONSTER,"blob").setTexture6(3,4,4,4,4,4).setSize(12, 12, 12);
    public static final ObjType troll = new ObjTypeMonster(6,MONSTER,"troll").setTexture6(6,7,7,7,7,7).setSize(8, 16, 8);
    public static final ObjType trader = new ObjTypePerson(7,PERSON,"trader");
    public static final ObjType stick = new ObjType(8,OBJ,"stick").setTexture(5).setSize(2,16,2);

    ObjType(int type, int subtype, String name)
    {
        this.type = type;
        this.subtype = subtype;
        this.w = 16;
        this.h = 16;
        this.d = 16;
        this.name = name;
        if (types[type] != null)
        {
            System.out.println("Object type already registered");
            System.exit(1);
        }
        types[type] = this;
    }
    
    public static ObjType getObjType(int type)
    {
        if (types[type] != null) 
            return types[type];
        return null;
    }   
    
    public static int getObjSubtype(int type)
    {
        if (types[type] != null) 
            return types[type].subtype;
        return 0;
    }
    
    public String getName() {
        return name;
    }    

    public int getType() {
        return type;
    }    

    public int getSubtype() {
        return subtype;
    }    

    public ObjType setName(String name)
    {
        this.name = name;
        return this;
    }    
    
    public ObjType setSize(int width, int height, int depth)
    {
        w = width;
        h = height;
        d = depth;
        return this;
    }
    
    protected ObjType setTexture(int tex) {
        for (int i=0; i<6; i++){
            texture[i] = tex;
        }
        return this;
    }
    
    public ObjType setTexture6(int tex1, int tex2, int tex3, int tex4, int tex5, int tex6) {
        texture[0] = tex1;
        texture[1] = tex2;
        texture[2] = tex3;
        texture[3] = tex4;
        texture[4] = tex5;
        texture[5] = tex6;
        return this;
    }
    
    public ObjType setMirror() {
        return setMirror(true,false,false,false,false,true);        
    }
    
    public ObjType setMirror(boolean m1,boolean m2,boolean m3,boolean m4,boolean m5,boolean m6) {
        mirror[0] = m1;
        mirror[1] = m2;
        mirror[2] = m3;
        mirror[3] = m4;
        mirror[4] = m5;
        mirror[5] = m6;
        return this;
    }
    
    protected Node render(int id)
    {
        int face[] = {Face.NORTH,Face.EAST,Face.SOUTH,Face.WEST,Face.UP,Face.DOWN};
        int light[] = {200,160,200,160,240,120,};
        float SC = SubRender.SC;
        String idString = "obj:"+id;
        Node node = new Node(idString);

        SubRender rend = new SubRender(TextureMats.OBJECT,idString);
        for (int f=0;f<6;f++) {
            rend.addFace(texture[f], face[f], 0, 0, 0, w, h, d, light[f], Direction.south, Direction.south, mirror[f], true, null);
        }
        Geometry geom = rend.render();
        node.attachChild(geom);
               
        shape = node;
       
        return node;
    }
}


