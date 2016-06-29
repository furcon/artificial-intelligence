CXX_FILES := $(wildcard src/*/*.cc)
OBJ_FILES := $(addprefix obj_files/,$(notdir $(CXX_FILES:.cc=.o)))
CXX_FLAGS := -fno-strict-aliasing -g -std=c++11 -O2 -Wall -Werror -Wextra -Wno-missing-field-initializers -Wstrict-overflow

obj_files/%.o: src/*/%.cc
	${CXX} $(CXX_FLAGS) -c -o $@ $<
%: obj_files/%.o
	${CXX} -o $@ $^


BINARIES := search
all: $(BINARIES)

search: obj_files/search.o obj_files/tile.o


clean:
	rm -f obj_files/*.o
	rm -f $(BINARIES)
