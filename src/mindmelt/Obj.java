package mindmelt;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class Obj {
    protected int id = 1;
    protected int type = 1;
    protected float x,y,z;
    protected float dir = 0f;
    protected float dirTo = 0f;
    protected String name="nothing";
    protected int level=0;
    protected Node shape = null;
    protected float speed = 0f;
    protected float off = 0.5f;

    public final static int STOPPED = 0;
    public final static int TURNING_TO = 1;
    public final static int MOVING_TO = 2;
    protected int mode = STOPPED;

    //public static final Obj box = (ObjType)(new BlockTypeAir(0)).setCollType(Collisions.NONE);

    Obj(int id, int type, String name)
    {
        this.id = id;
        this.type = type;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }    
    
    public int getId() {
        return id;
    }
    
    public int getType() {
        return type;
    }
    
    public int getLevel() {
        return level;
    }
    
    public Obj setLevel(int level)
    {
        this.level = level;
        return this;
    }
    
    public Vec3i getPos()
    {
        return new Vec3i(x,y,z);
    }
    
    public Node getShape()
    {
        return shape;
    }
    
    public int getDir()
    {
        return (int)(dir);
    }
    
    private Obj moveShape()
    {
        float SC = SubRender.SC;       
        float xx = x*SC;
        float yy = y*SC+SC/2f;
        float zz = z*SC;
        if (shape!=null) shape.setLocalTranslation(xx, yy, zz);
        return this;
    }

    public Obj moveTo(float x, float y, float z)
    {
        this.x=x;
        this.y=y;
        this.z=z;
        return moveShape();
    }
    
    public Obj moveTo(Vec3i pos)
    {
        x = pos.x+off;
        y = pos.y;
        z = pos.z+off;
        return moveShape();
    }
           
    public Obj moveTo(Vec3i newPos, float speed)
    {
        float tol = 0.01f;
        float dx = (newPos.x+off-x)*speed;
        float dy = (newPos.y-y)*speed;
        float dz = (newPos.z+off-z)*speed;
        return moveBy(dx,dy,dz);
    }
       
    public float distance(Obj other)
    {
        return FastMath.sqrt((other.x-x)*(other.x-x)+(other.y-y)*(other.y-y)+(other.z-z)*(other.z-z));
    }
    
    public Obj moveBy(float x, float y, float z)
    {
        this.x+=x;
        this.y+=y;
        this.z+=z;
        moveShape();
        return this;
    }
    
    public Obj rotateBy(float a) {
        if (a>16f || a<-16f) return this;
        if (shape!=null) shape.rotate(0,a*FastMath.PI/8f,0);
        dir += a;
        if (dir<0f) dir += 16f;
        if (dir>16f) dir -= 16f;
        System.out.println("rotateBy: dir = "+dir+" a = "+a);
        return this;
    }    
       
    public Obj turnTo(float ang)
    {
        // Ang 0 << 16
        float angs[] = {0,ang*FastMath.PI/8f,0};
        Quaternion q = new Quaternion(angs);
        shape.setLocalRotation(q);
        dir = ang;
        System.out.println("turnTo: dir = "+dir+" ang: "+ang);
        return this;
    }

    public Obj turnTowards(Obj other)
    {
        Vector3f vec = new Vector3f(x-other.x,0,z-other.z).normalize();
        float target = FastMath.atan2(vec.x,vec.z)+FastMath.PI;
        Quaternion q = shape.getWorldRotation();
        float a[] = q.toAngles(null);
        float current = a[1];
        // make target 1 of 8 compass points N,NE,E,SE,S,SW,W or NW
        target = ((int)(target/FastMath.PI*4f))/4f*FastMath.PI;
        System.out.println("Target "+target);
        return turnTo(target);
    }
    
    public boolean turnTowards(Obj other, float speed)
    {
        Vector3f vec = new Vector3f(x-other.x,0,z-other.z).normalize();
        float target = FastMath.atan2(vec.x,vec.z);
        Quaternion q = shape.getWorldRotation();
        float a[] = q.toAngles(null);
        float current = a[1];
        // make target 1 of 8 compass points N,NE,E,SE,S,SW,W or NW
        target = ((int)(target/FastMath.PI*4f))/4f*FastMath.PI;
        if (FastMath.abs(target-current)<0.01f)
            return false;
        
        boolean fwd = false;
        if (target>current) fwd = true;
        if (target-current>FastMath.PI || target-current<-FastMath.PI ) fwd = !fwd;
        
        if (fwd)
            rotateBy(speed);
        else
            rotateBy(-speed);
        return true;
    }
    
    public boolean moveForward(IBlockAccess world, float speed)
    {
        float ang = dir/8f*FastMath.PI;
        float xx = (-FastMath.sin(ang));
        float zz = (-FastMath.cos(ang));
        
        //System.out.println("x "+x+" y "+y+" z "+z+" xx "+xx+" zz "+zz);
        if (canMoveInto(world, (int)(x+xx/2f+0.5f), (int)y, (int)(z+zz/2f+0.5f))) {
            moveBy(xx*speed,0,zz*speed);
            return true;
        }
        return false;
    }
    
    public boolean canMoveInto(IBlockAccess world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        Block block1 = world.getBlock(x, y-1, z);
        
        System.out.println("x "+x+" y "+y+" z "+z+" b "+block.getBlockType()+" b1 "+block1.getBlockType());
        if (!block.canEnter() || block.canEnter() && block1.isWater() )
            return false;
        return (!block1.canEnter());
    }
    
    public Obj update()
    {
        if (mode==TURNING_TO) {
        } else if (mode==MOVING_TO) {
        }
        return this;
    }
    
    public Node render()
    {
        ObjType objType = ObjType.getObjType(type);
        if (objType != null)
            shape = objType.render(id);
        moveShape();
        return shape;
    }
}


