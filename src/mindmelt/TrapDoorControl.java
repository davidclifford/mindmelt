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

public class TrapDoorControl extends AbstractControl
{
    private float speed = 3f;
    private BlockTrapDoor door;
    
    @Override
    protected void controlUpdate(float tpf) 
    {
        if (door==null || door.status == door.closed || door.status == door.open) 
            return;
        
        Node hinge = (Node)getSpatial();
        Node body = (Node)hinge.getChild(0);
        RigidBodyControl rbc = (RigidBodyControl)body.getControl(0);
        rbc.setEnabled(false);
        if (door.status==door.closing)
        {
            hinge.rotate(tpf*speed,0f,0f);
            
            if (getAngle(hinge)>=0f) 
            {
                door.status=door.closed;
                hinge.setLocalRotation(getQ(0));
                rbc.setEnabled(true);
            }
        }
        else if (door.status==door.opening)
        {
            hinge.rotate(-tpf*speed, 0f, 0f);
            if (getAngle(hinge)<=-FastMath.HALF_PI) 
            {
                door.status=door.open;
                hinge.setLocalRotation(getQ(-FastMath.HALF_PI));
            }
        }
    } 
    
    public void setDoor(BlockTrapDoor door)
    {
        this.door = door;
    }

    public BlockTrapDoor getDoor()
    {
        return door;
    }

    private float getAngle(Node hinge)
    {
        Quaternion q = hinge.getLocalRotation();
        float a[] = q.toAngles(null);
        return a[0];
    }
    
    private Quaternion getQ(float angle)
    {
        float angs[] = {angle,0,0};
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
