package mindmelt;

import com.jme3.scene.Node;

public class ObjPlayer extends Obj {

    ObjPlayer( int item, int type, String name)
    {
        super(item,type,name);
    }
    
    
    public Node render()
    {
        return new Node();
    }
  
}
