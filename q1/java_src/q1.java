import java.io.*;
import java.util.*;

public class q1 {
    public static class Tile {
        int x;
        int y;
        char value;
        Tile parent;

        public Tile(int xCoordinate, int yCoordinate, char val) {
            x = xCoordinate;
            y = yCoordinate;
            value = val;
            parent = null;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        public char val() {
            return value;
        }

        public Tile parent() {
            return parent;
        }

        public void setParent(Tile newParent) {
            parent = newParent;
        }

        public boolean isBlocked() {
            return (value == 'x');
        }

        public boolean isEndPosition() {
            return (value == 'E');
        }

        public boolean isStartPosition() {
            return (value == 'S');
        }
    }
   
    public static void printPath(Tile last, Tile map[][]) {
        Vector<Tile> path = new Vector<Tile>(25);
        path.addElement(last);
        Tile current = last;
        while(current.parent() != null) {
            path.addElement(current.parent());
            current = current.parent();
        }
        for (int i = path.size()-1; i >= 0; i--) {
            System.out.println(path.get(i).x() + ", " + path.get(i).y());
        }
    }

    public static void DFS(Tile current, Tile map[][]) {
        if (current.isEndPosition()) {
            printPath(current, map);
        } else {
            int x = current.x();
            int y = current.y();
            
            //Top x - 1
            if (x - 1 < 25 && x - 1 > -1) {
                //System.out.println("child is: " + (x - 1) + ", " + y);
                //System.out.println("with value: " + map[x - 1][y].val());
                if(map[x - 1][y] != current.parent() && !map[x - 1][y].isBlocked()) {
                    //System.out.println("Going to recurse on child: " + (x - 1) + ", " + y); 
                    map[x - 1][y].setParent(current);
                    DFS(map[x - 1][y], map);
                }
            }
            //Bottom x + 1
            if (x + 1 < 25 && x + 1 > -1) {
                //System.out.println("child is: " + (x + 1) + ", " + y);
                //System.out.println("with value: " + map[x + 1][y].val());
                if(map[x + 1][y] != current.parent() && !map[x + 1][y].isBlocked()) {
                    //System.out.println("Going to recurse on child: " + (x + 1) + ", " + y); 
                    map[x + 1][y].setParent(current);
                    DFS(map[x + 1][y], map);
                }
            } 
            //Left y - 1
            if (y - 1 < 25 && y - 1 > -1) {
                //System.out.println("child is: " + x + ", " + (y - 1));
                //System.out.println("with value: " + map[x][y - 1].val());
                if(map[x][y - 1] != current.parent() && !map[x][y - 1].isBlocked()) {
                    //System.out.println("Going to recurse on child: " + x + ", " + (y - 1)); 
                    map[x][y - 1].setParent(current);
                    DFS(map[x][y - 1], map);
                }
            } 
            //Right y + 1
            if (y + 1 < 25 && y + 1 > -1) {
                //System.out.println("child is: " + x + ", " + (y + 1));
                //System.out.println("with value: " + map[x][y + 1].val());
                if(map[x][y + 1] != current.parent() && !map[x][y + 1].isBlocked()) {
                    //System.out.println("Going to recurse on child: " + x + ", " + (y + 1)); 
                    map[x][y + 1].setParent(current);
                    DFS(map[x][y + 1], map);
                }
            }
        }
    }

    public static void main(String[] args) {
        Tile map[][] = new Tile[25][25];
        Tile start = null;
        try {
            File file = new File(args[0]);
            Scanner scanner = new Scanner(file);
            int row = 24;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                for (int col = 0; col < line.length(); col++) {
                    Tile current = new Tile(row, col, line.charAt(col));
                    map[row][col] = current;
                    if (current.isStartPosition()) {
                        start = current;
                    }
                }
                row--;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (start == null) {
            start = map[0][0];
        }

        /*for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                System.out.print(map[i][j].val());                
            }
            System.out.println();
        }*/

        DFS(start, map);
    }
}







