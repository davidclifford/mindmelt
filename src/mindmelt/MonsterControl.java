package mindmelt;

import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.util.Random;

public class MonsterControl extends AbstractControl
{
    private ObjMonster monster;
    private LevelMap level;
    private static int smellDistance = 10;
    private static int attackDistance = 2;
    private Random r = new Random();
    private float speed = r.nextFloat()*3f+3f;
    private int dir = 0;

    @Override
    protected void controlUpdate(float tpf) 
    {
        Obj player = level.getObject(0);
        float dist = monster.distance(player);
        int xx = (int)monster.x;
        int yy = (int)monster.y;
        int zz = (int)monster.z;
        if (dist>smellDistance) {
            // Wait for movement
            // Try to move forward
            int xv[] = {0,-1,-1,-1,0,1,1,1};
            int zv[] = {-1,-1,0,1,1,1,0,-1};
            System.out.println("1 dir = "+dir+" xx="+xx+" yy="+yy+" zz="+zz);
            xx = xx+xv[dir];
            zz = zz+zv[dir];
            System.out.println("2 dir = "+dir+" xx="+xx+" yy="+yy+" zz="+zz);
            if (monster.canMoveInto(level.getWorld(), xx, yy, zz)) {
                if (monster.moveTo(new Vec3i(xx,yy,zz),speed*tpf))
                        return;
            }
            if (r.nextFloat()<0.1f) {
                if (r.nextFloat()>0.5f) dir++; else dir--;
                if (dir>7) dir -= 8;
                if (dir<0) dir += 8;
                monster.turnTo(dir*2);
            }
        }
    }
        
    private void old_controlUpdate(float tpf) {          
        Obj player = level.getObject(0);
        float dist = monster.distance(player);
        if (dist>smellDistance) {
            if(r.nextFloat()<0.01f) {
                monster.rotateBy(FastMath.PI*(r.nextFloat()-0.5f));
            } else {
                if (!monster.moveForward(level.getWorld(),speed*tpf)){
                    monster.rotateBy(FastMath.PI*(r.nextFloat()-0.5f));
                } 
            }
            return;
        }
        if (monster.turnTowards(player,speed*tpf))
            return;
        if (dist>=attackDistance) {
            monster.moveForward(level.getWorld(),speed*tpf);
        }
    } 
     
    public void setMonster(ObjMonster monster)
    {
        this.monster = monster;
    }

    public ObjMonster getMonster()
    {
        return monster;
    }
    
    public void setLevel(LevelMap level)
    {
        this.level = level;
    }
    
    public LevelMap getLevel()
    {
        return level;
    }

    private float getAngle(Node hinge)
    {
        Quaternion q = hinge.getLocalRotation();
        float a[] = q.toAngles(null);
        return a[0];
    }
    
    private Quaternion getQ(float angle)
    {
        float angs[] = {angle,0,0,};
        Quaternion q = new Quaternion(angs);
        return q;
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) 
    {
        return;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) 
    {
        MonsterControl control = new MonsterControl();
        spatial.addControl(control);
        return control;
    }
}
