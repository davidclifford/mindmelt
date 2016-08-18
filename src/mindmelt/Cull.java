package mindmelt;

public class Cull 
{
    static final int None = 0;
    static final int Opaque = 1;
    static final int Transparent = 2;
    static final int Translucent = 3;
    
    // Based only on block types
    static final boolean isFaceVisable(BlockType b1, BlockType b2)
    {
        int c1 = b1.cull;
        int c2 = b2.cull;
        if (c1 == Opaque && c2 == Opaque) return false;
        if (c1 == Transparent && c2 == Transparent) return false;
        if (c1 == Transparent && c2 == Opaque) return false;
        if (c1 == Translucent && c2 == Opaque) return false;
        if (c1 == Translucent && c2 == Translucent) return false;
        return true;
    }
    
    // Based on Blocks i.e. with state
    // Block 1 = what to cull
    // Block 2 = which to compare against
    // side = N,S,E,W,U,D
    
    static final boolean isFaceVisable(int lookTo, IBlockAccess world, int x, int y, int z, int x1,int y1,int z1)
    {
        Block b1 = world.getBlock(x, y, z);
        Block b2 = world.getBlock(x1, y1, z1);
        BlockType bt1 = BlockType.getBlockType(b1.getBlockType());
        BlockType bt2 = BlockType.getBlockType(b2.getBlockType());
        int c1 = bt1.cull;
        int c2 = bt2.cull;
        int d1 = b1.direction;
        int d2 = b2.direction; 
        int f1 = bt1.getFace(lookTo, world, x, y, z);
        int s2 = Direction.opposite(lookTo);
        int f2 = bt2.getFace(s2,world,x1,y1,z1);
        
        if (f1 == Face.NONE) 
            return true;
        if (f2 == Face.FULL) 
            return isFaceVisable(bt1, bt2);

        //Triangles
        if (f1==Face.TRIANGLE_LEFT && f2==Face.TRIANGLE_RIGHT) 
            return false;
        if (f1==Face.TRIANGLE_RIGHT && f2==Face.TRIANGLE_LEFT) 
            return false;
        if (f1==Face.PART && f2==Face.PART) 
            return false;
        
        return true;
    }
}
