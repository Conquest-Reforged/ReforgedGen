package com.conquestreforged.gen.designer.heightmap.filter;

import com.conquestreforged.gen.designer.heightmap.Cell;
import com.conquestreforged.gen.designer.heightmap.Size;

public interface Filterable {

    Size getSize();

    Cell[] getBacking();

    default Cell getCellRaw(int x, int z) {
        if (x >= 0 && z >= 0 && x < getSize().total && z < getSize().total) {
            return getBacking()[getSize().indexOf(x, z)];
        }
        return null;
    }
}
