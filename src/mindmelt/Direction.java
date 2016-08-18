package mindmelt;

public class Direction 
{
    public static final int x[] = {0,1,0,-1,0,0,0};
    public static final int y[] = {0,0,0,0,1,-1,0};
    public static final int z[] = {-1,0,1,0,0,0,0};

    public static final int north=0;
    public static final int east=1;//
    public static final int south=2;
    public static final int west=3;//
    public static final int up=4;
    public static final int down=5;
    public static final int none=6;
    
    // rotate clockwise by amount
    static int rotateBy(int dir, int turn)
    {
        if (dir>=4) return dir;
        int newDir = (dir+turn)%4;
        while (newDir<0) newDir+=4;
        while (newDir>4) newDir-=4;
        return newDir;
    }
    
    // rotate anti-clockwise by amount
    static int rotateOppositeBy(int dir, int turn)
    {
        if (dir>=4) return dir;
        int newDir = (dir-turn)%4;
        while (newDir<0) newDir+=4;
        while (newDir>4) newDir-=4;
        return newDir;
    }
    
    static int antiClockwise(int dir)
    {
        return (dir-1)%4;
    }
    
    static int clockwise(int dir)
    {
        int s = (dir+1)%4;
        if (s<0) s+=4;
        return s;
    }
    
    static int opposite(int dir)
    {
        if (dir==north) return south;
        if (dir==west) return east;
        if (dir==south) return north;
        if (dir==east) return west;
        if (dir==up) return down;
        if (dir==down) return up;
        return dir;
    }
    
    static int top(int dir)
    {
        if (dir==west) return east;
        if (dir==east) return west;
        if (dir==up) return down;
        if (dir==down) return up;
        return dir;
    }
    
    static int dirX(int dir,int turn)
    {
        int d = turn;
        if (turn<=west)
            d = (dir+turn)%4;
        return x[d];
    }
    static int dirY(int dir,int turn)
    {
        return y[turn];
    }
    static int dirZ(int dir, int turn)
    {
        int d = turn;
        if (turn<=west)
            d = (dir+turn)%4;
        return z[d];
    }
}
