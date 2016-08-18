package mindmelt;

import java.util.Scanner;

public class LevelEntryExit {
    private String filename;
    private String desc;
    private String from;
    private String to;

    private int toX;
    private int toY;
    private int toZ;
    private int x1;
    private int y1;
    private int z1;
    private int x2;
    private int y2;
    private int z2;
    private String dir;

    public int getToX() {
        return toX;
    }
    
    public int getToY() {
        return toY;
    }

    public int getToZ() {
        return toZ;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFrom() {
        return from;
    }

    public int getDir() {
        int d = Direction.none;
        if (dir.equals("E")) d = Direction.east;
        else if (dir.equals("S")) d = Direction.south;
        else if (dir.equals("W")) d = Direction.west;
        else if (dir.equals("N")) d = Direction.north;
        return d;
    }

    public void setFrom(String from) {
        this.from = from;
        Scanner toke = new Scanner(from);
        toke.useDelimiter(",");
        x1 = toke.nextInt();
        y1 = toke.nextInt();
        z1 = toke.nextInt();
        if (toke.hasNext())
        {
            x2 = toke.nextInt();
            y2 = toke.nextInt();
            z2 = toke.nextInt();           
        } else {
            x2=x1;
            y2=y1;
            z2=z1;
        }
        if (x1>x2) {int t=x1; x1=x2; x2=t;}
        if (y1>y2) {int t=y1; y1=y2; y2=t;}
        if (z1>z2) {int t=z1; z1=z2; z2=t;}       
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
        Scanner toke = new Scanner(to);
        toke.useDelimiter(",");
        toX = toke.nextInt();
        toY = toke.nextInt();
        toZ = toke.nextInt();
        if (toke.hasNext())
            dir = toke.next();
        else
            dir = "X";    
    }
    
    public boolean inRange(int x, int y, int z)
    {
        return (x>=x1 && x<=x2 && y>=y1 && y<=y2 && z>=z1 && z<=z2);
    }
}
