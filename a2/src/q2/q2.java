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
        public Tile map[][];
        public Tile lastMoveMade[]; //[0] - from, [1] - to
        boolean gameOver;
        int score [];

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

            score = new int[2];
            score[0] = 0; //player 1 initial score
            score[1] = 0;

            lastMoveMade = new Tile[2];
        }

        public Board(Board copy) {
            gameOver = copy.gameOver;
            map = new Tile[5][5];

            for (int i = 1; i <= 4; i++) {
                for (int j = 1; j <= 4; j++) {
                    map[i][j] = new Tile(i, j);
                    map[i][j].setOccupied(copy.map[i][j].occupied());
                    map[i][j].setStones(copy.map[i][j].stones());
                }
            }

            score = new int[2];
            score[0] = copy.getScore(0); //player 1 initial score
            score[1] = copy.getScore(1);

            if (copy.lastMoveMade == null) {
                lastMoveMade = null;
            } else {
                Tile copyFMove = copy.lastMoveMade[0];
                Tile copyTMove = copy.lastMoveMade[1]; 
                lastMoveMade = new Tile[2];
                lastMoveMade[0] = map[copyFMove.x()][copyFMove.y()];
                lastMoveMade[1] = map[copyTMove.x()][copyTMove.y()];
            }
        }

        public int getScore(int player) {
            return score[player];
        }

        public void storeScore(int player, int bestScore) {
            score[player] = bestScore;
        }
    }

    public static class AIPlayer {
        public int player; //0 or 1
        int opponent; //0 or 1

        public AIPlayer(int pl) {
            player = pl;
            opponent = pl == 0 ? 1 : 0;
        }

        public boolean isTileMovableTo(int x, int y, Board board, int currentPlayer) {
            int currentOpponent = currentPlayer == 0 ? 1 : 0;

            if (x >= 1 && x <= 4 && y >= 1 && y <= 4) {
                if (board.map[x][y].occupied() != currentOpponent) {
                    return true;
                }
            }
            return false;
        }

        public LinkedList<Tile> getMovesFromTile(Tile tile, Board board, int currentPlayer) {
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
                    if (isTileMovableTo(x, y, board, currentPlayer)) {
                        moves.add(board.map[x][y]);
                    }
                }
            }

            return moves;
        }

        public LinkedList<Tile> getOwnedTiles(Board board, int currentPlayer) {
            LinkedList<Tile> owned = new LinkedList<Tile>();
            
            for (int i = 1; i <= 4; i++) {
                for (int j = 1; j <= 4; j++) {
                    if (board.map[i][j].occupied() == currentPlayer) {
                        owned.add(board.map[i][j]);
                    }
                }
            }

            return owned;
        }

        public int getNumberOfPossibleMoves(Board board, int currentPlayer) {
            LinkedList<Tile> myTiles = getOwnedTiles(board, currentPlayer);
            int numMoves = 0;
            Tile child = (Tile)myTiles.peekFirst();    
            while (child != null) {
                LinkedList<Tile> moves = getMovesFromTile(child, board, currentPlayer);
                numMoves += moves.size();
                myTiles.remove();
            }

            return numMoves;
        }
       
        public Map<Tile, LinkedList<Tile>> getAllPossibleMoves(Board board, int currentPlayer) {
            Map<Tile, LinkedList<Tile>> moves = 
                new HashMap<Tile, LinkedList<Tile>>();
            
            LinkedList<Tile> myTiles = getOwnedTiles(board, currentPlayer);
            for (int i = 0; i < myTiles.size(); i++) {
                Tile from = myTiles.get(i);
                LinkedList<Tile> to = getMovesFromTile(from, board, currentPlayer);
                if (to.size() > 0) {
                    moves.put(from, to);
                }
            }
            return moves;
        }

        public Board makeMove(Tile from, Tile to, Board board, int currentPlayer) {
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
            Tile tile1 = board.map[x1][y1];
            
            //tile2
            int x2 = x1 + dirX;
            int y2 = y1 + dirY;
            if (isTileMovableTo(x2, y2, board, currentPlayer)) {
                Tile tile2 = board.map[x2][y2];
                
                //tile3
                int x3 = x2 + dirX;
                int y3 = y2 + dirY;
                if (isTileMovableTo(x3, y3, board, currentPlayer)) { //move to 1, 2 and 3
                    Tile tile3 = board.map[x3][y3];
                
                    from.setStones(0);
                    from.setOccupied(-1);

                    tile1.setStones(tile1.stones() + 1);
                    tile1.setOccupied(currentPlayer);
                    stones = stones - 1;

                    if (stones == 0) {
                        return board;
                    } else if (stones == 1 || stones == 2) {
                        tile2.setStones(tile2.stones() + stones);
                        tile2.setOccupied(currentPlayer);
                    } else {
                        tile2.setStones(tile2.stones() + 2);
                        tile2.setOccupied(currentPlayer);
                        stones = stones - 2;
                        
                        tile3.setStones(tile3.stones() + stones);
                        if (stones > 0) {
                            tile3.setOccupied(currentPlayer);
                        }
                    }

                    return board;
                } else { //just move to 1 and 2
                    from.setStones(0);
                    from.setOccupied(-1);

                    tile1.setStones(tile1.stones() + 1);
                    tile1.setOccupied(currentPlayer);
                    stones = stones - 1;
                    
                    tile2.setStones(tile2.stones() + stones);
                    if (stones > 0) {
                        tile2.setOccupied(currentPlayer);
                    }

                    return board;
                }
            } else { //just move to 1
                from.setStones(0);
                from.setOccupied(-1);

                tile1.setStones(tile1.stones() + stones);
                if (stones > 0) {
                    tile1.setOccupied(currentPlayer);
                }

                return board;
            }
        }

        //takes in currentBoard
        //return {-1: player lost, 0: continue game, 1: player won}
        public int makeBestMove(Board currentBoard) {
            int numMoves = getNumberOfPossibleMoves(currentBoard, this.player);

            if (numMoves == 0) {
                currentBoard.gameOver = true;
                System.out.println("Player " + this.player + "cannot make any more moves.");
                return -1; //player can make no moves
            } else {
                Board bestBoard = getBestMove(3, currentBoard, this.player); //invoke minimax
                currentBoard = makeMove(bestBoard.lastMoveMade[0], bestBoard.lastMoveMade[1], currentBoard, this.player);
                if (bestBoard.getScore(this.player) == numMoves) {
                    return 1; //opponent can make no moves
                }
            }
            return 0; //no one won yet
        }

        //MINIMAX
        public Board getBestMove (int level, Board currentBoard, int currentPlayer) {
            Map<Tile, LinkedList<Tile>> allMoves = getAllPossibleMoves(currentBoard, currentPlayer);

            int bestScore = currentPlayer == this.player ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            int currentScore;
            Board bestBoard = null;

            if (allMoves.isEmpty() || level == 0) {
                bestScore = computeScore(bestBoard);
                bestBoard.storeScore(this.player, bestScore);
            } else {
                Tile from[] = (Tile [])allMoves.keySet().toArray();
                for (int i = 0; i < from.length; i++) { //for all tiles I own
                    LinkedList<Tile> to = (LinkedList<Tile>)allMoves.get(from[i]);
                    for (int j = 0; j < to.size(); j++) { // to all tiles I can move to
                        Board initialBoard = new Board(currentBoard);
                        Board moveBoard = makeMove(from[i], to.get(j), initialBoard, currentPlayer); //try move
                        if (currentPlayer == this.player) { //maximize
                            currentScore = (int)((Board)getBestMove(level - 1, moveBoard, this.opponent)).getScore(currentPlayer); //recurse
                            if (currentScore > bestScore) {
                                bestScore = currentScore;
                                bestBoard = moveBoard;
                            }
                        } else if (currentPlayer == this.opponent) { //minimize
                            currentScore = (int)((Board)getBestMove(level - 1, moveBoard, this.player)).getScore(currentPlayer);
                            if (currentScore < bestScore) {
                                bestScore = currentScore;
                                bestBoard = moveBoard;
                            }
                        }
                    }
                }
            }
            return bestBoard;
        }

        //EVALUATION FUNCTION
        public int computeScore(Board board) {
            int myMoves = getNumberOfPossibleMoves(board, this.player);
            int oppMoves = getNumberOfPossibleMoves(board, this.opponent);
            return myMoves - oppMoves;
        }
    }

    public static void main(String[] args) {
        //TODO intitialize game
        //implement random agent
    }
}
