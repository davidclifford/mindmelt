package mindmelt;

public class BlockTypeTransparent extends BlockType
{
    BlockTypeTransparent(int type)
    {
        super(type);
        cull=Cull.Transparent;
        mat = TextureMats.OPAQUE;
    }
}
