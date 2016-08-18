package mindmelt;

public class BlockTypeSecret extends BlockType
{
    BlockTypeSecret(int type)
    {
        super(type);
        cull=Cull.Opaque;
        //mat = 0;
        collisionType = Collisions.NONE;
    }

    protected int getFace(int side, IBlockAccess world,int bx,int by,int bz)
    {
        return Face.NONE;
    }

    public void render(int bx, int by, int bz, IBlockAccess world, Renderer rend)
    {
        int face[] = {Face.SOUTH,Face.EAST,Face.NORTH,Face.WEST,Face.UP,Face.DOWN};
        
        Block thisBlock = world.getBlock(bx,by,bz);
        int dir = thisBlock.getDirection();
        
        for (int d=0;d<6;d++)
        {
            int lookTo = d;
            int fac = Direction.rotateOppositeBy(d,dir);
            int x1 = bx+Direction.x[lookTo];
            int y1 = by+Direction.y[lookTo];
            int z1 = bz+Direction.z[lookTo];
            Block thatBlock = world.getBlock(x1,y1,z1);
            if (thatBlock==null || thatBlock.isAir() || !BlockType.getBlockType(thatBlock.getBlockType()).isFullBlock()) 
            {
                int light = getLight(lookTo);
                int f = face[d];
                int or = Direction.south;
                if (f==Face.DOWN) or = dir;
                if (f==Face.UP) or = Direction.top(dir);
                int tex = multiTex[fac];
                rend.getRenderer(mat).addFace(tex, f, bx,by,bz, width,height,depth, light,dir,or,false,false,rend.getCollision(collisionType));
            }
        }
    } 
}
