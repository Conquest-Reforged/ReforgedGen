package com.conquestreforged.gen.designer;

import com.conquestreforged.gen.designer.app.Applet;
import com.conquestreforged.gen.terrain.ReforgedTerrain;
import com.terraforged.core.settings.Settings;
import com.terraforged.core.util.Seed;
import com.terraforged.core.world.GeneratorContext;
import com.terraforged.core.world.terrain.Terrains;
import me.dags.noise.Module;

public class TerrainDesigner {

    public static void main(String[] args) {
        long seed = 1;
        Applet.start(seed);
    }

    public static Module getModule(Seed seed) {
        ReforgedTerrain terrain = new ReforgedTerrain(createContext());
        return terrain.uglySpikeMountains(seed);
    }

    private static GeneratorContext createContext() {
        return new GeneratorContext(
                new Terrains.Mutable().create(),
                new Settings()
        );
    }
}