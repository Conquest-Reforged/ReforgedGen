package com.conquestreforged.gen.designer.heightmap.filter;

import com.conquestreforged.gen.designer.heightmap.Cell;
import com.conquestreforged.gen.designer.heightmap.Constants;
import me.dags.noise.util.NoiseUtil;

public class Smoothing {

    private final int radius;
    private final float rad2;
    private final float strength;
    private final Modifier modifier;

    public Smoothing() {
        this.radius = NoiseUtil.round(1.75F + 0.5F);
        this.rad2 = 1.75F * 1.75F;
        this.strength = 0.85F;
        this.modifier = Modifier.range(Constants.ground(1), Constants.ground(64)).invert();
    }

    public void apply(Filterable map, int seedX, int seedZ, int iterations) {
        while (iterations-- > 0) {
            apply(map);
        }
    }

    private void apply(Filterable cellMap) {
        int maxZ = cellMap.getSize().total - radius;
        int maxX = cellMap.getSize().total - radius;
        for (int z = radius; z < maxZ; z++) {
            for (int x = radius; x < maxX; x++) {
                Cell cell = cellMap.getCellRaw(x, z);

                float total = 0;
                float weights = 0;

                for (int dz = -radius; dz <= radius; dz++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        float dist2 = dx * dx + dz * dz;
                        if (dist2 > rad2) {
                            continue;
                        }
                        int px = x + dx;
                        int pz = z + dz;
                        Cell neighbour = cellMap.getCellRaw(px, pz);
                        if (neighbour == null) {
                            continue;
                        }
                        float value = neighbour.value;
                        float weight = 1F - (dist2 / rad2);
                        total += (value * weight);
                        weights += weight;
                    }
                }

                if (weights > 0) {
                    float dif = cell.value - (total / weights);
                    cell.value -= modifier.modify(cell, dif * strength);
                }
            }
        }
    }
}
