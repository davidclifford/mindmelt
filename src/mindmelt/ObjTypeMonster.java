package mindmelt;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import mindmelt.ObjType;

public class ObjTypeMonster extends ObjType {

    ObjTypeMonster(int type, int subtype, String name)
    {
        super(type,subtype,name);
    }
    
    protected Node render(int id)
    {
        int face[] = {Face.NORTH,Face.EAST,Face.SOUTH,Face.WEST,Face.UP,Face.DOWN};
        int light[] = {200,160,200,160,240,120,};
        float SC = SubRender.SC;
        String idString = "mon:"+id;
        Node node = new Node(idString);

        SubRender rend = new SubRender(TextureMats.MONSTER,idString);
        for (int f=0;f<6;f++) {
            rend.addFace(texture[f], face[f], 0, 0, 0, w, h, d, light[f], Direction.south, Direction.south, mirror[f], true, null);
        }
        Geometry geom = rend.render();
        geom.setLocalTranslation(0,h*SC/16f,0);
        node.attachChild(geom);
        
        rend = new SubRender(TextureMats.MONSTER,idString);
        for (int f=0;f<6;f++) {
            rend.addFace(texture[f]+1, face[f], 0, 0, 0, w, h, d, light[f], Direction.south, Direction.south, mirror[f], true, null);
        }
        Geometry geom1 = rend.render();
        geom1.setLocalTranslation(0,0,0);
        node.attachChild(geom1);
                            
        shape = node;
        return node;
    }    

}