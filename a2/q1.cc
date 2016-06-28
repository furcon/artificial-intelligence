#include <sstream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fstream>
#include <iostream>

using namespace std;
int main(int argc, char** argv) {
    if(argc != 2) {
        printf("usage:\t ./search <maze-file.txt>\n"); 
        exit(-1);
    }

    char *fname = argv[1];
    std::ifstream infile(fname);
    std::string line;
    
    char map[25][25];
    int row = 0;
    while (std::getline(infile, line)) { //will run 25 times
        char* characters = (char *)line.c_str();
        for (int col = 0; col < 25; col++) {
            if (!characters[col]) {
                cout << "Error: invalid file" << endl;
                exit(-1);
            } else {
                map[row][col] = characters[col];
            }
        }
        row++;
    }

    for (int i = 0; i < 25; i++) {
        for (int j = 0; j < 25; j++) {
            cout << map[i][j];
        }
    }

    exit(0);
}

