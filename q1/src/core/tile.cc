#include "tile.h"
#include <cstring>
using namespace std;

#define BLOCKED "x"
#define START "S"
#define END "E"

Tile::Tile(int x, int y, char content) {
  this->x = x;
  this->y = y;
  this->content = content;

  this->parent=NULL;
}


bool Tile::isBlocked() {
  return strcmp(&content, BLOCKED) == 0;
}

bool Tile::isStartPosition() {
  return strcmp(&content, START) == 0;
}

bool Tile::isEndPosition() {
  return strcmp(&content, END) == 0;
}


int Tile::getX() {
  return this->x;
}

int Tile::getY() {
  return this->y;
}

Tile* Tile::getParent() {
  return this->parent;
}

char Tile::getContent() {
  return this->content;
}
