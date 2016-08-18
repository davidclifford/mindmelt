package mindmelt;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class BlockTypeWater extends BlockType
{
    protected boolean fullFace;
    
    BlockTypeWater(int type)
    {
        super(type);
        cull=Cull.Translucent;
        mat = TextureMats.WATERANIM;
        collisionType = Collisions.WATER;
    }
 
    protected int getFace(int side, IBlockAccess world,int bx,int by,int bz)
    {
        int sides[] = {Face.PART,Face.PART,Face.PART,Face.PART,Face.NONE,Face.FULL};
        int f = Face.FULL;
        if (getHeight(world,bx,by,bz)!=16)
            f = sides[side];
        return f;
    }
    
    protected int getHeight(IBlockAccess world,int bx,int by,int bz)
    {
        // Full size if block above not air (or null) 
        int height = 8;
        fullFace = false;
        Block blockAbove = world.getBlock(bx,by+1,bz);
        if (blockAbove != null && (blockAbove.getBlockType()==BlockType.water.id || blockAbove.getBlockType()==BlockType.deepwater.id))
        {
            height = 16;
            fullFace = true;
        }
        return height;
    }

    public void render(int bx, int by, int bz, IBlockAccess world, Renderer rend)
    {
        int face[] = {Face.NORTH,Face.EAST,Face.SOUTH,Face.WEST,Face.UP,Face.DOWN};
        
        Block thisBlock = world.getBlock(bx,by,bz);
        int dir = thisBlock.getDirection();
        
        int height = getHeight(world,bx,by,bz);
        for (int d=0;d<6;d++)
        {
            boolean visible = true;
            int lookTo = d;
            int fac = Direction.rotateOppositeBy(d,dir);
            int x1 = bx+Direction.x[lookTo];
            int y1 = by+Direction.y[lookTo];
            int z1 = bz+Direction.z[lookTo];
            Block thatBlock = world.getBlock(x1,y1,z1);
            if (thatBlock != null) 
            {
                visible = Cull.isFaceVisable(lookTo, world,bx,by,bz,x1,y1,z1);
            }
            if ( visible )
            {
                int light = getLight(lookTo);
                rend.getRenderer(mat).addFace(multiTex[0], face[d], bx,by,bz, 16,height,16, light, dir,dir, false,false,rend.getCollision(collisionType));
            }
        }
    }
}