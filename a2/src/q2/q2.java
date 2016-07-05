import java.io.*;
import java.util.*;

public class q2 {
    public static class Tile {
        int x; // [1, 4]
        int y; // [1. 4]
        int occupied; // -1 | 0 | 1
        int stones;

        public Tile(int X, int Y) {
            x = X;
            y = Y;
            occupied = -1;
            stones = 0;
        }

        public int x() {
            return x;
        }

        public int y() {
            return y;
        }

        public int occupied() {
            return occupied;
        }

        public int stones() {
            return stones;
        }

        public void setOccupied(int playerOrNone) {
            occupied = playerOrNone;
        }

        public void setStones(int stoneValue) {
            if (stoneValue >= 0) {
                stones = stoneValue;
            }
        }
    }

    public static class Board {
        public Tile map[][]; //risky to make public, too late to care
        public Tile lastMoveMade[]; //[0] - from, [1] - to
        boolean gameOver;

        public Board() {
            gameOver = false;
            map = new Tile[5][5];

            //ignore all x = 0, y = 0, doing this for ease of use
            for (int i = 1; i <= 4; i++) {
                for (int j = 1; j <= 4; j++) {
                    map[i][j] = new Tile(i, j);
                }
            }

            map[1][4].setOccupied(0); //player 1 = 0 (white)
            map[1][4].setStones(10);
            map[4][1].setOccupied(1); //player 2 = 1 (black)
            map[4][1].setStones(10);

            lastMoveMade = new Tile[2];
        }
    }

    public static class AIPlayer {
        int player; //0 or 1
        int opponent; //0 or 1
        int maxDepth; //probably 3
        int numExplored;

        public AIPlayer(pl, maxD) {
            player = pl;
            opponent = pl == 0 ? 1 : 0;
            maxDepth = maxD;
            numExplored = 0;
        }

        public boolean isTileMovableTo(int x, int y, Board board) {
            if (x >= 1 && x <= 4 && y >= 1 && y <= 4) {
                if (board.map[x][y].occupied() != opponent) {
                    return true;
                }
            }

            return false;
        }

        public LinkedList<Tile> getMovesFromTile(Tile tile, Board board) {
            LinkedList<Tile> moves = new LinkedList<Tile>();
            
            // bottom-left, bottom, bottom-right
            // center-left, center, center-right
            // top-left, top, top-right
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 && j == 0) { //don't move to center
                        continue;
                    }
                    int x = tile.x() + i;
                    int y = tile.y() + j; 
                    if (isTileMovableTo(x, y, board)) {
                        moves.add(board.map[x][y]);
                    }
                }
            }

            return moves;
        }

        public LinkedList<Tile> getOwnedTiles(Board board) {
            LinkedList<Tile> owned = new LinkedList<Tile>();
            
            for (int i = 1; i <= 4; i++) {
                for (int j = 1; j <= 4; j++) {
                    if (board.map[i][j].occupied() == player) {
                        owned.add(board.map[i][j]);
                    }
                }
            }

            return owned;
        }

        public int getNumberOfPossibleMoves(Board board) {
            LinkedList<Tile> myTiles = getOwnedTiles(board);
            int numMoves = 0;
            Tile child = (Tile)myTiles.peekFirst();    
            while (child != null) {
                LinkedList<Tile> moves = getMovesFromTile(child, board);
                numMoves += moves.size();
                myTiles.remove();
            }

            return numMoves;
        }

        public Board makeMove(Tile from, Tile to, Board board) {
            //assuming we can make the move as defined above
            board.lastMoveMade[0] = from;
            board.lastMoveMade[1] = to;

            // ex. -1, -1 is bottom-left
            int dirX = to.x() - from.x(); 
            int dirY = to.y() - from.y();

            int stones = from.stones();

            //tile 1
            int x1 = to.x();
            int y1 = to.y();
            
            //tile2
            int x2 = x1 + dirX;
            int y2 = y1 + dirY;
            if (isTileMovableTo(x2, y2, board) {
                Tile tile2 = board.map[x2][y2];
                
                //tile3
                int x3 = x2 + dirX;
                int y3 = y2 + dirY;
                if (isTileMovableTo(x3, y3, board) { //move to 1, 2 and 3
                    Tile tile3 = board.map[x3][y3]
                
                    from.setStones(0);
                    from.setOccupied(-1);

                    tile1.setStones(tile1.stones() + 1);
                    tile1.setOccupied(player);
                    stones = stones - 1;

                    if (stones == 0) {
                        return board;
                    } else if (stones == 1 || stones == 2) {
                        tile2.setStones(tile2.stones() + stones);
                        tile2.setOccupied(player);
                    } else {
                        tile2.setStones(tile2.stones() + 2);
                        tile2.setOccupied(player);
                        stones = stones - 2;
                        
                        tile3.setStones(tile3.stones() + stones);
                        if (stones > 0) {
                            tile3.setOccupied(player);
                        }
                    }

                    return board;
                } else { //just move to 1 and 2
                    from.setStones(0);
                    from.setOccupied(-1);

                    tile1.setStones(tile1.stones() + 1);
                    tile1.setOccupied(player);
                    stones = stones - 1;
                    
                    tile2.setStones(tile2.stones() + stones);
                    if (stones > 0) {
                        tile2.setOccupied(player);
                    }

                    return board;
                }
            } else { //just move to 1
                from.setStones(0);
                from.setOccupied(-1);

                tile1.setStones(tile1.stones() + stones);
                if (stones > 0) {
                    tile1.setOccupied(player);
                }

                return board;
            }

            return board;
        }

        //next steps:
        //getownedtiles
        //getpossiblemoves for each
        //use this to implement minimax tree
        //pick best move using minimax
        //makeMove

        //needtoimplement:
        //heuristics getopponentnummoves - getplayernummoves 
        
        //alpha-beta ugh

    }
}
