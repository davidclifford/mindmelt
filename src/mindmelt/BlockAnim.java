/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mindmelt;

import com.jme3.math.FastMath;
import com.jme3.scene.Node;

/**
 *
 * @author david_000
 */
public class BlockAnim extends Block
{
    protected BlockAnim(int id)
    {
        super((byte)id);
    }
/*        
    protected Node shape(int bx, int by, int bz, IBlockAccess world, Renderer r)
    {
        float S = SubRender.SC;

        BlockType blockType = BlockType.getBlockType(id);
        Renderer rend = new Renderer("FF:"+bx+":"+by+":"+bz, r.getBulletAppState());
        blockType.render(0, 0, 0, world, rend);
        Node ff = rend.render(true);
        ff.setLocalTranslation(-S/2f,0,-S/2f);
        Node axel = new Node("axel");
        axel.attachChild(ff);
        axel.setLocalTranslation(bx*S+S/2f, by*S, bz*S+S/2f);
        
        Rot4Control ffControl = new Rot4Control();
        ffControl.setSpeed(1f/8f);
        axel.addControl(ffControl);
        
        return axel;
    }*/
}
