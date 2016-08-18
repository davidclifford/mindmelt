package mindmelt;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Node;

public class BlockTrapDoor extends BlockDoor
{
   
    protected BlockTrapDoor(int id)
    {
        super((byte)id);
    }
    
    protected String getName()
    {
        return "trap";
    }
    
    @Override
    protected Node shape(int bx, int by, int bz, IBlockAccess world, Renderer r)
    {
        float S = SubRender.SC;
        
        BlockType blockType = BlockType.getBlockType(id);
        if (blockType == null) 
            return null;
        
        double ang[] = {FastMath.PI,FastMath.HALF_PI,0f,-FastMath.HALF_PI};

        Renderer rend = new Renderer(getName()+":"+bx+":"+by+":"+bz, r.getBulletAppState());
        blockType.render(0, 0, 0, world, rend);
        Node door = rend.render(true);
        door.setLocalTranslation(0,S/2f-S/16f,-S/2f+S/16f);
        
        Node hinge = new Node("hinge");
        Node frame = new Node("frame");
        hinge.attachChild(door);
        if(isOpen())
            hinge.rotate(-FastMath.HALF_PI,0,0);
        hinge.setLocalTranslation(0, S/2f-S/16f, S/2f-S/16f);
        frame.attachChild(hinge);
        float a2[] = {0.0f,(float)ang[direction],0.0f};
        Quaternion q2 = new Quaternion(a2);
        frame.setLocalRotation(q2);
        frame.setLocalTranslation(bx*S+S/2f, by*S+S/2f, bz*S+S/2f);
        TrapDoorControl doorControl = new TrapDoorControl();
        doorControl.setDoor(this);
        hinge.addControl(doorControl);
        
        return frame;
    }
}
