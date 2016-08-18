package mindmelt;

import com.jme3.math.FastMath;

public class Vec3i 
{
    public int x,y,z;
    
    public Vec3i(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vec3i(float x, float y, float z)
    {
        this.x = (int)x;
        this.y = (int)y;
        this.z = (int)z;
    }
    
    @Override
    public String toString()
    {
        return ""+x+":"+y+":"+z;
    }    
}
