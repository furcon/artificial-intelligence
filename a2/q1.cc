#include <sstream>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fstream>
using namespace std;
int main(int argc, char** argv) {
    if(argc != 2) {
        printf("usage:\t ./search <maze-file.txt>\n"); 
        exit(-1);
    }

    char *fname = argc[1];
    std::ifstream infile(fname);
    std::string line;
    std::string map[25][25] = {0};
    while (std::getline(infile, line)) {
        std::istringstream iss(line);
        
        if (!(iss >> a >> b)) { break; } // error

        // process pair (a,b)
        // }



}

