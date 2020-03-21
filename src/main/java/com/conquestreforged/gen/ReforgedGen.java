package com.conquestreforged.gen;

import com.conquestreforged.gen.terrain.ReforgedTerrain;
import com.terraforged.api.event.SetupEvent;
import com.terraforged.core.util.Seed;
import com.terraforged.core.world.terrain.Terrain;
import com.terraforged.core.world.terrain.provider.TerrainProvider;
import me.dags.noise.Module;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

@Mod("reforged_gen")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReforgedGen {

    private static final Logger log = LogManager.getLogger("ReforgedGen");
    private static final AtomicInteger ID_COUNTER = new AtomicInteger(50);

    @SubscribeEvent
    public static void terrain(SetupEvent.Terrain event) {
        log.info("Registering custom terrain types");

        TerrainProvider manager = event.getManager();

        // The seed that all our terrain types will build off
        Seed seed = event.getContext().seed;

        // This is similar to the LandForms class in TerraForged.
        // You can add however many new methods you like that return your custom terrain types.
        // Each terrain type will need to be registered as per the below example.
        ReforgedTerrain terrain = new ReforgedTerrain(event.getContext());

        // EXAMPLE: ugly_spiked_mountains

        // Here we create a new instance of the ugly spikes module
        Module module = terrain.uglySpikeMountains(seed);

        // Here we register it as 'unmixable' (described below)
        // We provide:
        // 1) the manager   - this is what all terrain types get registered to.
        // 2) a name        - ugly_spiked_mountains - which can be used to locate the terrain type using /terra locate.
        // 3) a weight      - controls how rare/common it is relative to other terrain types. 50 makes it very common
        //                    so it should be easy for the locate command to find.
        // 4) the module    - the module that provides the heightmap noise to shape the terrain.
        registerUnMixable(manager, "ugly_spiked_mountains", 50.0, module);
    }

    private static void registerMixable(TerrainProvider manager, String name, double weight, Module module) {
        // Generate a new id for the terrain type.
        int terrainId = ID_COUNTER.getAndIncrement();

        // Create the terrain type, giving it the name, id, and weight.
        Terrain terrain = new Terrain(name, terrainId, weight);

        // Register the terrain and module to manager
        // 'Mixable' means that TerraForged will create additional terrain types using this module blended with
        //  other modules. This creates more variation in the world. Note the unmixed module will be used too.
        manager.registerMixable(terrain, module);
    }

    private static void registerUnMixable(TerrainProvider manager, String name, double weight, Module module) {
        // Generate a new id for the terrain type.
        int terrainId = ID_COUNTER.getAndIncrement();

        // Create the terrain type, giving it the name, id, and weight.
        Terrain terrain = new Terrain(name, terrainId, weight);

        // Register the terrain and module to manager
        // 'UnMixable' means this terrain type will not blended with any others. This is useful for unique
        //  terrains such as volcanoes.
        manager.registerUnMixable(terrain, module);
    }
}
