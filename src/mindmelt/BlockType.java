package mindmelt;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import jme3tools.optimize.GeometryBatchFactory;

public class BlockType 
{
    public int id = 0;
    public boolean canenter = false;
    public boolean iswater = false;
    public int multiTex[] = {0,0,0,0,0,0};
    public boolean mirror[] = {false,false,false,false,false,false};
    public int cull = Cull.Opaque;
    public int mat = TextureMats.OPAQUE;
    public int width = 16;
    public int height = 16;
    public int depth = 16;
    public boolean object = false; //true if object, false if block
    public int collisionType = Collisions.NORMAL; //Normal-object, Normal, Forcefield, Waterwalk etc...
    
    static BlockType blockType[] = new BlockType[256];
    static String name[] = {"air","grass","wall","wood","rock","stone","glass","water","tree",
                            "ytree","bush","flower","grasswedge","grasscorner","sand","dirt",
                            "longgrass","crops","deepwater","roof","roofwedge","roofcorner",
                            "house","village","trunk","exit","doortop","doorknob","doorlock","gate",
                            "rug","lamp","talllamp","lamptop","plant","torch","forcefield","teleport",
                            "presspad","table","chair","bedhead","bedbottom","picture","fireplace",
                            "bookcase","barrel","bridge","signpost","sign","wardrobe","drawers",
                            "secretdoor","secretpit","secrettele","secretff","message","button",
                            "lever","steps","secretbutton","secretpp","castle","trapdoor","fence",
                            "castletop","entrancetop","floor-wall-ceil","ceiling","ladder","portal",
                            "wallslime","wallmanacle","wallhole","wallgrate","wallroot","woodblood",
                            "woodooze","woodknot"};
    static int maxBlock = 78;

    public static final BlockType air = (BlockType)(new BlockTypeAir(0)).setCollType(Collisions.NONE).enter();
    public static final BlockType grass = (BlockType)(new BlockType(1)).setTex(1);
    public static final BlockType wall = (BlockType)(new BlockType(2)).setTex(2);
    public static final BlockType wood = (BlockType)(new BlockType(3)).setTex(3);
    public static final BlockType rock = (BlockType)(new BlockType(4)).setTex(4);
    public static final BlockType stone = (BlockType)(new BlockType(5)).setTex(5);
    public static final BlockType glass = (BlockTypeTransparent)(new BlockTypeTransparent(6)).setTex(6);
    public static final BlockType water = (BlockTypeWater)(new BlockTypeWater(7)).setTex(68).setCollType(Collisions.WATER).enter().water();
    public static final BlockType tree = (BlockType)(new BlockTypeX(8)).setTex(8).setCollType(Collisions.NONE).enter();
    public static final BlockType ytree = (BlockType)(new BlockTypeX(9)).setTex(9).setCollType(Collisions.NONE).enter();
    public static final BlockType bush = (BlockType)(new BlockTypeX(10)).setTex(10).setThick(8).setHeight(8).setCollType(Collisions.NONE).enter();
    public static final BlockType flower = (BlockType)(new BlockTypeX(11)).setTex(11).setThick(4).setHeight(4).setCollType(Collisions.NONE).enter();
    public static final BlockType grasswedge = (BlockTypeWedge)(new BlockTypeWedge(12)).setTex(1);
    public static final BlockType grasscorner = (BlockTypeCorner)(new BlockTypeCorner(13)).setTex(1);
    public static final BlockType sand = (BlockType)(new BlockType(14)).setTex(12);
    public static final BlockType dirt = (BlockType)(new BlockType(15)).setTex(13);
    public static final BlockType longgrass = (BlockType)(new BlockTypeX(16)).setTex(14).setCollType(Collisions.NONE).enter();
    public static final BlockType crops = (BlockType)(new BlockTypeX(17)).setTex(15).setCollType(Collisions.NONE).enter();
    public static final BlockType deepwater = (BlockType)(new BlockTypeWater(18)).setTex(72).setCollType(Collisions.NONE).enter().water();
    public static final BlockType roof = (BlockType)(new BlockType(19)).setTex(17);
    public static final BlockType roofwedge = (BlockTypeWedge)(new BlockTypeWedge(20)).setTex(17);//17
    public static final BlockType roofcorner = (BlockTypeCorner)(new BlockTypeCorner(21)).setTex(17);
    public static final BlockType house = (BlockType)(new BlockType(22)).setTex(18);
    public static final BlockType village = (BlockType)(new BlockType(23)).setTex6(19,19,19,19,0,0);
    public static final BlockType trunk = (BlockType)(new BlockTypeX(24)).setTex(7).setCollType(Collisions.NONE).enter();
    public static final BlockType exit = (BlockType)(new BlockType(25)).setTex(20);
    public static final BlockType doortop = (BlockType)(new BlockTypeDoor(26)).setTex(21).setDepth(2).setMirror6(true,false,false,false,false,false).setObj().enter();
    public static final BlockType doorknob = (BlockType)(new BlockTypeDoor(27)).setTex(22).setDepth(2).setMirror6(true,false,false,false,false,false).setObj().enter();
    public static final BlockType doorlock = (BlockType)(new BlockTypeDoor(28)).setTex(23).setDepth(2).setMirror6(true,false,false,false,false,false).setObj().enter();
    public static final BlockType gate = (BlockType)(new BlockTypeTransparent(29)).setTex(24).setDepth(2).setMirror6(true,false,false,false,false,false).setObj().enter();
    public static final BlockType rug = (BlockType)(new BlockType(30)).setTex6(3,3,3,3,25,3);
    public static final BlockType lamp = (BlockType)(new BlockTypeX(31)).setTex(26).setHeight(13).setThick(10).setCollType(Collisions.NONE).enter();
    public static final BlockType talllamp = (BlockType)(new BlockType(32)).setTex(27).setThick(2);
    public static final BlockType lamptop = (BlockType)(new BlockTypeX(33)).setTex(28).setThick(10).setHeight(7);
    public static final BlockType plant = (BlockType)(new BlockTypeX(34)).setTex(29).setThick(8).setHeight(13);
    public static final BlockType torch = (BlockType)(new BlockType(35)).setTex6(30,30,30,30,31,31).setThick(2).setHeight(10).setCollType(Collisions.NONE).enter();
    public static final BlockType forcefield = (BlockType)(new BlockTypeTransparent(36)).setTex(60).setCollType(Collisions.FF).setMaterial(TextureMats.ANIM);
    public static final BlockType teleport = (BlockType)(new BlockTypeTransparent(37)).setTex(64).setCollType(Collisions.NONE).setMaterial(TextureMats.ANIM);
    public static final BlockType presspad = (BlockType)(new BlockTypeTransparent(38)).setTex(3).setThick(12).setHeight(1);
    public static final BlockType table = (BlockType)(new BlockTypeTransparent(39)).setTex6(35,35,35,35,34,0).setHeight(8);
    public static final BlockType chair = (BlockType)(new BlockTypeChair(40)).setTex6(36,36,37,36,38,0).setThick(8).setHeight(8).setCollType(Collisions.NONE).enter();
    public static final BlockType bedhead = (BlockType)(new BlockType(41)).setTex6(47,45,46,45,39,0).setHeight(8).setMirror6(false,true,false,false,false,false);
    public static final BlockType bedbottom = (BlockType)(new BlockType(42)).setTex6(47,47,47,47,47,0).setHeight(8);
    public static final BlockType picture = (BlockType)(new BlockType(43)).setTex6(40,40,40,40,2,2);
    public static final BlockType fireplace = (BlockType)(new BlockType(44)).setTex(84).setMaterial(TextureMats.ANIM);
    public static final BlockType bookcase = (BlockType)(new BlockType(45)).setTex6(42,42,42,42,2,2);
    public static final BlockType barrel = (BlockType)(new BlockType(46)).setTex6(43,43,43,43,44,44).setThick(8).setHeight(8);
    public static final BlockType bridge = (BlockType)(new BlockTypeBridge(47)).setTex(3).setHeight(2);
    public static final BlockType signpost = (BlockType)(new BlockType(48)).setTex(3).setThick(2);
    public static final BlockType sign = (BlockType)(new BlockType(49)).setTex(3).setDepth(2).setHeight(8);
    public static final BlockType wardrobe = (BlockType)(new BlockType(50)).setTex6(48,49,49,49,49,49);
    public static final BlockType drawers = (BlockType)(new BlockType(51)).setTex6(50,49,49,49,49,49).setHeight(8);
    public static final BlockType secretdoor = (BlockType)(new BlockTypeSecret(52)).setTex(56).setCollType(Collisions.NONE).enter();
    public static final BlockType secretpit = (BlockType)(new BlockTypeSecret(53)).setTex(3).setCollType(Collisions.NONE);
    public static final BlockType secrettele = (BlockType)(new BlockTypeAir(54)).setCollType(Collisions.NONE).enter();
    public static final BlockType secretff = (BlockType)(new BlockTypeAir(55)).setCollType(Collisions.FF);
    public static final BlockType message = (BlockType)(new BlockType(56)).setTex(51);
    public static final BlockType button = (BlockType)(new BlockType(57)).setTex(52).setThick(2).setHeight(2).setObj().setCollType(Collisions.NONE).enter();
    public static final BlockType lever = (BlockType)(new BlockType(58)).setTex(3).setThick(2).setHeight(8).setObj().setCollType(Collisions.NONE).enter();
    public static final BlockType steps = (BlockType)(new BlockTypeSteps(59)).setTex(2);
    public static final BlockType secretbutton = (BlockType)(new BlockType(60)).setTex(55);
    public static final BlockType secretpp = (BlockType)(new BlockType(61)).setTex(3).enter();
    public static final BlockType castle = (BlockType)(new BlockType(62)).setTex6(57,57,57,57,0,0);
    public static final BlockType trapdoor = (BlockType)(new BlockTypeDoor(63)).setTex(53).setHeight(2).setObj();
    public static final BlockType fence = (BlockType)(new BlockType(64)).setTex6(59,59,59,59,0,0).setDepth(2);
    public static final BlockType castletop = (BlockType)(new BlockTypeDoor(65)).setTex6(54,54,54,54,0,0);
    public static final BlockType entrancetop = (BlockType)(new BlockType(66)).setTex6(76,76,76,76,77,77);
    public static final BlockType fwc = (BlockType)(new BlockType(67)).setTex6(2,2,2,2,3,78);
    public static final BlockType ceiling = (BlockType)(new BlockType(68)).setTex(78);
    public static final BlockType ladder = (BlockType)(new BlockType(69)).setTex6(79,79,79,79,0,0).setDepth(2).setCollType(Collisions.NONE);
    public static final BlockType portal = (BlockType)(new BlockType(70)).setTex(80).setMaterial(TextureMats.ANIM);
    public static final BlockType wallslime = (BlockType)(new BlockType(71)).setTex(88);
    public static final BlockType wallmanacle = (BlockType)(new BlockType(72)).setTex(89);
    public static final BlockType wallhole = (BlockType)(new BlockType(73)).setTex(90);
    public static final BlockType wallgrate = (BlockType)(new BlockType(74)).setTex(91);
    public static final BlockType wallroot = (BlockType)(new BlockType(75)).setTex(92);
    public static final BlockType woodblood = (BlockType)(new BlockType(76)).setTex(93);
    public static final BlockType woodooze = (BlockType)(new BlockType(77)).setTex(94);
    public static final BlockType woodknot = (BlockType)(new BlockType(78)).setTex(95);
    
    public static BlockType getBlockType(int id)
    {
        if (blockType[id] != null) return blockType[id];
        return null;
    }
    
    protected BlockType(int id)
    {
        if (blockType[id] != null)
        {
            System.out.println("Block already registered");
            System.exit(1);
        }
        blockType[id] = this;
        this.id = id;
    }
    
    protected BlockType setMaterial(int mat)
    {
        this.mat = mat;
        return this;
    }
    
    protected BlockType setTex(int tex)
    {
        int multi[] = {tex,tex,tex,tex,tex,tex};
        this.multiTex = multi;
        return this;
    }
    
    protected BlockType setTex6(int t1,int t2,int t3,int t4,int t5,int t6)
    {
        int multi[] = {t1,t2,t3,t4,t5,t6};
        this.multiTex = multi;
        return this;
    }
    protected BlockType setMirror(boolean mir)
    {
        boolean multi[] = {mir,mir,mir,mir,mir,mir};
        this.mirror = multi;
        return this;
    }
    
    protected BlockType setMirror6(boolean m1,boolean m2,boolean m3,boolean m4,boolean m5,boolean m6)
    {
        boolean multi[] = {m1,m2,m3,m4,m5,m6};
        this.mirror = multi;
        return this;
    }
    
    protected BlockType setCull(int cull)
    {
        this.cull = cull;
        return this;
    }
    
    protected BlockType setWidth(int w)
    {
        this.width = w;
        return this;
    }
    
    protected BlockType setThick(int w)
    {
        this.width = w;
        this.depth = w;
        return this;
    }
    
    protected BlockType setDepth(int d)
    {
        this.depth = d;
        return this;
    }
    
    protected BlockType setHeight(int h)
    {
        this.height = h;
        return this;
    }
    
    protected BlockType setObj()
    {
        object = true;
        return this;
    }   
    
    protected BlockType setCollType(int collType)
    {
        collisionType = collType;
        return this;
    }
    
    protected BlockType enter()
    {
        canenter = true;
        return this;
    }    
    
    protected BlockType water()
    {
        iswater = true;
        return this;
    }
    
    protected boolean canEnter()
    {
        return canenter;
    } 
    
    protected boolean isWater()
    {
        return iswater;
    }
    
    protected int getWidth(int dir)
    {
        if (dir == Direction.east || dir == Direction.west)
            return depth;
        return width;
    }
    
    protected int getDepth(int dir)
    {
        if (dir == Direction.east || dir == Direction.west)
            return width;
        return depth;
    }
    
    protected int getHeight(int dir)
    {
        return height;
    }
    
    protected int getFace(int side, IBlockAccess world,int bx,int by,int bz)
    {
        int dir = world.getBlock(bx, by, bz).getDirection();
        
        if (isFullHeight(dir) && isFullWidth(dir) && isFullDepth(dir)) return Face.FULL;
        
        if (side==Face.UP && !isFullHeight(dir)) return Face.NONE;
        if (side==Face.UP && !isFullWidth(dir)) return Face.PART;
        if (side==Face.UP && !isFullDepth(dir)) return Face.PART;
        if (side==Face.WEST && !isFullWidth(dir)) return Face.NONE;
        if (side==Face.WEST && !isFullHeight(dir)) return Face.PART;
        if (side==Face.WEST && !isFullDepth(dir)) return Face.PART;
        if (side==Face.EAST && !isFullWidth(dir)) return Face.NONE;
        if (side==Face.EAST && !isFullHeight(dir)) return Face.PART;
        if (side==Face.EAST && !isFullDepth(dir)) return Face.PART;
        if (side==Face.SOUTH && !isFullDepth(dir)) return Face.NONE;
        if (side==Face.SOUTH && !isFullHeight(dir)) return Face.PART;
        if (side==Face.SOUTH && !isFullWidth(dir)) return Face.PART;
        if (side==Face.NORTH && !isFullDepth(dir)) return Face.NONE;
        if (side==Face.NORTH && !isFullHeight(dir)) return Face.PART;
        if (side==Face.NORTH && !isFullWidth(dir)) return Face.PART;
        if (side==Face.DOWN && !isFullWidth(dir)) return Face.PART;
        if (side==Face.DOWN && !isFullDepth(dir)) return Face.PART;
        return Face.FULL;
    }
    
    protected boolean isObject()
    {
        return object;
    }
    
    protected boolean isFullHeight(int dir)
    {
        return getHeight(dir)==16;
    }

    protected boolean isFullWidth(int dir)
    {
        return getWidth(dir)==16;
    }

    protected boolean isFullDepth(int dir)
    {
        return getDepth(dir)==16;
    }

    protected boolean isFullBlock()
    {
        return (height==16) && (width==16) && (depth ==16);
    }

    public void render(int bx, int by, int bz, IBlockAccess world, Renderer rend)
    {
        int face[] = {Face.NORTH,Face.EAST,Face.SOUTH,Face.WEST,Face.UP,Face.DOWN};
        Block thisBlock = world.getBlock(bx,by,bz);
        int dir = thisBlock.getDirection();
        int width = getWidth(dir);
        int depth = getDepth(dir);
        int height = getHeight(dir);
      
        for (int d=0;d<6;d++)
        {
            boolean visible = true;
            int lookTo = d;
            int fac = Direction.rotateOppositeBy(d,dir);
            int x1 = bx+Direction.x[lookTo];
            int y1 = by+Direction.y[lookTo];
            int z1 = bz+Direction.z[lookTo];
            Block thatBlock = world.getBlock(x1,y1,z1);
            if (thatBlock != null) 
            {
                visible = Cull.isFaceVisable(lookTo, world,bx,by,bz,x1,y1,z1);
            }
            if ( visible)
            {
                int light = getLight(lookTo);
                int f = face[d];
                int or = Direction.south;
                if (f==Face.DOWN) or = dir;
                if (f==Face.UP) or = Direction.top(dir);
                int tex = multiTex[fac];
                if (tex>0)
                    rend.getRenderer(mat).addFace(tex, f, bx,by,bz, width,height,depth, light,dir,or,mirror[fac],object,rend.getCollision(collisionType));
            }
        }
    }

    protected int getLight(int dir)
    {
        int l[] = {200,160,200,160,240,120,};
        int light = 220;
        if (dir>=0 && dir<6)
            light = l[dir];
        return light;
    }

}
