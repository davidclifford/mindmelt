package mindmelt;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class BlockTypeX extends BlockType
{
    BlockTypeX(int type)
    {
        super(type);
        cull=Cull.None;
        mat = TextureMats.BOTHSIDES;
        collisionType = Collisions.NONE;
    }

    protected int getFace(int side, IBlockAccess world,int bx,int by,int bz)
    {
        return Face.NONE;
    }
    
    static int faces[] = {Face.X1,Face.X2};
    public void render(int bx, int by, int bz, IBlockAccess world, Renderer rend)
    {
        int light[] = {240,200};
        for (int i=0;i<2;i++)
        {
            rend.getRenderer(mat).addFace(multiTex[i],faces[i],bx,by,bz,width,height,width,light[i],Direction.south,Direction.south, false,false,rend.getCollision(collisionType));
        }
    }    
}