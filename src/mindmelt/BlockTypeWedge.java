package mindmelt;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class BlockTypeWedge extends BlockType
{
    BlockTypeWedge(int type)
    {
        super(type);
        cull=Cull.Opaque;
        //mat = 0;
    }
    
    private int getFace(int side,int dir)
    {
        int sides[][] = {
            {Face.FULL,Face.TRIANGLE_LEFT,Face.NONE,Face.TRIANGLE_RIGHT,Face.NONE,Face.FULL},
            {Face.TRIANGLE_RIGHT,Face.FULL,Face.TRIANGLE_LEFT,Face.NONE,Face.NONE,Face.FULL},
            {Face.NONE,Face.TRIANGLE_RIGHT,Face.FULL,Face.TRIANGLE_LEFT,Face.NONE,Face.FULL},
            {Face.TRIANGLE_LEFT,Face.NONE,Face.TRIANGLE_RIGHT,Face.FULL,Face.NONE,Face.FULL},
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
        int face[][] = {
            {Face.NORTH,Face.TRIANG_E_S,Face.SLOPE_S,Face.TRIANG_W_S,Face.DOWN},
            {Face.TRIANG_N_W,Face.EAST,Face.TRIANG_S_W,Face.SLOPE_W,Face.DOWN},
            {Face.SLOPE_N,Face.TRIANG_E_N,Face.SOUTH,Face.TRIANG_W_N,Face.DOWN},
            {Face.TRIANG_N_E,Face.SLOPE_E,Face.TRIANG_S_E,Face.WEST,Face.DOWN},
        };
        int dirs[][] = {
            {Direction.north,Direction.east,Direction.none,Direction.west,Direction.down},
            {Direction.north,Direction.east,Direction.south,Direction.none,Direction.down},
            {Direction.none,Direction.east,Direction.south,Direction.west,Direction.down},
            {Direction.north,Direction.none,Direction.south,Direction.west,Direction.down},
        };

        Block thisBlock = world.getBlock(bx,by,bz);
        int dir = thisBlock.getDirection();
        
        for (int d=0;d<5;d++)
        {
            boolean visible = true;
            int fac = face[dir][d];
            int lookTo = dirs[dir][d];
            if (lookTo!=Direction.none)
            {
                int x1 = bx+Direction.x[lookTo];
                int y1 = by+Direction.y[lookTo];
                int z1 = bz+Direction.z[lookTo];
                Block thatBlock = world.getBlock(x1,y1,z1);
                if (thatBlock != null) 
                {
                    visible = Cull.isFaceVisable(lookTo, world,bx,by,bz,x1,y1,z1);
                }
            }
            if ( visible )
            {
                int light = getLight(lookTo);
                int or = Direction.south;
                if (fac==Face.DOWN) or = dir;   
                rend.getRenderer(mat).addFace(multiTex[d], fac, bx,by,bz, 16,16,16, light,dir,or,false,false,rend.getCollision(collisionType));
            }
        }
    }
}