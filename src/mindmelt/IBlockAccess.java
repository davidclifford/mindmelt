package mindmelt;

public interface IBlockAccess 
{
    // Get Block in World coords
    public Block getBlock(int x,int y, int z);
    
    // Add block in world coords
    public Block addBlock(Block block, int x, int y, int z);
    
}
