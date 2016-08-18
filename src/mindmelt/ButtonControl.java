package mindmelt;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

public class ButtonControl extends AbstractControl
{
    private float speed = 1f;
    private BlockButton button;
    
    @Override
    protected void controlUpdate(float tpf) 
    {
        float S = SubRender.SC;
        if (button==null || button.status == button.on || button.status == button.off) 
            return;
        
        Node hinge = (Node)getSpatial();
        if (button.status==button.opening)
        {
            hinge.move(0,0,tpf/speed);
            if (hinge.getLocalTranslation().getZ()>=-S/2f+S/16f)
            {
                button.status = button.off;
                hinge.setLocalTranslation(0, 0, -S/2f+S/16f);
            }
        }
        else if (button.status==button.closing)
        {
            hinge.move(0,0,-tpf/speed);
            if (hinge.getLocalTranslation().getZ()<=-S/2f-S/16f)
            {
                hinge.setLocalTranslation(0, 0, -S/2f-S/16f);
                button.status = button.opening;
            }
        }
    } 
    
    public void setButton(BlockButton button)
    {
        this.button = button;
    }

    public BlockButton getButton()
    {
        return button;
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
