package com.conquestreforged.gen.designer.heightmap;

public class Size {

    public final int size;
    public final int total;
    public final int border;

    public Size(int resolution) {
        this.size = resolution;
        this.border = 0;
        this.total = resolution;
    }

    public int indexOf(int x, int z) {
        return (z * total) + x;
    }
}
