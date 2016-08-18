package mindmelt;

import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.util.BufferUtils;

public class AnimControl extends AbstractControl
{
    private float speed = 1f/16f;
    private float rot = 0f;
    private int tile = 1;
    private float off = 1f/16f;
    private int num = 3;
    
    public void setSpeed(int speed)
    {
        this.speed = 1f/speed;
    }
    
    public void setNumAnims(int num)
    {
        this.num = num - 1;
    }
    
    @Override
    protected void controlUpdate(float tpf) 
    {
        float S = SubRender.SC;
        Geometry geom = (Geometry)getSpatial();
        rot+=tpf;
        if (rot>=speed)
        {
            Mesh mesh = geom.getMesh();
            VertexBuffer coords = mesh.getBuffer(VertexBuffer.Type.TexCoord);
            int elms = coords.getNumElements();
            Vector2f[] tex = new Vector2f[elms];
            for (int e=0;e<elms;e++)
            {
                float u = (Float)coords.getElementComponent(e, 0);
                float v = (Float)coords.getElementComponent(e, 1);
                //System.out.println(u+" "+v);
                tex[e] = new Vector2f(u+off,v);
            }
            /*coords.compact(0);
            coords=null;*/
            mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(tex));
            tile++;
            off = 1f/16f;
            if (tile>num)
            {
                tile=0;
                off = -num*1f/16f;
            }
            //System.out.println("rot "+rot+" tile "+tile+" off "+off);
            rot -=speed;
        }
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) 
    {
        return;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) 
    {
        LeverControl control = new LeverControl();
        spatial.addControl(control);
        return control;
    }
}
