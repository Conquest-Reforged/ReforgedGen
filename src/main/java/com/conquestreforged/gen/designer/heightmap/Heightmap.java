package com.conquestreforged.gen.designer.heightmap;

import com.conquestreforged.gen.designer.TerrainDesigner;
import com.conquestreforged.gen.designer.heightmap.filter.Filterable;
import com.terraforged.core.util.Seed;
import me.dags.noise.Module;

public class Heightmap implements Filterable {

    private Size size;
    private Cell[] data;

    public float getValue(int x, int z) {
        return data[size.indexOf(x, z)].value;
    }

    public void update(float x, float z, float zoom, int resolution, int seed) {
        init(resolution);
        render(TerrainDesigner.getModule(new Seed(seed)), x, z, zoom, resolution);
    }

    public void init(int resolution) {
        size = new Size(resolution);
        data = new Cell[resolution * resolution];
    }

    public void render(Module module, float x, float z, float zoom, int resolution) {
        float offset = (resolution * zoom) / 2F;
        for (int dz = 0; dz < resolution; dz++) {
            for (int dx = 0; dx < resolution; dx++) {
                float nx = (x - offset) + (dx * zoom);
                float nz = (z - offset) + (dz * zoom);
                Cell cell = new Cell();
                cell.value = module.getValue(nx, nz);
                data[size.indexOf(dx, dz)] = cell;
            }
        }
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public Cell[] getBacking() {
        return data;
    }
}
