package com.conquestreforged.gen.designer.heightmap.filter;

import com.conquestreforged.gen.designer.heightmap.Cell;

public class Steepness {

    private final int radius;
    private final float scaler;

    public Steepness() {
        this(1, 16F);
    }

    public Steepness(int radius, float scaler) {
        this.radius = radius;
        this.scaler = scaler;
    }

    public void apply(Filterable map) {
        for (int dz = 0; dz < map.getSize().total; dz++) {
            for (int dx = 0; dx < map.getSize().total; dx++) {
                visit(map, map.getCellRaw(dx, dz), dx, dz);
            }
        }
    }

    public void visit(Filterable cellMap, Cell cell, int cx, int cz) {
        float totalHeightDif = 0F;
        for (int dz = -1; dz <= 2; dz++) {
            for (int dx = -1; dx <= 2; dx++) {
                if (dx == 0 && dz == 0) {
                    continue;
                }

                int x = cx + dx * radius;
                int z = cz + dz * radius;
                Cell neighbour = cellMap.getCellRaw(x, z);
                if (neighbour == null) {
                    continue;
                }

                float height = Math.max(neighbour.value, 62 / 256F);

                totalHeightDif += (Math.abs(cell.value - height) / radius);
            }
        }
        cell.steepness = Math.min(1, totalHeightDif * scaler);
    }
}
