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

public class LeverControl extends AbstractControl
{
    private float speed = 2f;
    private BlockLever lever;

    @Override
    protected void controlUpdate(float tpf) 
    {
        if (lever==null || lever.status == lever.on || lever.status == lever.off) 
            return;
        
        Node hinge = (Node)getSpatial();
        if (lever.status==lever.opening)
        {
            hinge.rotate(-tpf*speed, 0f, 0f);
            
            if (getAngle(hinge)<=FastMath.QUARTER_PI) 
            {
                lever.status=lever.off;
                hinge.setLocalRotation(getQ(FastMath.QUARTER_PI));
            }
        }
        else if (lever.status==lever.closing)
        {
            hinge.rotate(tpf*speed, 0f, 0f);
            if (getAngle(hinge)>=FastMath.QUARTER_PI+FastMath.HALF_PI) 
            {
                lever.status=lever.on;
                hinge.setLocalRotation(getQ(FastMath.QUARTER_PI+FastMath.HALF_PI));
            }
        }
    } 
    
    public void setLever(BlockLever lever)
    {
        this.lever = lever;
    }

    public BlockLever getLever()
    {
        return lever;
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
        LeverControl control = new LeverControl();
        spatial.addControl(control);
        return control;
    }
}
