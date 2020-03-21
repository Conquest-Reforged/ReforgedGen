package com.conquestreforged.gen.designer.app;

import com.conquestreforged.gen.designer.TerrainDesigner;
import com.conquestreforged.gen.designer.heightmap.Constants;
import com.conquestreforged.gen.designer.heightmap.Heightmap;
import com.conquestreforged.gen.designer.heightmap.filter.Erosion;
import com.conquestreforged.gen.designer.heightmap.filter.Smoothing;
import com.terraforged.core.util.Seed;
import me.dags.noise.Module;
import me.dags.noise.util.NoiseUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class Exporter {

    public static void export(int seed, float offsetX, float offsetZ, float zoom, boolean filters) {
        System.out.print("Exporting... ");
        Module module = TerrainDesigner.getModule(new Seed(seed));
        ForkJoinPool.commonPool().submit(() -> {
            export(module, offsetX, offsetZ, zoom, filters);
            return null;
        });
    }

    public static void export(Module module, float offsetX, float offsetZ, float zoom, boolean filters) throws IOException {
        long id = System.currentTimeMillis();
        int size = NoiseUtil.round(Constants.RESOLUTION * zoom);

        Heightmap heightmap = new Heightmap();
        heightmap.init(size);
        heightmap.render(module, offsetX, offsetZ, 1F, size);

        if (filters) {
            new Erosion().apply(heightmap, 0, 0, Constants.EROSION_ITERATIONS);
            new Smoothing().apply(heightmap, 0, 0, 1);
        }

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                float value = heightmap.getValue(x, y);
                int white = NoiseUtil.round(value * 255);
                int color = new Color(white, white, white).getRGB();
                image.setRGB(x, y, color);
            }
        }

        File out = new File("Export-" + id + ".png");
        ImageIO.write(image, "png", out);
        System.out.println("Saved to: " + out);
    }
}
