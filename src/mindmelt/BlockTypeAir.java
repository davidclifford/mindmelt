package mindmelt;

import com.jme3.scene.Node;

public class BlockTypeAir extends BlockType
{
    BlockTypeAir(int type)
    {
        super(type);
        cull=Cull.None;
    }
    
    protected int getFace(int side, IBlockAccess world,int bx,int by,int bz)
    {
        return Face.NONE;
    }

    public void render(int bx, int by, int bz, Node node, IBlockAccess world, SubRender rend)
    {
        return;
    }    
}