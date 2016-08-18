package mindmelt;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;

public class TextureMats 
{
    static final int MAX = 8;
    static Material matTypes[] = new Material[MAX];
    static final int OPAQUE = 0;
    static final int BOTHSIDES = 1;
    static final int TRANS = 2;
    static final int ANIM = 3;
    static final int WATERANIM = 4;
    static final int OBJECT = 5;
    static final int MONSTER = 6;
    static final int PERSON = 7;

    
    TextureMats(AssetManager assetManager)
    {
        Texture tex = assetManager.loadTexture("Textures/terrain.png");
        tex.setMagFilter(Texture.MagFilter.Nearest);
        tex.setMinFilter(Texture.MinFilter.NearestLinearMipMap );
        
        Texture objTex = assetManager.loadTexture("Textures/objects.png");
        objTex.setMagFilter(Texture.MagFilter.Nearest);
        objTex.setMinFilter(Texture.MinFilter.NearestLinearMipMap );
        
        Texture monsterTex = assetManager.loadTexture("Textures/monster.png");
        monsterTex.setMagFilter(Texture.MagFilter.Nearest);
        monsterTex.setMinFilter(Texture.MinFilter.NearestLinearMipMap );
        
        Texture personTex = assetManager.loadTexture("Textures/person.png");
        personTex.setMagFilter(Texture.MagFilter.Nearest);
        personTex.setMinFilter(Texture.MinFilter.NearestLinearMipMap );
        
        // Single side (backface cull)
        matTypes[OPAQUE] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTypes[OPAQUE].setTexture("ColorMap", tex);
        matTypes[OPAQUE].setBoolean("VertexColor", true);
        matTypes[OPAQUE].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        matTypes[OPAQUE].getAdditionalRenderState().setAlphaTest(true);
        matTypes[OPAQUE].getAdditionalRenderState().setAlphaFallOff(0.5f);
        matTypes[OPAQUE].setName("OPAQUE");
        
        // Both sides
        matTypes[BOTHSIDES] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTypes[BOTHSIDES].setTexture("ColorMap", tex);
        matTypes[BOTHSIDES].setBoolean("VertexColor", true);
        matTypes[BOTHSIDES].getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        matTypes[BOTHSIDES].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        matTypes[BOTHSIDES].getAdditionalRenderState().setAlphaTest(true);
        matTypes[BOTHSIDES].getAdditionalRenderState().setAlphaFallOff(0.5f);
        matTypes[BOTHSIDES].setName("BOTHSIDES");
        
        // Transparent (eg stained glass, water)
        matTypes[TRANS] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTypes[TRANS].setTexture("ColorMap", tex);
        matTypes[TRANS].setBoolean("VertexColor", true);
        matTypes[TRANS].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        matTypes[TRANS].setName("TRANS");
        matTypes[TRANS].setTransparent(true);

        // Single side moving
        matTypes[ANIM] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTypes[ANIM].setTexture("ColorMap", tex);
        matTypes[ANIM].setBoolean("VertexColor", true);
        matTypes[ANIM].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        matTypes[ANIM].getAdditionalRenderState().setAlphaTest(true);
        matTypes[ANIM].getAdditionalRenderState().setAlphaFallOff(0.5f);
        matTypes[ANIM].setName("ANIM");
        // Transparent (eg stained glass, water)
        matTypes[WATERANIM] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTypes[WATERANIM].setTexture("ColorMap", tex);
        matTypes[WATERANIM].setBoolean("VertexColor", true);
        matTypes[WATERANIM].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        //matTypes[WATERANIM].getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        matTypes[WATERANIM].setName("WATERANIM");
        matTypes[WATERANIM].setTransparent(true);

        matTypes[OBJECT] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTypes[OBJECT].setTexture("ColorMap", objTex);
        matTypes[OBJECT].setBoolean("VertexColor", true);
        matTypes[OBJECT].getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        matTypes[OBJECT].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        matTypes[OBJECT].setTransparent(true);
        matTypes[OBJECT].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        matTypes[OBJECT].getAdditionalRenderState().setAlphaTest(true);
        matTypes[OBJECT].getAdditionalRenderState().setAlphaFallOff(0.5f);
        matTypes[OBJECT].setName("OBJECT");

        matTypes[MONSTER] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTypes[MONSTER].setTexture("ColorMap", monsterTex);
        matTypes[MONSTER].setBoolean("VertexColor", true);
        matTypes[MONSTER].getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        matTypes[MONSTER].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        matTypes[MONSTER].setTransparent(true);
        matTypes[MONSTER].setName("MONSTER");

        matTypes[PERSON] = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTypes[PERSON].setTexture("ColorMap", personTex);
        matTypes[PERSON].setBoolean("VertexColor", true);
        matTypes[PERSON].getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        matTypes[PERSON].getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        matTypes[PERSON].setTransparent(true);
        matTypes[PERSON].setName("PERSON");
    }
    static Material getMaterialType(int type)
    {
        return matTypes[type];
    }
}
