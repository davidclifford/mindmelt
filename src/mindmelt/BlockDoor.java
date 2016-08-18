package mindmelt;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;

public class BlockDoor extends Block
{
    static final byte closed = 1;
    static final byte opening = 2;
    static final byte closing = 3;
    static final byte open = 4;

    byte status = BlockDoor.closed;
    
    protected BlockDoor(int id)
    {
        super((byte)id);
    }
    
    protected boolean isOpen()
    {
        return status==open;
    }
    
    protected boolean isClosed()
    {
        return status==closed;
    }
    
    protected void open()
    {
        status=opening;
    }
    
    protected void close()
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
        return "door";
    }
    
    protected int getUpper()
    {
        return BlockType.doortop.id;
    }    
    
    protected int getLower()
    {
        return BlockType.doorknob.id;
    }
    
    protected Node shape(int bx, int by, int bz, IBlockAccess world, Renderer r)
    {
        float S = SubRender.SC;
        
        BlockType blockType = BlockType.getBlockType(getLower());
        if (blockType == null) 
            return null;

        double ang[] = {FastMath.PI,FastMath.HALF_PI,0f,-FastMath.HALF_PI};

        Renderer rend = new Renderer(getName()+":"+bx+":"+by+":"+bz, r.getBulletAppState());
        blockType.render(0, 0, 0, world, rend);
        blockType = BlockType.getBlockType(getUpper());
        blockType.render(0, 1, 0, world, rend);
        Node door = rend.render(true);
        door.setLocalTranslation(S/2f-S/16f,S/2f,0);
        Node hinge = new Node("hinge");
        hinge.setLocalTranslation(-S/2f+S/16f, 0, S/2f-S/16f);
        Node frame = new Node("frame");
        hinge.attachChild(door);
        if(isOpen())
            hinge.rotate(0,FastMath.HALF_PI,0);
        frame.attachChild(hinge);
        float a2[] = {0.0f,(float)ang[direction],0.0f};
        Quaternion q2 = new Quaternion(a2);
        frame.setLocalRotation(q2);
        frame.setLocalTranslation(bx*S+S/2f, by*S, bz*S+S/2f);
        DoorControl doorControl = new DoorControl();
        doorControl.setDoor(this);
        hinge.addControl(doorControl);
        
        return frame;
    }
    
    protected String getSpecificInfo()
    {
        return ","+status;
    }
    
    
   protected int getLight()
   {
       return 200;
   }
}
