package mindmelt;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class BlockTypeSteps extends BlockType
{
    BlockTypeSteps(int type)
    {
        super(type);
        cull=Cull.Opaque;
        //mat = 0;
    }
    
    private int getFace(int side,int dir)
    {
        int sides[][] = {
            {Face.PART,Face.PART,Face.FULL,Face.PART,Face.PART,Face.FULL},//ok
            {Face.PART,Face.PART,Face.PART,Face.FULL,Face.PART,Face.FULL},
            {Face.FULL,Face.PART,Face.PART,Face.PART,Face.PART,Face.FULL},
            {Face.PART,Face.FULL,Face.PART,Face.PART,Face.PART,Face.FULL},
        };
        return sides[dir][side];
    }

    protected int getFace(int side, IBlockAccess world,int bx,int by,int bz)
    {
        int dir = world.getBlock(bx,by,bz).getDirection();
        return getFace(side,dir);
    }

    public void render(int bx, int by, int bz, IBlockAccess world, Renderer rend)
    {
        Block thisBlock = world.getBlock(bx,by,bz);
        int dir = thisBlock.getDirection();
        
        for (int d=0;d<6;d++)
        {
            boolean visible = true;
            int fac = d;
            int lookTo = d;
            int x1 = bx+Direction.x[lookTo];
            int y1 = by+Direction.y[lookTo];
            int z1 = bz+Direction.z[lookTo];
            Block thatBlock = world.getBlock(x1,y1,z1);
            if (thatBlock != null) 
            {
                visible = Cull.isFaceVisable(lookTo, world,bx,by,bz,x1,y1,z1);
            }
            if ( visible || d==Face.UP)
            {
                int light = getLight(lookTo);
                int or = Direction.south;
                if (fac==Face.DOWN) or = dir;
                if (fac==Face.UP) or = Direction.top(dir);
                rend.getRenderer(mat).addFace(multiTex[d], fac, bx,by,bz, 16,8,16, light,dir,or,false,false,rend.getCollision(collisionType));
            }
            if ( (visible && d!=Face.DOWN) || d==dir)
            {
                int wid[] = {16,8,16,8};
                int dep[] = {8,16,8,16};
                int off_x[] = {8,4,8,12};
                int off_z[] = {12,8,4,8};
                int light = getLight(lookTo);
                int or = Direction.south;
                if (fac==Face.UP) or = Direction.top(dir);
                rend.getRenderer(mat).addFace(multiTex[d], fac, bx,by,bz, wid[dir],8,dep[dir], off_x[dir],8,off_z[dir], light,dir,or,false,false,rend.getCollision(collisionType));
            }
        }
    }
}