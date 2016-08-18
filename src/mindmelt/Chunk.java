package mindmelt;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.util.List;
import java.util.Random;

public class Chunk 
{
    Block chunk[][][] = new Block[16][16][16];
    
    int cx,cy,cz;
    Node node = null;
    Random r = new Random();

    // in chunk coords
    public Chunk(int xx, int yy, int zz)
    {
        cx=xx;
        cy=yy;
        cz=zz;
        
        for (int y=0; y<16; y++)
        {
            for (int z=0; z<16; z++)
            {
                for (int x=0; x<16; x++)
                {
                    chunk[x][y][z] = new Block(BlockType.air.id);
                }    
            }
        }

    }
    
    // in world coords
    public Block addBlock(Block block, int x, int y, int z)
    {
        //System.out.println(new Vec3i(x,y,z).toString());
        chunk[x&15][y&15][z&15] = block;
        return block;
    }
    
    // in world coords
    public Block getBlock(int x, int y, int z)
    {
        return chunk[x&15][y&15][z&15];
    }
    
    // Render or re-render whole chunk
    public Node render(IBlockAccess chunks, BulletAppState bulletAppState)
    {
        destroyChildren(node,bulletAppState);
        node = new Node();
        
        Renderer rend = new Renderer("chunk:"+cx+":"+cy+":"+cz, bulletAppState);

        for (int x=0;x<16;x++)
        {
            for (int y=0;y<16;y++)
            {
                for (int z=0;z<16;z++)
                {
                    Block block = chunk[x][y][z];
                    if (block!=null && !block.isAir())
                    {
                        // Returns child node (e.g door, lever etc)   
                        // or null which means the block has been added to the 'rend' object
                        Node child = block.shape((cx<<4)+x,(cy<<4)+y,(cz<<4)+z,chunks,rend);
                        if (child!=null)
                            node.attachChild(child);
                    }
                }
            }
        }
        node.attachChild(rend.render(false)); 
        //System.out.println("Chunk = "+cx+":"+cy+":"+cz+" number of blocks = "+b);
        return node;
    }
    
    public void destroy(BulletAppState bulletAppState)
    {
        if (node==null) 
            return;
        System.out.println("Entering node "+cx+" "+cy+" "+cz);
        int numControls = node.getNumControls();
        for (int c = 0; c<numControls; c++)
        {
            Control control = node.getControl(c);
            System.out.println("Destroying control "+control.toString());
            node.removeControl(control);
            control = null;
        }
        destroyChildren(node, bulletAppState);
        node.detachAllChildren();
        node.removeFromParent();
    }
    
    private void destroyChildren(Spatial node, BulletAppState bulletAppState)
    {
        if (node==null)
            return;
        System.out.println("Entering child node "+node.getName()+" "+node.toString());
        if (node instanceof Node)
        {
            List<Spatial> children = ((Node)node).getChildren();
            for (Spatial child : children)
            {
                if (child instanceof Node)
                    destroyChildren((Node)child,bulletAppState);
            }
        }
        int numControls = node.getNumControls();
        System.out.println("numControls "+numControls);
        for (int c = 0; c<numControls; c++)
        {
            System.out.println("Control no. "+c);
            Control control = node.getControl(0);
            System.out.println("Destroying control "+control.getClass().toString());
            node.removeControl(control);
            if (control instanceof RigidBodyControl)
            {
                bulletAppState.getPhysicsSpace().remove(control);
                System.out.println("Removing control "+control.toString());
            }
            control = null;
        }
        node.removeFromParent();
    }
 }
