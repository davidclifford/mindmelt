package mindmelt;

import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;

public class Block 
{
    protected byte direction;
    protected byte id;
    
    protected Block(int id)
    {
        this.id = (byte)id;
        this.direction = Direction.north;
    }
    
    static Block newBlock(int id)
    {
        int param[] = {0,0,0,0,0,0,0,0,0};
        Block block = newBlockWithParams(id,param);
        return block;
    }
    
    static Block newBlockWithParams(int id, int param[])
    {
        Block block;
        if (id==BlockType.doorknob.id || id==BlockType.doortop.id)
        {
            block = new BlockDoor(id);
            ((BlockDoor)block).setStatus((byte)param[1]);
        }
        else
        if (id==BlockType.doorlock.id)
        {
            block = new BlockDoorLocked(id);
            ((BlockDoorLocked)block).setStatus((byte)param[1]);
        }
        else
        if (id==BlockType.gate.id)
        {
            block = new BlockGate(id);
            ((BlockGate)block).setStatus((byte)param[1]);
        }
        else
        if (id==BlockType.button.id)
        {
            block = new BlockButton(id);
            ((BlockButton)block).setStatus((byte)param[1]);
        }
        else
        if (id==BlockType.lever.id)
        {
            block = new BlockLever(id);
            ((BlockLever)block).setStatus((byte)param[1]);
        }
        else
        if (id==BlockType.trapdoor.id)
        {
            block = new BlockTrapDoor(id);
            ((BlockTrapDoor)block).setStatus((byte)param[1]);
        }
        else
        {
            block = new Block(id);
        }
        
        block.setDirection(param[0]);
        return block;
    }
    
    public boolean canEnter()
    {
        BlockType blockType = BlockType.getBlockType(id);
        return blockType.canEnter();
    }

    public boolean isWater()
    {
        BlockType blockType = BlockType.getBlockType(id);
        return blockType.isWater();
    }
    public int getDirection()
    {
        return direction;
    }
    
    public void setDirection(int dir)
    {
        direction = (byte)dir;
    }
    
    public int getBlockType()
    {
        return id;
    }
    
    public boolean isAir()
    {
        return id == BlockType.air.id;
    }
    
    protected Node shape(int bx, int by, int bz, IBlockAccess world, Renderer rend)
    {
        BlockType blockType = BlockType.getBlockType(id);
        if (blockType != null)
            blockType.render(bx, by, bz, world, rend);
        return null;
    }
    
    public String getInfo()
    {
        String info = ""+id;
        String specific = getSpecificInfo();
        if (specific != null) {
            info += ","+direction+specific;
        } else {
            if (direction>0) {
                info += ","+direction;
            }
        }
        info += ":";
        return info;
    }
    
    protected String getSpecificInfo()
    {
        return null;
    }
}
