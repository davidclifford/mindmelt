package mindmelt;

import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class World implements IBlockAccess 
{
    Map chunks = new HashMap<String,Chunk>();
    Iterator it;
       
    public void iterateChunks()
    {
        it = chunks.entrySet().iterator();
    }
    
    public Chunk getNextChunk()
    {
        if (!it.hasNext()) 
            return null;
        Map.Entry<String,Chunk> entry = (Map.Entry<String,Chunk>)it.next();
        String key = entry.getKey();
        return entry.getValue();
    }
    
    // in world coords
    public Block getBlock(int x,int y, int z)
    {
        Vec3i pos = new Vec3i(x>>4,y>>4,z>>4);
        Chunk chunk = (Chunk)chunks.get(pos.toString());
        if (chunk==null) return null;
        return chunk.getBlock(x, y, z);
    }
    
    // in world coords
    public Block addBlock(Block block, int x, int y, int z)
    {
        Vec3i pos = new Vec3i(x>>4,y>>4,z>>4);
        Chunk chunk = (Chunk)chunks.get(pos.toString());
        if (chunk==null)
        {
            chunk = new Chunk(x>>4,y>>4,z>>4);
            chunks.put(pos.toString(), chunk);
        }
        chunk.addBlock(block,x,y,z);
        
        return block;
    }
    
    // in chunk coords
    public Chunk getChunk(int x,int y, int z)
    {
        //System.out.println(new Vec3i(x,y,z).toString());
        Vec3i pos = new Vec3i(x,y,z);
        Chunk chunk = (Chunk)chunks.get(pos.toString());
        if (chunk==null) return null;
        return chunk;
    }
    
    public void renderAll(Node node, BulletAppState bulletAppState)
    {
        Collection c = chunks.values();
        Iterator it = c.iterator();
        while (it.hasNext())
        {
            Chunk chunk = (Chunk)it.next();
            Node n  = chunk.render(this, bulletAppState);
            if (n!=null) 
                node.attachChild(n);
        }
    }
    
    // in world coords
    public void _update(Node node, int x, int y, int z, BulletAppState bulletAppState)
    {
        Chunk chunk = getChunk(x>>4,y>>4,z>>4);
        if (chunk!=null)
            node.attachChild(chunk.render(this, bulletAppState));
    }
    
    public void update(Node node, int x, int y, int z, BulletAppState bulletAppState)
    {
        System.out.println("updating "+new Vec3i(x,y,z));
        _update(node,x,y,z,bulletAppState);
        if ((x&15)==0) _update(node,x-1,y,z,bulletAppState);
        if ((x&15)==15) _update(node,x+1,y,z,bulletAppState);
        if ((y&15)==0) _update(node,x,y-1,z,bulletAppState);
        if ((y&15)==15) _update(node,x,y+1,z,bulletAppState);
        if ((z&15)==0) _update(node,x,y,z-1,bulletAppState);
        if ((z&15)==15) _update(node,x,y,z+1,bulletAppState);
    }
    
    public void destroy(BulletAppState bulletAppState)
    {
        iterateChunks();
        Chunk chunk;
        while((chunk=getNextChunk())!=null)
        {
            chunk.destroy(bulletAppState);
        }
    }
}
