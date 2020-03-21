package com.conquestreforged.gen.terrain;

import com.terraforged.core.util.Seed;
import com.terraforged.core.world.GeneratorContext;
import com.terraforged.core.world.heightmap.Levels;
import me.dags.noise.Module;
import me.dags.noise.Source;

public class ReforgedTerrain {

    private final Levels levels;

    public ReforgedTerrain(GeneratorContext context) {
        levels = context.levels;
    }

    public Module uglySpikeMountains(Seed seed) {
        return Source.simplex(seed.next(), 50, 2)
                .pow(3)
                .scale(0.65)
                .bias(levels.water);
    }
}
