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
    private float speed = r.nextFloat()*2f+2f;

    @Override
    protected void controlUpdate(float tpf) 
    {
        Obj player = level.getObject(0);
        float dist = monster.distance(player);
        float xx = monster.x;
        float yy = monster.y;
        float zz = monster.z;
        float turn = 0.1f;
        
        if (dist<smellDistance) {
            float bx = player.x - xx;
            float bz = player.z - zz;
            float targetAngle = (FastMath.atan2(bx,bz)+FastMath.PI)/FastMath.TWO_PI*16f;
            int nearest = (int)(targetAngle+0.5f);
            if (nearest>15) nearest -= 16;
            nearest = nearest/2*2;
            System.out.println("Target A = "+nearest);
            monster.turnTo(nearest);
        }

        int dir = monster.getDir()/2;
        System.out.println("mon = "+monster.id+" x = "+xx+" y = "+yy+" z= "+zz+" dir = "+dir);
        // Calculate new position to move forward to
        float xv[] = {0f,-1f,-1f,-1f,0f,1f,1f,1f};
        float zv[] = {-1f,-1f,0,1f,1f,1f,0,-1f};
        float xc[] = {0f,-0.5f,-0.5f,-0.5f,0f,0.5f,0.5f,0.5f};
        float zc[] = {-0.5f,-0.5f,0,0.5f,0.5f,0.5f,0,-0.5f};
//            System.out.println("1 dir = "+dir+" xx="+xx+" yy="+yy+" zz="+zz);
        float nx = xx+xv[dir];
        float nz = zz+zv[dir];
        float cx = xx + xc[dir];
        float cz = zz + zc[dir];        

        if (dist>smellDistance) {
            // Wait for movement
            if (monster.canMoveInto(level.getWorld(), (int)cx, (int)yy, (int)cz)) {
                monster.moveTo(new Vec3i(nx,yy,nz),speed*tpf);
                turn = 0.001f;
            } else {
                turn = 0.1f;
            }
            if (r.nextFloat()<turn) {
                speed = r.nextFloat()*2f+2f;
                if (r.nextFloat()>0.5f) dir+=1; else dir-=1;
                if (dir>7) dir -= 8;
                if (dir<0) dir += 8;
                monster.turnTo(dir*2);
            }
        } else if (dist>attackDistance) {
            if (monster.canMoveInto(level.getWorld(), (int)cx, (int)yy, (int)cz)) {
                monster.moveTo(new Vec3i(nx,yy,nz),speed*tpf);            
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
