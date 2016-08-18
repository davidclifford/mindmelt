package mindmelt;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;

public class BlockButton extends Block
{
    static final byte off = 1;
    static final byte closing = 2;
    static final byte opening = 3;
    static final byte on = 4;

    byte status = BlockButton.off;
    
    protected BlockButton(int id)
    {
        super((byte)id);
    }
    
    protected boolean isOff()
    {
        return status==off;
    }
    
    protected boolean isOn()
    {
        return status==on;
    }
    
    protected void turnOff()
    {
        status=opening;
    }
    
    protected void turnOn()
    {
        status=closing;
    }
    
    protected void setStatus(byte stat)
    {
        status = stat;
    }
    
    protected byte getStatus()
    {
        return status;
    }
    
    protected String getName()
    {
        return "button";
    }

    protected Node shape(int bx, int by, int bz, IBlockAccess world, Renderer r)
    {
        float S = SubRender.SC;
        BlockType blockType = BlockType.getBlockType(id);
        if (blockType == null) 
            return null;
        
        double ang[] = {FastMath.PI,FastMath.HALF_PI,0f,-FastMath.HALF_PI};
        
        Renderer rend = new Renderer(getName()+":"+bx+":"+by+":"+bz, r.getBulletAppState());
        blockType.render(0, 0, 0, world, rend);
        Node button = rend.render(true);
        
        button.setLocalTranslation(0,S/2f,0);
        
        Node hinge = new Node("hinge");
        Node frame = new Node("frame");
        hinge.attachChild(button);
        hinge.setLocalTranslation(0,0,-S/2f+S/16f);
        frame.attachChild(hinge);
        float a2[] = {0.0f,(float)ang[direction],0.0f};
        Quaternion q2 = new Quaternion(a2);
        frame.setLocalRotation(q2);
        frame.setLocalTranslation(bx*S+S/2f, by*S+S/2f, bz*S+S/2f);
        ButtonControl buttonControl = new ButtonControl();
        buttonControl.setButton(this);
        hinge.addControl(buttonControl);
        
        return frame;
    }
       
    protected String getSpecificInfo()
    {
        return ","+status;
    }   
}
