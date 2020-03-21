package com.conquestreforged.gen.designer.app.renderer;

import com.conquestreforged.gen.designer.app.Applet;
import com.conquestreforged.gen.designer.heightmap.Constants;
import com.conquestreforged.gen.designer.heightmap.Heightmap;
import processing.core.PApplet;

public class MeshRenderer extends Renderer {

    public MeshRenderer(Applet visualizer) {
        super(visualizer);
    }

    @Override
    public void render(Heightmap heightmap, float zoom) {
        int worldHeight = Constants.WORLD_HEIGHT;
        int waterLevel = Constants.SEA_LEVEL;
        int seabedLevel = (int) (( Constants.SEA_LEVEL - 0.04) * worldHeight);
        int resolution = Constants.RESOLUTION;

        float w = applet.width / (float) (resolution - 1);
        float h = applet.width / (float) (resolution - 1);
        float unit = w / zoom;

        applet.noStroke();
        applet.pushMatrix();
        applet.translate(-applet.width / 2F, -applet.width / 2F);

        for (int dy = 0; dy < resolution - 1; dy++) {
            applet.beginShape(PApplet.TRIANGLE_STRIP);
            for (int dx = 0; dx < resolution; dx++) {
                draw(heightmap, dx, dy, w, h, zoom, worldHeight, waterLevel, unit);
                draw(heightmap, dx, dy + 1, w, h, zoom, worldHeight, waterLevel, unit);
            }
            applet.endShape();
        }

        applet.popMatrix();
    }

    private void draw(Heightmap heightmap, int dx, int dz, float w, float h, float zoom, int worldHeight, int waterLevel, float unit) {
        float height = heightmap.getValue(dx, dz) * worldHeight;
        float x = dx * w;
        float z = dz * h;
        float y = getHeight((int) height, waterLevel, unit);
        if (Constants.BLOCKY_MESH) {
            y = (int) y;
        }
        applet.vertex(x, z, y);
    }
}
