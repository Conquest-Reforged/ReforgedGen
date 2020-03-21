package com.conquestreforged.gen.designer.app;

import com.conquestreforged.gen.designer.app.renderer.MeshRenderer;
import com.conquestreforged.gen.designer.app.renderer.Renderer;
import com.conquestreforged.gen.designer.app.renderer.VoxelRenderer;
import com.conquestreforged.gen.designer.heightmap.Constants;
import com.conquestreforged.gen.designer.heightmap.Heightmap;
import com.conquestreforged.gen.designer.heightmap.filter.Erosion;
import com.conquestreforged.gen.designer.heightmap.filter.Smoothing;
import com.conquestreforged.gen.designer.heightmap.filter.Steepness;
import me.dags.noise.util.NoiseUtil;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.function.Consumer;

public class Applet extends PApplet {

    private static boolean random = false;
    public static int seed = -1;

    protected Renderer mesh = new MeshRenderer(this);
    protected Renderer voxel = new VoxelRenderer(this);

    private float offsetX = 0;
    private float offsetZ = 0;
    private final Heightmap heightmap = new Heightmap();
    public final Controller controller = new Controller();
    private final Erosion erosion = new Erosion();
    private final Smoothing smoothing = new Smoothing();
    private final Steepness steepness = new Steepness();
    private final String bits = System.getProperty("sun.arch.data.model");

    public static void start(long seed) {
        Applet.seed = (int) seed;
        Applet.random = seed == -1;
        main(Applet.class.getName());
    }

    @Override
    public void draw() {
        offsetX += controller.velocityX() * controller.zoomLevel() * controller.zoomLevel();
        offsetZ += controller.velocityY() * controller.zoomLevel() * controller.zoomLevel();

        heightmap.update(offsetX, offsetZ, controller.zoomLevel(), Constants.RESOLUTION, seed);
        if (controller.filters() && controller.zoomLevel() <= Constants.MAX_FILTER_ZOOM_LEVEL) {
            erosion.apply(heightmap, 0, 0, Constants.EROSION_ITERATIONS);
            smoothing.apply(heightmap, 0, 0, 1);
        }
        steepness.apply(heightmap);

        // color stuff
        noStroke();
        background(0);
        colorMode(HSB, 100);

        // lighting
        ambientLight(0, 0, 75, width / 2F, -height, height / 2F);
        pointLight(0, 0, 50, width / 2F, -height * 100, height / 2F);

        // render
        pushMatrix();
        controller.apply(this);
        drawTerrain(controller.zoomLevel());
        popMatrix();

        int size = NoiseUtil.round(Constants.RESOLUTION * controller.zoomLevel());
        leftAlignText(10, 20, 20, "Size: " + size + "x" + size);

        if (controller.export()) {
            Exporter.export(seed, offsetX, offsetZ, controller.zoomLevel(), controller.filters());
        }
    }

    @Override
    public void setup() {
        super.setup();
        surface.setAlwaysOnTop(true);
        if (random) {
            offsetX = 0;
            offsetZ = 0;
        }
    }

    @Override
    public void settings() {
        size(500, 500, P3D);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        controller.mousePress(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        controller.mouseRelease(event);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        controller.mouseDrag(event);
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        controller.mouseWheel(event);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        controller.keyPress(event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        controller.keyRelease(event);
    }

    public void leftAlignText(int margin, int top, int lineHeight, String... lines) {
        noLights();
        fill(0, 0, 100);
        for (int i = 0; i < lines.length; i++) {
            int y = top + (i * lineHeight);
            text(lines[i], margin, y);
        }
    }


    public void drawTerrain(float zoom) {
        if (controller.getRenderMode() == 0) {
            voxel.render(heightmap, zoom);
        } else {
            mesh.render(heightmap, zoom);
        }
    }

    public static float hue(float value, int steps, int max) {
        value = Math.round(value * (steps - 1));
        value /= (steps - 1);
        return value * max;
    }
}
