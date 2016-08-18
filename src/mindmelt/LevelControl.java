package mindmelt;

import java.util.ArrayList;

public class LevelControl {

    private ArrayList<LevelEntryExit> levelEntryExit = new ArrayList<LevelEntryExit>();
    private ArrayList<LevelTalk> levelTalk = new ArrayList<LevelTalk>();
    private ArrayList<LevelCode> levelCode = new ArrayList<LevelCode>();
    
    private int version;
    private String name;
    private String description;
    private int id;
    
    public LevelEntryExit findEntryExit(int x, int y, int z)
    {
        for (LevelEntryExit lvl : levelEntryExit )
        {
            if( lvl.inRange(x,y,z) )
            {
                return lvl;
            }
        }
        return null;
    }
    
    public void addEntryExit(LevelEntryExit entryExit)
    {
        levelEntryExit.add(entryExit);
    }

    public void addTalk(LevelTalk talk)
    {
        levelTalk.add(talk);
    }

    public void addCode(LevelCode code)
    {
        levelCode.add(code);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
