package mindmelt;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;


public class ObjMonster extends Obj {
    
    public final static int DEAD = 0;
    public final static int WANDER = 1;
    public final static int ATTACK = 2; 
    public final static int RUNAWAY = 3; 
    
    protected int status = DEAD;
    
    ObjMonster(int id, int item, String name)
    {
        super(id, item, name);
        status = WANDER;
    }      
    
}
