package mindmelt;

public class BlockTypeChair extends BlockType
{
    BlockTypeChair(int type)
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
    
    public void render(int bx, int by, int bz, IBlockAccess world, Renderer rend)
    {
        int face[] = {Face.NORTH,Face.EAST,Face.SOUTH,Face.WEST,Face.UP};
        
        Block thisBlock = world.getBlock(bx,by,bz);
        int dir = thisBlock.getDirection();
        
        for (int d=0;d<5;d++)
        {
            int lookTo = d;
            int fac = Direction.rotateOppositeBy(d,dir);
            int light = getLight(lookTo);
            int f = face[d];
            int or = Direction.south;
            if (f==Face.UP) or = Direction.top(dir);
            int tex = multiTex[fac];
            int h = height;
            if (fac==Face.SOUTH) h = 16;
            rend.getRenderer(mat).addFace(tex, f, bx,by,bz, width,h,width, light,dir,or,false,false,rend.getCollision(collisionType));
        }
    }
}
