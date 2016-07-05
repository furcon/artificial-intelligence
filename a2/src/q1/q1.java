import java.io.*;
import java.util.*;

public class q1 {
    public static class Tile {
        int x;
        int y;
        char value;
        Tile parent;
        boolean visited;
        int heuristic;
        int cost;

        public Tile(int xCoordinate, int yCoordinate, char val) {
            x = xCoordinate;
            y = yCoordinate;
            value = val;
            parent = null;
            visited = false;
            heuristic = 10000; //given 25x25 grid, this is an impossible estimate 
            cost = 0;
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

        public boolean visited() {
            return visited;
        }

        public int heuristic() {
            return heuristic;
        }

        public int cost() {
            return cost;
        }

        public void setParent(Tile newParent) {
            parent = newParent;
        }

        public void setVisited(boolean isVisited) {
            visited = isVisited;
        }

        public void setHeuristic(int h) {
            heuristic = h;
        }

        public void setCost(int c) {
            cost = c;
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

    public static class CostHeuristicComparator implements Comparator<Tile> {
        public int compare(Tile self, Tile other) {
            Integer selfTotal = new Integer(self.heuristic() + self.cost());
            Integer otherTotal = new Integer(other.heuristic() + other.cost());
            return selfTotal.compareTo(otherTotal);
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

    public static LinkedList<Tile> getChildren (Tile parent, Tile[][] map) {
        LinkedList<Tile> children = new LinkedList<Tile>();
        int x = parent.x();
        int y = parent.y();

        //Top x - 1
        if (x - 1 < 25 && x - 1 > -1) {
            if(map[x - 1][y] != parent.parent() && 
                    !map[x - 1][y].visited() && !map[x - 1][y].isBlocked()) {
                map[x - 1][y].setParent(parent);
                children.add(map[x-1][y]);
            }
        }
        //Bottom x + 1
        if (x + 1 < 25 && x + 1 > -1) {
            if(map[x + 1][y] != parent.parent() && 
                    !map[x + 1][y].visited() && !map[x + 1][y].isBlocked()) {
                map[x + 1][y].setParent(parent); 
                children.add(map[x + 1][y]);
            }
        } 
        //Left y - 1
        if (y - 1 < 25 && y - 1 > -1) {
            if(map[x][y - 1] != parent.parent() && 
                    !map[x][y - 1].visited() && !map[x][y - 1].isBlocked()) {
                map[x][y - 1].setParent(parent); 
                children.add(map[x][y - 1]);
            }
        } 
        //Right y + 1
        if (y + 1 < 25 && y + 1 > -1) {
            if(map[x][y + 1] != parent.parent() && 
                    !map[x][y + 1].visited() && !map[x][y + 1].isBlocked()) {
                map[x][y + 1].setParent(parent); 
                children.add(map[x][y + 1]);
            }
        }

        return children;
    }

    public static void BFS(Tile start, Tile map[][]) {
        LinkedList<Tile> openQueue = new LinkedList<Tile>(); 
        openQueue.add(start);
        start.setVisited(true);
        while(!openQueue.isEmpty()) {
            Tile next = (Tile)openQueue.remove();
            if (next.isEndPosition()) {
                printPath(next, map);
                openQueue.clear();
            } else {
                LinkedList<Tile> children = getChildren(next, map);
                Tile child = (Tile)children.peekFirst();
                while(child != null) {
                    child.setVisited(true);
                    openQueue.add(child);
                    children.remove();
                    child = (Tile)children.peekFirst();
                }            
            }
        }
    }

    public static void DFS(Tile current, Tile map[][]) {
        if (current.isEndPosition()) {
            printPath(current, map);
        } else {
            LinkedList<Tile> children = getChildren(current, map);
            while(!children.isEmpty()) {
                Tile child = (Tile)children.remove();
                DFS(child, map);
            }
        }
    }

    //Cost: each parent to child is cost of 1
    //Heuristic: manhattan distance to end
    public static void AStar(Tile start, Tile map[][]) {
        Comparator<Tile> c = new CostHeuristicComparator();
        PriorityQueue<Tile> openQueue = new PriorityQueue<Tile>(25, c); 
        start.setCost(0);
        openQueue.add(start);
        while(!openQueue.isEmpty()) {
            Tile next = (Tile)openQueue.remove();
            next.setVisited(true);

            if (next.isEndPosition()) {
                printPath(next, map);
                openQueue.clear();
            } else {
                LinkedList<Tile> children = getChildren(next, map);
                Tile child = (Tile)children.peekFirst();
                while (child != null) {
                    child.setCost(next.cost() + 1);
                    openQueue.add(child); //will prioritize
                    children.remove();
                    child = (Tile)children.peekFirst();
                }
            }
        }
    }

    //heuristic used: manhattan distance from current to end
    public static void generateHeuristics(Tile end, Tile map[][]) {
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                if (!map[i][j].isBlocked()) {
                    int h = Math.abs(end.x() - map[i][j].x()) + 
                        Math.abs(end.y() - map[i][j].y());
                    map[i][j].setHeuristic(h);
                } 
            }
        } 
    }

    public static void main(String[] args) {
        Tile map[][] = new Tile[25][25];
        Tile start = null;
        Tile end = null;
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
                    if (current.isEndPosition()) {
                        end = current;
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

        //DFS(start, map);
        BFS(start, map);
        //generateHeuristics(end, map);
        //AStar(start, map);
    }
}


