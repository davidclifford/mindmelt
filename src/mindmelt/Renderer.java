package mindmelt;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class Renderer 
{
    private SubRender renderer[] = new SubRender[TextureMats.MAX];
    private Collisions collide[] = new Collisions[Collisions.MAX];
    private String name;
    private BulletAppState bas;
    
    Renderer(String name, BulletAppState bulletAppState)
    {
        this.name = name;
        this.bas = bulletAppState;
        for (int material=0; material<TextureMats.MAX; material++)
        {
            renderer[material] = new SubRender(material,name+":"+material);
        }        
        for (int col=0; col<Collisions.MAX; col++)
        {
            collide[col] = new Collisions(col,bulletAppState);
        }        
    }
    
    public Node render(boolean kinematic)
    {
        Node shape = new Node();
        
        for (int material=0; material<TextureMats.MAX; material++)
        {
            Geometry geom = renderer[material].render();
            if (geom!=null)
            {               
                shape.attachChild(geom);
            }
        }
        for (int col=0; col<Collisions.MAX; col++)
        {
            RigidBodyControl collisionControl = collide[col].generateCollisions(kinematic);
            if (collisionControl!=null)
            {
                shape.addControl(collisionControl);
                bas.getPhysicsSpace().add(collisionControl);
            }
        }

        return shape;
    }
    
    public SubRender getRenderer(int material)
    {
        return renderer[material];
    }
    
    public Collisions getCollision(int collType)
    {
        if (collType==Collisions.NONE) 
            return null;
        return collide[collType];
    }
    
    public BulletAppState getBulletAppState()
    {
        return bas;
    }
}
