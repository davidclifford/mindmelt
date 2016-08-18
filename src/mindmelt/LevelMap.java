package mindmelt;

import com.jme3.bullet.BulletAppState;
import com.jme3.scene.Node;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class LevelMap {
    
    private Random r = new Random(System.currentTimeMillis());
    private String mapName;
    private LevelControl levelControl;
    private ObjStore levelObjects;
    private World world;
    private Node levelNode;
    
    public LevelMap(String mapName)
    {
        this.mapName = mapName;
    }

    public void loadLevel(BulletAppState bulletAppState, Node rootNode, ObjStore objects) {
        world = new World();
        levelObjects = new ObjStore("level:"+mapName);
        levelNode = new Node();
        
        try {
            loadLevelMap(world);
            loadLevelControl(mapName);
            loadObjects(levelControl.getId(), objects);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        renderAllBlocks(bulletAppState);
        rootNode.attachChild(levelNode);
    }    

    private void loadObjects(int level, ObjStore objects) {
        objects.getObj(0).setLevel(level);
        Iterator it = objects.getIterator();
        while (it.hasNext())
        {
            Obj obj = (Obj)it.next();
            if (obj.getLevel() == level)
            {
                levelObjects.addObj(obj.getId(),obj);
                Node ob = obj.render();
                int type = obj.getType();
                if (ObjType.getObjSubtype(type)==ObjType.MONSTER) {
                    MonsterControl monControl = new MonsterControl();
                    monControl.setLevel(this);
                    monControl.setMonster((ObjMonster)obj);
                    ob.addControl(monControl);
                }
                levelNode.attachChild(ob);
            }
        }
    }
    
    private void loadLevelControl(String mapName) throws Exception 
    {
        final int HEADER=1;
        final int ENTRY=2;
        final int CODE=3;
        final int TALK=4;
        String line;
        String filename = "data/" + mapName + ".ctl";
        BufferedReader input = new BufferedReader(new FileReader(filename));
        levelControl = new LevelControl();
        int mode = HEADER;
        LevelEntryExit entryExit = null;
        
        while ((line = input.readLine())!=null) {
            if (line.startsWith("//"))
                continue;
            if (mode==HEADER) {
                Scanner toke = new Scanner(line);
                toke.useDelimiter("=");
                String key = toke.next();
                if (key.equals("version")) levelControl.setVersion(toke.nextInt());
                else if (key.equals("num")) levelControl.setId(toke.nextInt());
                else if (key.equals("name")) levelControl.setName(toke.next());
                else if (key.equals("description")) levelControl.setDescription(toke.next());
                else if (key.equals("--")) {
                    entryExit = new LevelEntryExit();
                    mode ++;
                }
            } else if (mode==ENTRY) {
                Scanner toke = new Scanner(line);
                toke.useDelimiter("=");
                String key = toke.next();
                if (key.equals("filename")) entryExit.setFilename(toke.next());
                else if (key.equals("desc")) entryExit.setDesc(toke.next());
                else if (key.equals("from")) entryExit.setFrom(toke.next());
                else if (key.equals("to")) entryExit.setTo(toke.next());
                else if (key.equals("-")||key.equals("--"))
                {
                    levelControl.addEntryExit(entryExit);
                    if (key.equals("-"))
                    {
                        entryExit = new LevelEntryExit();
                    } else {
                        mode++;
                    }
                }
            }
        }
        input.close();
    }
    
    private void loadLevelMap(World world) throws Exception 
    {
        String line;
    
        String filename = "data/" + mapName + ".lvl";
        BufferedReader input = new BufferedReader(new FileReader(filename));
        //Header
        int chunkx = 0, chunky = 0, chunkz = 0;
        int x = 0 ,y = 0 ,z = 0;
        
        while ((line = input.readLine())!=null) {
            int cp = 0;
            if (line.startsWith("C"))
            {
                //New chunk
                Scanner toke = new Scanner(line.substring(cp+1));
                toke.useDelimiter(",");
                chunkx = (new Integer(toke.next())).intValue();
                chunky = (new Integer(toke.next())).intValue();
                chunkz = (new Integer(toke.next())).intValue();
                //System.out.println("New chunk "+chunkx+":"+chunky+":"+chunkz);
                x = y = z = 0;
            } else {
                Scanner toke = new Scanner(line);
                toke.useDelimiter(":");
                String part;
                int b=0;
                int param[] = new int[9];
                while (toke.hasNext())
                {

                    part = toke.next();
                    if (part.startsWith("["))
                    {
                        Scanner toke1 = new Scanner(part.substring(cp+1));
                        toke1.useDelimiter("]");
                        String coords = toke1.next();
                        Scanner toke2 = new Scanner(coords);
                        toke2.useDelimiter(",");
                        x = toke2.nextInt();
                        y = toke2.nextInt();
                        z = toke2.nextInt();
                        //System.out.println("moving to "+x+":"+y+":"+z);
                    } else if (part.startsWith("N"))
                    {
                        int n = new Integer(part.substring(1)).intValue();
                        for (int i=0;i<n;i++)
                        {
                            Block block = Block.newBlockWithParams(b,param);
                            world.addBlock(block, (chunkx<<4)+x, (chunky<<4)+y, (chunkz<<4)+z);
                            x++;
                        }
                    } else if (part.startsWith(".") )
                    {
                        Block block = Block.newBlockWithParams(b,param);
                        world.addBlock(block, (chunkx<<4)+x, (chunky<<4)+y, (chunkz<<4)+z);
                        x++;
                        if (part.equals(".."))
                        {
                            block = Block.newBlockWithParams(b,param);
                            world.addBlock(block, (chunkx<<4)+x, (chunky<<4)+y, (chunkz<<4)+z);
                            x++;
                        }
                    }
                    else
                    {
                        Scanner toke1 = new Scanner(part);
                        toke1.useDelimiter(",");
                        b = toke1.nextInt();
                        param = new int[9];
                        for (int i=0; toke1.hasNext();i++)
                        {
                            param[i] = toke1.nextInt();
                        }
                        Block block = Block.newBlockWithParams(b,param);
                        world.addBlock(block, (chunkx<<4)+x, (chunky<<4)+y, (chunkz<<4)+z);
                        x++;
                    }
                }
                x=0;
                z++;
                if (z>15)
                {
                    z = 0;
                    y++;
                }
            }
        }
        input.close();
    }
    
    private void renderAllBlocks(BulletAppState bulletAppState)
    {
        world.renderAll(levelNode, bulletAppState);
    }
    
    public Block getBlock(int x, int y, int z)
    {
        return world.getBlock(x, y, z);
    }
    
    public IBlockAccess getWorld() {
        return world;
    }
    public void addBlock(Block block, int x, int y, int z)
    {
            world.addBlock(block, x, y, z);
    }
    
    public void update(int x, int y, int z, BulletAppState bulletAppState)
    {
        world.update(levelNode, x, y, z, bulletAppState);
    }
    
    public void destroy(BulletAppState bulletAppState)
    {
        levelObjects.destroy(levelNode);
        world.destroy(bulletAppState);
    }
      
    public LevelEntryExit findEntryExit(int x, int y, int z)
    {
        return levelControl.findEntryExit(x, y, z);
    }

    public Obj getObject(String name)
    {
        return levelObjects.getObjectByName(name);
    }
    
    public Obj getObject(int id)
    {
        if (levelObjects==null) return null;
        return levelObjects.getObj(id);
    }
    
    public void saveLevel() {
        try {
            saveLevelMap();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveLevelMap() throws Exception 
    {
        Chunk chunk;
        String filename = "data/" + mapName + ".lvl";
        
        BufferedWriter out = new BufferedWriter(new FileWriter(filename));
        world.iterateChunks();
        
        while ((chunk = world.getNextChunk()) != null)
        {
            int cx = chunk.cx;
            int cy = chunk.cy;
            int cz = chunk.cz;
            //System.out.println("saving chunk "+cx+":"+cy+":"+cz);
            out.write("C"+cx+","+cy+","+cz+"\n");
            boolean wasAir = false;

            for (int y=0; y<16; y++)
            {
                for (int z=0; z<16; z++)
                {
                    StringBuilder blockLine = new StringBuilder();
                    String prev = "";
                    int numSame = 0;
                    String blockInfo="";
                    
                    for (int x=0; x<16; x++)
                    {
                        Block block = chunk.getBlock(x,y,z);
                        if (block == null || block.isAir())
                        {
                            wasAir = true;
                        }
                        else
                        {
                            blockInfo = block.getInfo();
                            if (wasAir)
                            {
                                numSame = repeat(blockLine,numSame);
                                blockLine.append('[').append(x).append(',').append(y).append(',').append(z).append("]:");
                            }
                            wasAir = false;
                            if (prev.equals(blockInfo))
                            {
                                //blockLine.append('.');
                                numSame++;
                            } 
                            else
                            {
                                numSame = repeat(blockLine,numSame);
                                blockLine.append(blockInfo);
                            }
                        }
                        prev = blockInfo;
                        //System.out.println("["+x+","+y+","+z+"]"+blockLine);
                        if (x==15) 
                            numSame = repeat(blockLine,numSame);
                    }
                    if (blockLine.length()!=0)
                        out.write(blockLine+"\n");
                }
                //out.write("\n");
            }
        }
        out.close();
    }
    
    private int repeat(StringBuilder blockLine, int numSame)
    {
        if (numSame>0)
        {
            if (numSame==1) blockLine.append('.');
            if (numSame==2) blockLine.append("..");
            //if (numSame==3) blockLine.append("...");
            if (numSame>2) blockLine.append('N').append(numSame);
            blockLine.append(':');
        }
        return 0;      
    }
    
    public void importLevel() {
        world = new World();
        try {
            importMap();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void importMap() throws Exception {
        int version;
        String line;
        int top = 0, bottom = 0, left = 0, right = 0;
        int light, monst, level = 1;

        String filename = "data/" + mapName + ".map";
        BufferedReader input = new BufferedReader(new FileReader(filename));
        //Header
        while (!(line = input.readLine()).equals("-")) {
            Scanner toke = new Scanner(line);
            toke.useDelimiter("=");
            String key = toke.next();
            if (key.equals("version")) {
                version = toke.nextInt();
            }
        }
        //Section 
        //Section header
        do {
            while (((line = input.readLine()) != null)
                    && !(line.equals("-"))) {
                Scanner toke = new Scanner(line);
                toke.useDelimiter("=");
                String key = toke.next();
                if (key.equals("top")) {
                    top = toke.nextInt();
                } else if (key.equals("bottom")) {
                    bottom = toke.nextInt();
                } else if (key.equals("left")) {
                    left = toke.nextInt();
                } else if (key.equals("right")) {
                    right = toke.nextInt();
                } else if (key.equals("light")) {
                    light = toke.nextInt();
                } else if (key.equals("monst")) {
                    monst = toke.nextInt();
                } else if (key.equals("level")) {
                    level = toke.nextInt();
                }
            }
            int zz = top;
            while ((line = input.readLine()) != null && line.length()>0) {
                int s = 0;
                for (int x = left; x <= right; x++) {
                    char un = line.charAt(s++);
                    setBlockFromChar(un, x, 8+level*4-4, zz);
                }
                zz++;
            }

        } while (line != null);

        input.close();
    }
    
    public static final String CHAR_TO_UNIT_TYPE = "$ o.t)(UzX#\\/mSbB[]<GZ@T%+'*|YMn~^:?!=-WxOFHV_>&hApDIdlLfEPrQ,`v\"";

    public void setBlockFromChar(char c, int x, int y, int z) 
    {
        Block block;
        if (c == ' ') {
            block = new Block(BlockType.wood.id);
            if(r.nextFloat()<0.1f) {
                int b = r.nextInt(3);
                if (b==0) block = new Block(BlockType.woodblood.id);
                if (b==1) block = new Block(BlockType.woodooze.id);
                if (b==2) block = new Block(BlockType.woodknot.id);
            }
            world.addBlock(block, x, y, z);
        } else if (c == '=') {
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
        } else if (c == 'u') {
            block = new BlockTrapDoor(BlockType.trapdoor.id);
            world.addBlock(block, x, y, z);
        } else if (c == '\'') {
            if (r.nextFloat()<0.1f) {
                int plant = r.nextInt(3);
                block = new Block(BlockType.flower.id);
                if (plant==1) block = new Block(BlockType.bush.id);
                if (plant==2) block = new Block(BlockType.longgrass.id);
                world.addBlock(block, x, y+1, z);        
            }
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
        } else if (c == 'F') {
            block = new Block(BlockType.fence.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
        } else if (c == '#') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);

            int wallid = BlockType.wall.id;
            if (r.nextFloat()>0.8f) {
                int w = r.nextInt()%3;
                if (w==0) wallid = BlockType.wallslime.id;
                if (w==1) wallid = BlockType.wallhole.id;
                if (w==2) wallid = BlockType.wallgrate.id;
            }
            block = new Block(wallid);
            world.addBlock(block, x, y+1, z);

            wallid = BlockType.wall.id;
            if (r.nextFloat()>0.8f) {
                int w = r.nextInt()%2;
                if (w==0) wallid = BlockType.wallmanacle.id;
                if (w==1) wallid = BlockType.wallslime.id;
            }
            block = new Block(wallid);
            world.addBlock(block, x, y+2, z);

            wallid = BlockType.wall.id;
            if (r.nextFloat()>0.8f) {
                int w = r.nextInt()%2;
                if (w==0) wallid = BlockType.wallslime.id;
                if (w==1) wallid = BlockType.wallroot.id;
            }
            block = new Block(wallid);
            world.addBlock(block, x, y+3, z);
        } else if (c == 'S') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.secretdoor.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.secretdoor.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == 'X') {
            //pit
        } else if (c == 'x') {
            block = new Block(BlockType.secretpit.id);
            world.addBlock(block, x, y, z);
        } else if (c == 't') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.secrettele.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'z') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.secretff.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == '|') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            if (r.nextFloat()<0.5f) {
                block = new Block(BlockType.trunk.id);
                world.addBlock(block, x, y+1, z);
                block = new Block(BlockType.tree.id);
                world.addBlock(block, x, y+2, z);
            }
        } else if (c == 'Y') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.trunk.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.ytree.id);
            world.addBlock(block, x, y+2, z);
        } else if (c == '~') {
            block = new Block(BlockType.water.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.water.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
        } else if (c == '-') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.water.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.water.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.bridge.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'W') {
            block = new Block(BlockType.water.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.water.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
        } else if (c == '^') {
            block = new Block(BlockType.deepwater.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.deepwater.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.deepwater.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-3, z);
        } else if (c == '*') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.bush.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'M') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.rock.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.rock.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.rock.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.rock.id);
            world.addBlock(block, x, y+2, z);
            if (r.nextFloat()<0.8f) {
                block = new Block(BlockType.rock.id);
                world.addBlock(block, x, y+3, z);        
                if (r.nextFloat()<0.2f) {
                    block = new Block(BlockType.rock.id);
                    world.addBlock(block, x, y+4, z);        
                }
            }
        } else if (c == 'O') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.rock.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.rock.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.rock.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.rock.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.rock.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == 'n') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y+1, z);
            if (r.nextFloat()<0.1f) {
                block = new Block(BlockType.grass.id);
                world.addBlock(block, x, y+2, z);        
            }        
        } else if (c == '?') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.signpost.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.sign.id);
            world.addBlock(block, x, y+2, z);
        } else if (c == 'Q') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.stone.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.stone.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.stone.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == '+') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.glass.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == 'P') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.picture.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == 'm') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.message.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == 'f') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.fireplace.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == 'E') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.bookcase.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == 'B') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new BlockButton(BlockType.button.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'b') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.secretbutton.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == '/' || c == '\\') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new BlockLever(BlockType.lever.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'V') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.village.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.entrancetop.id);
            world.addBlock(block, x, y+2, z);
            //block = new Block(BlockType.roof.id);
            //world.addBlock(block, x, y+2, z);
        } else if (c == 'H') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.house.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.roofwedge.id);
            world.addBlock(block, x, y+2, z);
        } else if (c == '&') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.castle.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.castletop.id);
            world.addBlock(block, x, y+2, z);
        } else if (c == ':') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
        } else if (c == '!') {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.crops.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == '@') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.exit.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.entrancetop.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == '[' || c == '_') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new BlockDoor(BlockType.doorknob.id);
            block.setDirection(Direction.south);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == ']' || c == '<') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new BlockDoorLocked(BlockType.doorlock.id);
            world.addBlock(block, x, y+1, z);
            //block = new Block(BlockType.doortop.id);
            //world.addBlock(block,x,2,z);                   
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == 'G' || c == '>') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new BlockGate(BlockType.gate.id);
            world.addBlock(block, x, y+1, z);
            //block = new Block(BlockType.gate.id);
            //world.addBlock(block,x,2,z);                   
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else if (c == 'U') {
            block = new BlockTrapDoor(BlockType.trapdoor.id);
            world.addBlock(block, x, y, z);
        } else if (c == 'r') {
            block = new Block(BlockType.rug.id);
            world.addBlock(block, x, y, z);
        } else if (c == 'l') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.talllamp.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.lamptop.id);
            world.addBlock(block, x, y+2, z);
        } else if (c == 'p') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.plant.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == '%') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.ladder.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.ladder.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.ladder.id);
            world.addBlock(block, x, y+3, z);
            block = new Block(BlockType.ladder.id);
            world.addBlock(block, x, y+4, z);
        } else if (c == 'Z') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new BlockAnim(BlockType.forcefield.id);
            world.addBlock(block, x, y+1, z);
            block = new BlockAnim(BlockType.forcefield.id);
            world.addBlock(block, x, y+2, z);
        } else if (c == 'T') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new BlockAnim(BlockType.teleport.id);
            world.addBlock(block, x, y+1, z);
            block = new BlockAnim(BlockType.teleport.id);
            world.addBlock(block, x, y+2, z);
        } else if (c == 'o') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.presspad.id);
            block.setDirection(Direction.east);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'A') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.table.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'h') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.chair.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'L') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.bedhead.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'e') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.bedbottom.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'I') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.wardrobe.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'd') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.drawers.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == 'D') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.barrel.id);
            world.addBlock(block, x, y+1, z);
        } else if (c == '.') {
            block = new Block(BlockType.secretpp.id);
            world.addBlock(block, x, y, z);
        } else if (c == '$') {
            block = new Block(BlockType.wood.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.portal.id);
            world.addBlock(block, x, y+1, z);
            block = new Block(BlockType.portal.id);
            world.addBlock(block, x, y+2, z);
            block = new Block(BlockType.wall.id);
            world.addBlock(block, x, y+3, z);
        } else {
            block = new Block(BlockType.sand.id);
            world.addBlock(block, x, y-2, z);
            block = new Block(BlockType.dirt.id);
            world.addBlock(block, x, y-1, z);
            block = new Block(BlockType.grass.id);
            world.addBlock(block, x, y, z);
            block = new Block(BlockType.torch.id);
            world.addBlock(block, x, y+1, z);
        }
    }
    
}
