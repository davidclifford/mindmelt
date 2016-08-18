package mindmelt;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

public class Collisions 
{
    static final int NONE = 3;
    static final int NORMAL = 0;
    static final int WATER = 1;
    static final int FF = 2;
    static final int MAX = 3;
    
    static final int collGroups [] = 
        {PhysicsCollisionObject.COLLISION_GROUP_01,
         PhysicsCollisionObject.COLLISION_GROUP_02,
         PhysicsCollisionObject.COLLISION_GROUP_03};
    
    BulletAppState bulletAppState;
    
    ArrayList<Vector3f> collisionVerts;
    ArrayList<Integer> collIndx;
    int index=0;
    PhysicsCollisionObject collisionGroup;
    int collGroup = Collisions.NORMAL;
    
    public Collisions(int collGroup, BulletAppState bulletAppState) 
    {
        this.bulletAppState = bulletAppState;
        this.collGroup = collGroup;
        collisionVerts = new ArrayList<Vector3f>();
        collIndx = new ArrayList<Integer>();
        index = 0;
    }
    
    public RigidBodyControl generateCollisions(boolean kinematic)
    {
        if (index==0)
            return null;
        Mesh collisionMesh = new Mesh();
        Vector3f[] vec = collisionVerts.toArray(new Vector3f[collisionVerts.size()]);
        int ind[] = SubRender.convertIntegers(collIndx);
        collisionMesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vec));
        collisionMesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(ind));
        collisionMesh.updateBound();
        collisionMesh.updateCounts();
        collisionMesh.setMode(Mesh.Mode.Triangles);
        collisionMesh.setStatic();

        Geometry collisionGeometry = new Geometry("CollisionMesh", collisionMesh);
        CollisionShape collisionShape = CollisionShapeFactory.createMeshShape(collisionGeometry);
        RigidBodyControl collisionControl = new RigidBodyControl(collisionShape, 0);
        collisionControl.setKinematic(kinematic);
        for (int j = 0; j < MAX; j++) 
        {
            if (collGroup == j) 
            {
                collisionControl.setCollisionGroup(collGroups[collGroup]);
            } 
            else 
            {
                collisionControl.removeCollideWithGroup(collGroups[j]);
            }
        }

        return collisionControl;
    }   
    
    public void addVert(Vector3f vert)
    {
        collisionVerts.add(vert);
    }
    
    public void addQuadIndex()
    {
        collIndx.add(index+2);
        collIndx.add(index+0);
        collIndx.add(index+1);
        collIndx.add(index+1);
        collIndx.add(index+3);
        collIndx.add(index+2);
        index += 4;
    }    
    
    public void addTri1Index()
    {
        collIndx.add(index+2);
        collIndx.add(index+0);
        collIndx.add(index+1);
        index += 3;
    }    
    
    public void addTri2Index()
    {
        collIndx.add(index+1);
        collIndx.add(index+0);
        collIndx.add(index+2);
        index += 3;
    }    
}
