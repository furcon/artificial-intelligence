#ifndef TILE_H
#define TILE_H
class Tile {
  public:
    Tile(int x, int y, char entry);
    bool isStartPosition();
    bool isEndPosition();
    bool isBlocked();//false means it's blocked
    int getX();
    int getY();
    char getContent();

    Tile* getParent();

    //neighbors
    Tile *left;
    Tile *right;
    Tile *up;
    Tile *down;

    Tile *parent;

  private:
    int x;
    int y;
    char content;
};
#endif
