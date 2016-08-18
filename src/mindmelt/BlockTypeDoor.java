package mindmelt;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class BlockTypeDoor extends BlockType
{
    BlockTypeDoor(int type)
    {
        super(type);
        cull=Cull.None;
    }
    
    protected int getFace(int side, IBlockAccess world,int bx,int by,int bz)
    {
        return Face.NONE;
    } 
}