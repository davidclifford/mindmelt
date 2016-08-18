package mindmelt;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class DoorControl extends AbstractControl
{
    private float speed = 3f;
    private BlockDoor door;
    private long time=System.currentTimeMillis();
    
    @Override
    protected void controlUpdate(float tpf) 
    {
        if (door==null || door.status == door.closed) 
            return;
        
        if (door.status == door.open)
        {
            if (System.currentTimeMillis()-time>3000L)
               door.status=door.closing;
            else
                return;
        }
        
        Node hinge = (Node)getSpatial();
        Node body = (Node)hinge.getChild(0);
        RigidBodyControl rbc = (RigidBodyControl)body.getControl(0);
        rbc.setEnabled(false);
        if (door.status==door.closing)
        {
            hinge.rotate(0f, -tpf*speed, 0f);
            
            if (getAngle(hinge)<=0f) 
            {
                door.status=door.closed;
                hinge.setLocalRotation(getQ(0));
                rbc.setEnabled(true);
            }
        }
        else if (door.status==door.opening)
        {
            hinge.rotate(0f, tpf*speed, 0f);
            if (getAngle(hinge)>=FastMath.HALF_PI) 
            {
                door.status=door.open;
                hinge.setLocalRotation(getQ(FastMath.HALF_PI));
                time = System.currentTimeMillis();
            }
        }
    } 
    
    public void setDoor(BlockDoor door)
    {
        this.door = door;
    }

    public BlockDoor getDoor()
    {
        return door;
    }

    private float getAngle(Node hinge)
    {
        Quaternion q = hinge.getLocalRotation();
        float a[] = q.toAngles(null);
        return a[1];
    }
    
    private Quaternion getQ(float angle)
    {
        float angs[] = {0,angle,0};
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
        DoorControl control = new DoorControl();
        spatial.addControl(control);
        return control;
    }
}
