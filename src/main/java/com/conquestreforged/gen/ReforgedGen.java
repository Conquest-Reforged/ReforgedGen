package com.conquestreforged.gen;

import com.terraforged.api.event.SetupEvent;
import com.terraforged.api.material.geology.StrataConfig;
import com.terraforged.api.material.geology.StrataGenerator;
import com.terraforged.core.world.geology.Strata;
import com.terraforged.core.world.terrain.Terrain;
import me.dags.noise.Module;
import me.dags.noise.Source;
import net.minecraft.block.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("reforged_gen")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReforgedGen {

    @SubscribeEvent
    public static void geology(SetupEvent.Geology event) {
        // strata config controls the depths and number of layers of a material type
        StrataConfig config = new StrataConfig();
        config.rock.minLayers = 40;
        config.rock.maxLayers = 60;

        // the provided strata generator populates a new strata with materials
        // according to our strata config
        int seed = event.getContext().seed.next();
        StrataGenerator generator = event.getManager().getStrataGenerator();
        Strata<BlockState> strata = generator.generate(seed, 100, config);

        // register to the geology manager
        event.getManager().register(strata);
    }

    @SubscribeEvent
    public static void terrain(SetupEvent.Terrain event) {
        int seed = event.getContext().seed.next();

        Module perlin = Source.perlin(seed, 100, 3)
                // scale output height between 0 - 30 blocks
                .scale(event.getContext().levels.scale(30))
                // set the base height of the terrain to water level
                .bias(event.getContext().levels.water);

        Terrain terrain = new Terrain("hello_perlin", 123);

        // a 'mixable' terrain module can get blended with other
        // mixable modules to create all-new terrain variations
        event.getManager().registerMixable(terrain, perlin);
    }
}
