package mindmelt;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mindmelt extends SimpleApplication 
{
    private BulletAppState bulletAppState;
    private Random r = new Random(System.currentTimeMillis());
    private static final Logger logger = Logger.getLogger("");
    private static final int FLY = 0;
    private static final int FOLLOW = 1;
    private static final int FIRST_PERSON = 2;
    private static final int FLY_MAX = 3;
    private static Mindmelt app;
    private BitmapText txt;

    private LevelMap level;
    Runtime runtime;
    static int count = 0;
    TextureMats textureMats;
    int currentBlock = 1;
    boolean controlKey = false;
    boolean shiftKey = false;
    boolean altKey = false;
    String levelName = "world";
    Vector3f collisionPoint = new Vector3f(Vector3f.ZERO);
    private CharacterControl player;
    private boolean onLadder = false;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false, forward = false, back = false;
    int flyMode = FLY;
    boolean waterWalk = false;
    boolean ffWalk = false;
    boolean blockMode=true;
    boolean changeCollision = false;
    private Nifty nifty;
    private ObjStore gameObjects;

    int lx = 0;
    int ly = 0;
    int lz = 0;

    public static void main(String[] args) {
        app = new Mindmelt();
        logger.setLevel(Level.OFF);
        app.start();

    }
    int last1x=0;
    int last1y=0;
    int last1z=0;
    int last2x=0;
    int last2y=0;
    int last2z=0;
    Block lastBlock=null;
    
    @Override
    public void handleError(java.lang.String errMsg, java.lang.Throwable t)
    {
        System.out.println(errMsg);
        t.printStackTrace();
    }

    @Override
    public void simpleInitApp() 
    {
        int b[] = {2, 4, 5};
        float SC = SubRender.SC;
        /* NIFTY */
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                          inputManager,
                                                          audioRenderer,
                                                          guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/Mindmelt.xml", "start");
        //guiViewPort.addProcessor(niftyDisplay);
        
        setUpKeys();
        initCrossHairs();
        
        textureMats = new TextureMats(assetManager);
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);
        
        runtime = Runtime.getRuntime();
        runtime.gc();
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Initial memory is bytes: " + memory);

        
        long time = System.currentTimeMillis();
        gameObjects = new ObjStore("game");
        //Load global game data e.g. Objects
        loadGame(gameObjects);
        
        level = new LevelMap(levelName);
        // Import the map from a file
        //expandLevel("aporto", 2);
        ////Block block = Block.newBlock(BlockType.wood.id);
        ////world.addBlock(block, 0, 0, 0);
        level.loadLevel(bulletAppState, rootNode, gameObjects);
        //level.importLevel();
        System.out.println("Import Took " + (System.currentTimeMillis() - time) + " ms");

        runtime.gc();
        memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("After importing memory is bytes: " + memory);

        time = System.currentTimeMillis();
        System.out.println("Rendering Took " + (System.currentTimeMillis() - time) + " ms");

        flyCam.setMoveSpeed(8f*SC);
        viewPort.setBackgroundColor(ColorRGBA.Cyan);

        runtime.gc();
        memory = runtime.totalMemory() - runtime.freeMemory();;
        System.out.println("Total memory is bytes: " + memory);

        BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
        txt = new BitmapText(fnt, false);
        txt.setBox(new Rectangle(0, 0, settings.getWidth(), settings.getHeight()));
        txt.setSize(fnt.getPreferredSize());
        txt.setLocalTranslation(0, txt.getHeight(), 0);
        guiNode.attachChild(txt);
        
        //Water
        /*
        FilterPostProcessor proc = new FilterPostProcessor(assetManager);
        Vector3f vec =  Vector3f.UNIT_Y.add(new Vector3f(0.21f,0,0.32f));
        WaterFilter waterFilter = new WaterFilter(worldNode,vec);
        waterFilter.setWaterHeight(8.5f*SC);
        waterFilter.setMaxAmplitude(0f);
        proc.addFilter(waterFilter);
        viewPort.addProcessor(proc);
        */
        inputManager.setCursorVisible(true);

        initPlayer();
    }

    @Override
    public void simpleUpdate(float tpf) 
    {
        float sc = SubRender.SC;
        float playerSpeed = sc/8f;
        Vector3f coords = cam.getLocation();
        int x = (int) ((coords.x + .0001) / sc);
        int y = (int) ((coords.y + .0001) / sc);
        int z = (int) ((coords.z + .0001) / sc);
        
        Obj playerObj = level.getObject(0);
        if (playerObj==null) return;
        playerObj.moveTo(coords.x/sc, (coords.y-sc*2f)/sc, coords.z/sc);

        String fly = (flyMode==FLY) ? "FLY" : (flyMode==FOLLOW) ? "FOLLOW" : "FIRST_PERSON";
        String ww = (waterWalk==true) ? "ON" : "OFF";
        String ff = (ffWalk==true) ? "ON" : "OFF";
        String blkmd = (blockMode==true) ? "BLOCK" : "CHAR";
        Vector3f camDir = cam.getDirection().clone().multLocal(playerSpeed).setY(0);
        Vector3f camLeft = cam.getLeft().clone().multLocal(playerSpeed).setY(0);
        
        Block be = level.getBlock(x, y, z);
        Block bl = level.getBlock(x, y-1, z);
        Block bf = level.getBlock(x, y-2, z);
        String blockEye = (be!=null) ? BlockType.name[be.getBlockType()] : "null";
        String blockLeg = (bl!=null) ? BlockType.name[bl.getBlockType()] : "null";
        String blockFeet = (bf!=null) ? BlockType.name[bf.getBlockType()] : "null";
        int beid = (be==null) ? 0 : be.id ;
        int blid = (bl==null) ? 0 : bl.id;
        int bfid = (bf==null) ? 0 : bf.id;
        String block_name;
        if (blockMode)
            block_name = BlockType.name[currentBlock];
        else
            block_name = "["+level.CHAR_TO_UNIT_TYPE.charAt(currentBlock)+"]";
        txt.setText(x + ":" + y + ":" + z + " " + block_name+ " "+collisionPoint+" fly "+fly+" water "+ww+" ff "+ff+" bm "+blkmd+" "+blockEye+" "+blockLeg+" "+blockFeet);
        
        if ( (blid == BlockType.ladder.id) || (bfid == BlockType.ladder.id) )
        {
            onLadder = true;
        } else {
            onLadder = false;
        }
        
        if (player.onGround())
        {
            walkDirection.set(0, 0, 0);
            if (left)  { walkDirection.addLocal(camLeft); }
            if (right) { walkDirection.addLocal(camLeft.negate()); }
            if (forward) { walkDirection.addLocal(camDir); }
            if (back)  { walkDirection.addLocal(camDir.negate()); }
        }    

        boolean ladderMove = false;
        if (onLadder) {
            //player.setEnabled(false);
            Vector3f pos = cam.getLocation();
            if (up && beid == BlockType.ladder.id) {
                pos.addLocal(Vector3f.UNIT_Y.mult(speed*tpf));
                cam.setLocation(pos);
                ladderMove = true;
            }
            if (down && bfid == BlockType.ladder.id) 
            {
                pos.addLocal(Vector3f.UNIT_Y.mult(-speed*tpf));
                cam.setLocation(pos);
                ladderMove = true;
            }
        }
        if (!ladderMove)
            player.setWalkDirection(walkDirection);
        
        if (changeCollision) 
        {
            bulletAppState.getPhysicsSpace().remove(player);
            if (waterWalk==true)
            {
                player.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
            } else {                
                player.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
            }
            if (ffWalk==false)
            {
                player.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
            } else {
                player.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
            }
            bulletAppState.getPhysicsSpace().add(player);
            changeCollision = false;
        }
        
        int dirX = (int)Math.signum(camDir.getX());
        int dirY = (int)Math.signum(camDir.getZ());

        if (flyMode==FLY)
            player.setPhysicsLocation(cam.getLocation());
        else if (flyMode==FOLLOW)
            cam.setLocation(player.getPhysicsLocation().add(camDir.mult(-SubRender.SC*4f)).add(Vector3f.UNIT_Y.mult(sc*0.6f)));
        else
            if (onLadder) {
                player.setPhysicsLocation(cam.getLocation().add(Vector3f.UNIT_Y.mult(-sc*0.6f)));
            } else {
                cam.setLocation(player.getPhysicsLocation().add(Vector3f.UNIT_Y.mult(sc*0.6f)));
            }

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    void initPlayer()
    {
        float sc = SubRender.SC;
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(sc/3f,sc, 1);
        //bulletAppState.getPhysicsSpace().remove(player);
        //SphereCollisionShape sphereShape = new SphereCollisionShape(2f);
        //capsuleShape.setMargin(1f);
        player = new CharacterControl(capsuleShape,sc/8f );
        if (levelName.equals("world")) 
            cam.setLocation(new Vector3f(118*sc, 10*sc, 115*sc));
        else
            cam.setLocation(new Vector3f(4*sc, 10*sc, 4*sc));            
        player.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        player.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        player.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        player.setJumpSpeed(sc*6);
        player.setFallSpeed(sc*8);
        player.setGravity(100);
        player.setMaxSlope(FastMath.PI/3.3f);
        player.setPhysicsLocation(cam.getLocation());
        Node playerNode = new Node("Player");
        Spatial ds = player.createDebugShape(assetManager);
        ds.setName("player");
        playerNode.attachChild(ds);
        playerNode.addControl(player);
        bulletAppState.getPhysicsSpace().add(playerNode);

        float ang[] = {0,FastMath.QUARTER_PI,0};
        cam.setRotation(new Quaternion(ang));
        //System.out.println(player);
        rootNode.attachChild(playerNode);        
    }
    
    private void setUpKeys() {
        inputManager.addMapping("<", new KeyTrigger(KeyInput.KEY_COMMA));
        inputManager.addMapping(">", new KeyTrigger(KeyInput.KEY_PERIOD));
        inputManager.addMapping("import", new KeyTrigger(KeyInput.KEY_F1));
        inputManager.addMapping("load", new KeyTrigger(KeyInput.KEY_F2));
        inputManager.addMapping("save", new KeyTrigger(KeyInput.KEY_F3));
        inputManager.addMapping("ctrl", new KeyTrigger(KeyInput.KEY_LCONTROL));
        inputManager.addMapping("shift", new KeyTrigger(KeyInput.KEY_LSHIFT));
        inputManager.addMapping("alt", new KeyTrigger(KeyInput.KEY_LMENU));
        inputManager.addMapping("ClickL", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("ClickR", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Fly", new KeyTrigger(KeyInput.KEY_1));
        inputManager.addMapping("Water", new KeyTrigger(KeyInput.KEY_2));
        inputManager.addMapping("Forcefield", new KeyTrigger(KeyInput.KEY_3));
        inputManager.addMapping("Blockmode", new KeyTrigger(KeyInput.KEY_4));
        inputManager.addMapping("Fill", new KeyTrigger(KeyInput.KEY_INSERT));
        inputManager.addMapping("Delete", new KeyTrigger(KeyInput.KEY_DELETE));
        
        inputManager.addListener(actionListener, "<");
        inputManager.addListener(actionListener, ">");
        inputManager.addListener(actionListener, "import");
        inputManager.addListener(actionListener, "load");
        inputManager.addListener(actionListener, "save");
        inputManager.addListener(actionListener, "ctrl");
        inputManager.addListener(actionListener, "shift");
        inputManager.addListener(actionListener, "alt");
        inputManager.addListener(actionListener, "ClickR");
        inputManager.addListener(actionListener, "ClickL");
        inputManager.addListener(actionListener, "Left");
        inputManager.addListener(actionListener, "Right");
        inputManager.addListener(actionListener, "Forward");
        inputManager.addListener(actionListener, "Back");
        inputManager.addListener(actionListener, "Up");
        inputManager.addListener(actionListener, "Down");
        inputManager.addListener(actionListener, "Jump");
        inputManager.addListener(actionListener, "Fly");
        inputManager.addListener(actionListener, "Water");
        inputManager.addListener(actionListener, "Forcefield");
        inputManager.addListener(actionListener, "Blockmode");
        inputManager.addListener(actionListener, "Fill");
        inputManager.addListener(actionListener, "Delete");
    }
    /**
     * These are our custom actions triggered by key presses. We do not walk
     * yet, we just keep track of the direction the user pressed.
     */
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String binding, boolean value, float tpf) {
            if (binding.equals("Left")) {
              if (value) { left = true; } else { left = false; }
            } else if (binding.equals("Right")) {
              if (value) { right = true; } else { right = false; }
            } else if (binding.equals("Forward")) {
              if (value) { forward = true; } else { forward = false; }
            } else if (binding.equals("Back")) {
              if (value) { back = true; } else { back = false; }
            } else if (binding.equals("Up")) {
              if (value) { up = true; } else { up = false; }
            } else if (binding.equals("Down")) {
              if (value) { down = true; } else { down = false; }
            } else if (binding.equals("Jump")) {
              player.jump();
            } else if (binding.equals("Fly")) {
              if (value) {
                  flyMode++;
                  flyMode %= FLY_MAX;
              }
            } else if (binding.equals("Water")) {
              if (value) {waterWalk = !waterWalk; changeCollision=true;}
            } else if (binding.equals("Forcefield")) {
              if (value) {ffWalk = !ffWalk; changeCollision=true;}
            } else if (binding.equals("Blockmode")) {
              if (value) {blockMode = !blockMode; currentBlock=1;}
            } else if (binding.equals("ClickL")) {
                if (value) {
                    activate(true);
                }
            } else if (binding.equals("ClickR")) {
                if (value) {
                    activate(false);
                }
            } else if (binding.equals("<")) {
                if (value) {
                    changeBlock(-1);
                }
            } else if (binding.equals(">")) {
                if (value) {
                    changeBlock(1);
                }
            } else if ( ( binding.equals("Fill")|| binding.equals("Delete") ) && value) {
                int sx = last1x;
                int sy = last1y;
                int sz = last1z;
                int ex = last2x;
                int ey = last2y;
                int ez = last2z;
                if (last1x>last2x) {int t=sx;sx=ex;ex=t;}
                if (last1y>last2y) {int t=sy;sy=ey;ey=t;}
                if (last1z>last2z) {int t=sz;sz=ez;ez=t;}
                System.out.println(sx+" "+sy+" "+sz);
                System.out.println(ex+" "+ey+" "+ez);
                for (int x=sx;x<=ex;x++) {
                    for (int y=sy;y<=ey;y++) {
                        for (int z=sz;z<=ez;z++) {
                            Block block = new Block(BlockType.air.id);
                            if (binding.equals("Fill")) {
                                int params[] = {lastBlock.direction,1};
                                block = Block.newBlockWithParams(lastBlock.id,params);
                            }
                            level.addBlock(block, x, y, z);
                            if (z%16==0) level.update(x,y,z, bulletAppState);
                        }
                        if (y%16==0) level.update(x,y,sz, bulletAppState);
                    }
                    if (x%16==0) level.update(x,sy,sz, bulletAppState);
                }
                level.update(sx,sy,sz, bulletAppState);
                level.update(ex,ey,ez, bulletAppState);
           } else if (binding.equals("import") && value) {
                level.destroy(bulletAppState);
                //rootNode.detachAllChildren();
                level.importLevel();

            } else if (binding.equals("load") && value) {
                load(levelName);
                
            } else if (binding.equals("save") && value) {
                level.saveLevel();
                
            } else if (binding.equals("ctrl")) {
                controlKey = value;
                
            } else if (binding.equals("shift")) {
                shiftKey = value;
                
             } else if (binding.equals("alt")) {
                altKey = value;
                
            }
        }
    };

    private void load(String filename)
    {
        level.destroy(bulletAppState);
        //rootNode.detachAllChildren();

        level = new LevelMap(filename);
        level.loadLevel(bulletAppState, rootNode, gameObjects);
        levelName = filename;
    }
    
    private void changeBlock(int state) {
        currentBlock += state;
        if (blockMode)
        {
            if (currentBlock < 1) {
                currentBlock = BlockType.maxBlock;
            }
            if (currentBlock > BlockType.maxBlock) {
                currentBlock = 1;
            }
        } else {
            if (currentBlock < 1) {
                currentBlock = level.CHAR_TO_UNIT_TYPE.length()-1;
            }
            if (currentBlock > level.CHAR_TO_UNIT_TYPE.length())
            {
                currentBlock = 1;
            }
        }
    }

    private void activate(boolean state) {
        float SC = SubRender.SC;
        long timenow = System.currentTimeMillis();

        CollisionResults results = new CollisionResults();
        // 2. Aim the ray from cam loc to cam direction.

        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Collect intersections between Ray and Shootables in results list.
        rootNode.collideWith(ray, results);
        CollisionResult result = null;
        for (int i = 0; i < results.size(); i++) 
        {
            if (!results.getCollision(i).getGeometry().getName().equals("player"))
            {
                result = results.getCollision(i);
                break;
            }
        }
        
        if (result==null) return;
        collisionPoint = result.getContactPoint().divide(SC);

        // 4. Print the results
        System.out.println("----- Collisions? " + results.size() + "-----");
        for (int i = 0; i < results.size(); i++) 
        {
            // For each hit, we know distance, impact point, name of geometry.
            float dist = results.getCollision(i).getDistance();
            Vector3f pt = results.getCollision(i).getContactPoint().divideLocal(SC);
            String hit = results.getCollision(i).getGeometry().getName();
            Vector3f norm = results.getCollision(i).getContactNormal();
            System.out.println("* Collision #" + i + " at " + hit + " " +dist+ " far, point " + pt.toString()+", normal "+norm.toString());
        }
        
        Vector3f coords = result.getContactPoint();
        System.out.println("You are at " + cam.toString());
        System.out.println("Nearest at " + coords.toString());
        System.out.println("Diff " + (cam.getLocation().divide(SC).subtract(coords)).toString());
        String name = result.getGeometry().getName();
        System.out.println("Name "+name);
        int id = 0;
        if (name.startsWith("obj:")) {
             id = new Integer(name.substring(4)).intValue();
        }
            
        System.out.println("ALT "+altKey+" CTL "+controlKey+" SHFT "+shiftKey);
        if (!altKey && !controlKey && !shiftKey && (name.startsWith("door:") || name.startsWith("lock:") || name.startsWith("gate:"))) {
            DoorControl doorControl = result.getGeometry().getParent().getParent().getControl(DoorControl.class);
            if (doorControl == null) {
                return;
            }
            BlockDoor door = doorControl.getDoor();
            if (door.isOpen()) {
                door.close();
            } else if (door.isClosed()) {
                door.open();
            }
            return;
        }
        if (!altKey && !controlKey && !shiftKey && (name.startsWith("trap:"))) {
            TrapDoorControl doorControl = result.getGeometry().getParent().getParent().getControl(TrapDoorControl.class);
            if (doorControl == null) {
                return;
            }
            BlockTrapDoor door = doorControl.getDoor();
            if (door.isOpen()) {
                door.close();
            } else if (door.isClosed()) {
                door.open();
            }
            return;
        }
        if (!altKey && !controlKey && !shiftKey && (name.startsWith("lever:"))) {
            LeverControl leverControl = result.getGeometry().getParent().getParent().getControl(LeverControl.class);
            if (leverControl == null) {
                return;
            }
            BlockLever lever = leverControl.getLever();
            if (lever.isOff()) {
                lever.turnOn();
            } else if (lever.isOn()) {
                lever.turnOff();
            }
            return;
        }
        if (!altKey && !controlKey && !shiftKey && (name.startsWith("button:"))) {
            ButtonControl buttonControl = result.getGeometry().getParent().getParent().getControl(ButtonControl.class);
            if (buttonControl == null) {
                return;
            }
            BlockButton button = buttonControl.getButton();
            if (button.isOff()) {
                button.turnOn();
            } else if (button.isOn()) {
                button.turnOff();
            }
            return;
        }

        if (!state && !controlKey && !shiftKey && (name.startsWith("obj:"))) {
            Obj ob = level.getObject(id);
            Vector3f norm = result.getContactNormal();
            ob.moveBy(norm.x/16,norm.y/16,norm.z/16);
            return;
        }               
        if (state && !controlKey && !shiftKey && (name.startsWith("obj:"))) {
            Obj ob = level.getObject(id);
            Vector3f norm = result.getContactNormal();
            ob.moveBy(-norm.x/16,-norm.y/16,-norm.z/16);
            return;
        }
        if (!state && controlKey && !shiftKey && (name.startsWith("obj:"))) {
            Obj ob = level.getObject(id);
            ob.rotateBy(1.0f);
            return;
        }          
        if (state && controlKey && !shiftKey && (name.startsWith("obj:"))) {
            Obj ob = level.getObject(id);
            ob.rotateBy(-1.0f);
            return;
        }  
        // Use normal to find side
        Vector3f norm = result.getContactNormal();
        float xn = norm.x;
        float yn = norm.y;
        float zn = norm.z;
        String SIDE = "???";
        if (xn>0.01) SIDE = "RGT";
        else if (xn<-0.01) SIDE = "LFT";
        else if (yn>0.01) SIDE = "TOP";
        else if (yn<-0.01) SIDE = "BOT";
        else if (zn>0.01) SIDE = "BAK";
        else if (zn<-0.01) SIDE = "FNT";
        else SIDE = "ANG";
        System.out.println("Norm = "+norm.toString()+" Side = "+SIDE);

        // NEW WAY      
        float off = 0.0f;
        Vector3f loc = cam.getLocation().divide(SC);
        float x1 = loc.getX() + off;
        float y1 = loc.getY() + off;
        float z1 = loc.getZ() + off;
        float x2 = coords.getX() + off;
        float y2 = coords.getY() + off;
        float z2 = coords.getZ() + off;
        System.out.println("x1,y1,z1 "+x1+","+y1+","+z1);
        System.out.println("x2,y2,z2 "+x2+","+y2+","+z2);
        
        float sc = 100000f;
        float r = 0.5f;
        x1 = (float)((long)(x1*sc+r)/sc);
        y1 = (float)((long)(y1*sc+r)/sc);
        z1 = (float)((long)(z1*sc+r)/sc);
        x2 = (float)((long)(x2*sc+r)/sc);
        y2 = (float)((long)(y2*sc+r)/sc);
        z2 = (float)((long)(z2*sc+r)/sc);
        System.out.println("x1,y1,z1 "+x1+","+y1+","+z1);
        System.out.println("x2,y2,z2 "+x2+","+y2+","+z2);
        
        float xf = (float)Math.floor(x2);
        float yf = (float)Math.floor(y2);
        float zf = (float)Math.floor(z2);
        float xc = (float)Math.ceil(x2);
        float yc = (float)Math.ceil(y2);
        float zc = (float)Math.ceil(z2);
        //System.out.println("xf,yf,zf "+xf+","+yf+","+zf);
        //System.out.println("xc,yc,zc "+xc+","+yc+","+zc);
        if (xf==xc) { if (x1>x2) xf-=1f; else xc+=1f; }
        if (yf==yc) { if (y1>y2) yf-=1f; else yc+=1f; }
        if (zf==zc) { if (z1>z2) zf-=1f; else zc+=1f; }
        System.out.println("xf,yf,zf "+xf+","+yf+","+zf);
        System.out.println("xc,yc,zc "+xc+","+yc+","+zc);
    
        int bx=(int)xf;
        int by=(int)yf;
        int bz=(int)zf;
        int ax=bx;
        int ay=by;
        int az=bz;
        
        float xb = (x1<=x2) ? xf : xc;
        float yb = (y1<=y2) ? yf : yc;
        float zb = (z1<=z2) ? zf : zc;
        System.out.println("xb,yb,zb "+xb+","+yb+","+zb);
        
        float xy = x2+(yb-y2)/(y1-y2)*(x1-x2);
        float xz = x2+(zb-z2)/(z1-z2)*(x1-x2);
        float yx = y2+(xb-x2)/(x1-x2)*(y1-y2);
        float yz = y2+(zb-z2)/(z1-z2)*(y1-y2);
        float zx = z2+(xb-x2)/(x1-x2)*(z1-z2);
        float zy = z2+(yb-y2)/(y1-y2)*(z1-z2);
        
    	System.out.println("xb,yx,zx "+xb+","+yx+","+zx);
    	System.out.println("xy,yb,zy "+xy+","+yb+","+zy);
    	System.out.println("xz,yz,zb "+xz+","+yz+","+zb);

        String side = "";
        float x=0;
        float y=0;
        float z=0;
        int dir = Direction.south;
        if (xb>=xf && xb<=xc && yx>=yf && yx<=yc && zx>=zf && zx<=zc)
        {
            x = xb;
            y = yx;
            z = zx;
            if (x1<=x2) 
            {
                side = "left";
                dir = Direction.west;
                ax=bx-1;
            } else {
                side = "right";
                dir = Direction.east;
                ax=bx+1;
            }
        }
        else if (xy>=xf && xy<=xc && yb>=yf && yb<=yc && zy>=zf && zy<=zc)
        {
            x = xy;
            y = yb;
            z = zy;
            if (y1<=y2) 
            { 
                side = "bottom";
                ay=by-1;
            } else {
                side = "top";
                ay=by+1;
            }
            if (x1>x2 && Math.abs(x1-x2)>Math.abs(z1-z2)) dir = Direction.east;
            if (x1<=x2 && Math.abs(x1-x2)>Math.abs(z1-z2)) dir = Direction.west;
            if (z1>z2 && Math.abs(x1-x2)<=Math.abs(z1-z2)) dir = Direction.south;
            if (z1<=z2 && Math.abs(x1-x2)<=Math.abs(z1-z2)) dir = Direction.north;
        }
        else if (xz>=xf && xz<=xc && yz>=yf && yz<=yc && zb>=zf && zb<=zc)
        {
           x = xz;
           y = yz;
           z = zb;
           if (z1<=z2) 
            { 
                side = "front";
                dir = Direction.north;
                az=bz-1;
            } else {
                side = "back";
                dir = Direction.south;
                az=bz+1;
            }
        }
        System.out.println("x,y,z "+x+","+y+","+z);
        System.out.println("side "+side+" "+dir);
        System.out.println("bx,by,bz "+bx+","+by+","+bz);
        System.out.println("ax,ay,az "+ax+","+ay+","+az);
        
        if (lx!=bx || ly!=by || lz!=bz)
        {
            lx = bx; ly = by; lz = bz;
        }
        
        if (!shiftKey && loadNextLevel(bx,by,bz))
            return;
        
        // Need to press shift key to place/remove blocks
        if (!shiftKey) 
            return;
        
        if (controlKey && !altKey) {
            Block b = level.getBlock(bx, by, bz);
            int d = b.getDirection();
            d += (state) ? 1 : -1;
            if (d < 0) {
                d += 4;
            }
            if (d >= 4) {
                d -= 4;
            }
            System.out.println("dir = "+d);
            b.setDirection(d);
        } else if (!controlKey && !altKey) {
            if (state) { // add block
                int params[] = {dir,1};
                if (blockMode)
                {
                    Block block = Block.newBlockWithParams(currentBlock,params);
                    level.addBlock(block, ax, ay, az);
                    last2x = last1x;
                    last2y = last1y;
                    last2z = last1z;
                    last1x = ax;
                    last1y = ay;
                    last1z = az;
                    lastBlock = block;
                }
                else
                {
                    char c = level.CHAR_TO_UNIT_TYPE.charAt(currentBlock);
                    level.setBlockFromChar(c, ax, ay, az);
                }    
            } else {// remove block
                Block block = new Block(BlockType.air.id);
                level.addBlock(block, bx, by, bz);
                last2x = last1x;
                last2y = last1y;
                last2z = last1z;
                last1x = bx;
                last1y = by;
                last1z = bz;
                lastBlock = block;
            }
        }
        if (altKey && !controlKey)
        {
            if (level.getBlock(bx, by+1, bz) != null && !level.getBlock(bx, by+1, bz).isAir()) 
                return;
            
            Block block;
            if (state) {
                //move up
                int iy = by;
                while (!(block = level.getBlock(bx, iy, bz)).isAir())
                {
                    level.addBlock(block, bx, iy+1, bz);
                    level.update(bx, iy+1, bz, bulletAppState);
                    iy--;
                }
                level.addBlock(block, bx, iy+1, bz);
                level.update(bx, iy+1, bz, bulletAppState);
            } else {
                //move down
                int iy = by;
                while ((block=level.getBlock(bx, iy, bz))!=null && !block.isAir())
                {
                    iy--;
                }
                iy++;
                while ((block=level.getBlock(bx, iy, bz))!=null && !block.isAir())
                {
                    level.addBlock(block, bx, iy-1, bz);
                    level.update(bx, iy-1, bz, bulletAppState);
                    iy++;
                }
                level.addBlock(block, bx, iy-1, bz);
                level.update(bx, iy-1, bz, bulletAppState);
            }
        }
        level.update( bx, by, bz, bulletAppState);
        System.out.println("Time " + (System.currentTimeMillis() - timenow));
        /*
         runtime.gc();
         long memory = runtime.totalMemory() - runtime.freeMemory();
         System.out.println("Used memory is bytes: " + memory);
         */
    }

    private void initCrossHairs() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+");        // fake crosshairs :)
        ch.setLocalTranslation( // center
        settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 3 * 2,
        settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }
    
    private boolean loadNextLevel(int x, int y, int z)
    {
        LevelEntryExit levelEntryExit = level.findEntryExit(x,y,z);
        
        if (levelEntryExit==null)
            return false;
        
        float to_x = levelEntryExit.getToX()+0.5f;
        float to_y = levelEntryExit.getToY()+0.5f;
        float to_z = levelEntryExit.getToZ()+0.5f;
        int dir = levelEntryExit.getDir();
        String filename = levelEntryExit.getFilename();
        System.out.println("From "+levelName+" To "+filename);
        if (!filename.equals(levelName))
            load(filename);
        
        float sc = SubRender.SC;
        cam.setLocation(new Vector3f(to_x*sc, to_y*sc, to_z*sc));
        float a[] = {FastMath.PI,FastMath.HALF_PI,0,-FastMath.HALF_PI};
        if (dir!=Direction.none) {
            float ang[] = {0,a[dir],0};
            cam.setRotation(new Quaternion(ang));
        }
        player.setPhysicsLocation(cam.getLocation());
        return true;
    }
        
    
    private void expandLevel(String mapName, int size) {
        try {
            expandMap(mapName, size);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void expandMap(String mapName, int size) throws Exception {
        int version;
        String line;
        int top = 0, bottom = 0, left = 0, right = 0;
        int light, monst, level;

        String filename = "data/" + mapName + ".map";
        String filename2 = "data/" + mapName + "X.map";
        BufferedReader input = new BufferedReader(new FileReader(filename));
        BufferedWriter output = new BufferedWriter(new FileWriter(filename2));
        //Header
        while (!(line = input.readLine()).equals("-")) {
            output.write(line+"\n");
            
        }
        output.write("-\n");
        //Section 
        //Section header
        do {
            while (((line = input.readLine()) != null)
                    && !(line.equals("-"))) {
                output.write(line+"\n");
           }
            output.write("-\n");
            while ((line = input.readLine()) != null) {
                for (int i=0; i<size; i++)
                {
                    for (int x = 0; x<line.length(); x++) {
                        for (int s=0;s<size;s++)
                        {
                            output.write(line.charAt(x));
                        }
                    }
                    output.write('\n');
                }
            }

        } while (line != null);
        output.close();
        input.close();
    }

    
    private String loadGame(ObjStore objects) {
       try {
            loadObjects(objects);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
       return null;
    }
    
    private String loadObjects(ObjStore objects) throws Exception {            
        String line;
    
        String filename = "data/objects.dat";
        BufferedReader input = new BufferedReader(new FileReader(filename));
        
        while ((line = input.readLine())!=null) {
            if (line.startsWith("//"))
                continue;
            Scanner toke = new Scanner(line);
            toke.useDelimiter(",");
            int id = toke.nextInt();
            String name = toke.next();
            int type = toke.nextInt();
            int level = toke.nextInt();
            int x = toke.nextInt();
            int y = toke.nextInt();
            int z = toke.nextInt();
            // .... etc
            Obj obj = null;
            int subtype = ObjType.getObjSubtype(type);
            if (subtype==ObjType.OBJ) 
                obj = new Obj(id, type, name);
            else if (subtype==ObjType.PERSON)
                obj = new ObjPerson(id, type, name);
            else if (subtype==ObjType.MONSTER) 
                obj = new ObjMonster(id, type, name);
            else if (subtype==ObjType.PLAYER) 
                obj = new ObjPlayer(id, type, name);
            if (obj!=null) {
                obj.setLevel(level).moveTo(new Vec3i(x,y,z));
                objects.addObj(id, obj);
            }
        }
        input.close();
        return null;
    }
}