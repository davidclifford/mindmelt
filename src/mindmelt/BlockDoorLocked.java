package mindmelt;

public class BlockDoorLocked extends BlockDoor
{
    protected BlockDoorLocked(int id)
    {
        super((byte)id);
    }  
    
    protected String getName()
    {
        return "lock";
    }
    
    protected int getUpper()
    {
        return BlockType.doortop.id;
    }    
    
    protected int getLower()
    {
        return BlockType.doorlock.id;
    }
}
