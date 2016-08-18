package mindmelt;

public class BlockGate extends BlockDoor
{
    protected BlockGate(int id)
    {
        super((byte)id);
    }  
    
    protected String getName()
    {
        return "gate";
    }
    
    protected int getUpper()
    {
        return BlockType.gate.id;
    }    
    
    protected int getLower()
    {
        return BlockType.gate.id;
    }
}
