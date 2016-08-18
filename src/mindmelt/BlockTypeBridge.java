package mindmelt;

public class BlockTypeBridge extends BlockType
{
    BlockTypeBridge(int type)
    {
        super(type);
        cull=Cull.Opaque;
        mat = TextureMats.BOTHSIDES;
    }

    protected int getFace(int side, IBlockAccess world,int bx,int by,int bz)
    {
        if (side==Face.DOWN)
            return Face.FULL;
        return Face.PART;
    }

    public void render(int bx, int by, int bz, IBlockAccess world, Renderer rend)
    {
        int face[] = {Face.NORTH,Face.EAST,Face.SOUTH,Face.WEST,Face.UP,Face.DOWN};
        
        Block thisBlock = world.getBlock(bx,by,bz);
        int dir = thisBlock.getDirection();
        
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
                int f = face[d];
                int or = Direction.south;
                if (f==Face.DOWN) or = dir;
                if (f==Face.UP) or = Direction.top(dir);
                int tex = multiTex[fac];
                int h = height;
                if (fac==Face.WEST || fac==Face.EAST)
                    h = 8;
                rend.getRenderer(mat).addFace(tex, f, bx,by,bz, width,h,width, light,dir,or,false,false,rend.getCollision(collisionType));
            }
        }
    } 
}
