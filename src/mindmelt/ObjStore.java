package mindmelt;

import com.jme3.scene.Node;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObjStore {
    private String name;
    private Map store;
    
    ObjStore(String name)
    {
        this.name = name;
        store =  new HashMap<Integer,Obj>();
    }
    
    public ObjStore addObj(int id, Obj obj) {
        store.put(id, obj);
        return this;
    }
    
    public Obj getObj(int id)
    {
        return (Obj)store.get(id);
    }
    
    public Obj getObjectByName(String byName)
    {
        String name = byName;
        if (name.startsWith("obj:"))
            name = name.substring(4);
        Iterator it = getIterator();
        while (it.hasNext()) {
            Obj ob = (Obj)it.next();
            if (ob.getName().equals(name)) {
                return ob;
            }
        }
        return null;
    }
    
    public Iterator getIterator()
    {
        Collection c = store.values();
        return c.iterator();
    }
    
    public void destroy(Node node)
    {
        node.detachAllChildren();
        node.removeFromParent();
    }
}
