package mindmelt;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;

public class BlockLever extends Block
{
    static final byte on = 1;
    static final byte opening = 2;
    static final byte closing = 3;
    static final byte off = 4;

    byte status = BlockLever.off;
    
    protected BlockLever(int id)
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
        return "lever";
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

        Node lever = rend.render(true);
        lever.setLocalTranslation(0, S/2f,0);
        
        Node hinge = new Node("hinge");
        
        hinge.attachChild(lever);
        
        if(isOff())
            hinge.rotate(FastMath.QUARTER_PI,0,0);
        else
            hinge.rotate(FastMath.QUARTER_PI+FastMath.HALF_PI,0,0);
        
        Node frame = new Node("frame");
        hinge.setLocalTranslation(0, 0, -S/2f);
        frame.attachChild(hinge);
        float a2[] = {0.0f,(float)ang[direction],0.0f};
        Quaternion q2 = new Quaternion(a2);
        frame.setLocalRotation(q2);
        frame.setLocalTranslation(bx*S+S/2f, by*S+S/2f, bz*S+S/2f);
        
        LeverControl leverControl = new LeverControl();
        leverControl.setLever(this);
        hinge.addControl(leverControl);
        
        return frame;
    }
        
    protected String getSpecificInfo()
    {
        return ","+status;
    }
}
