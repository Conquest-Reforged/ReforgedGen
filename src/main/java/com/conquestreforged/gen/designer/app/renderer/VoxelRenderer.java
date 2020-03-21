package com.conquestreforged.gen.designer.app.renderer;

import com.conquestreforged.gen.designer.app.Applet;
import com.conquestreforged.gen.designer.heightmap.Constants;
import com.conquestreforged.gen.designer.heightmap.Heightmap;
import processing.core.PApplet;

public class VoxelRenderer extends Renderer {

    public VoxelRenderer(Applet visualizer) {
        super(visualizer);
    }

    @Override
    public void render(Heightmap heightmap, float zoom) {
        int worldHeight = Constants.WORLD_HEIGHT;
        int resolution = Constants.RESOLUTION;
        int center = resolution / 2;

        float w = applet.width / (float) resolution;
        float h = applet.width / (float) resolution;
        float el = w / zoom;

        applet.pushMatrix();
        applet.translate(-applet.width / 2F, -applet.width / 2F);

        for (int dy = 0; dy < resolution; dy++) {
            for (int dx = 0; dx < resolution; dx++) {
                float cellHeight = heightmap.getValue(dx, dy) * worldHeight;
                int height = Math.min(worldHeight, Math.max(0, (int) cellHeight));

                if (height < 0) {
                    continue;
                }

                float x0 = dx * w;
                float x1 = (dx + 1) * w;
                float z0 = dy * h;
                float z1 = (dy + 1) * h;
                float y = getHeight(height, Constants.SEA_LEVEL, el);

                drawColumn(x0, z0, 0, x1, z1, y);
            }
        }

        applet.popMatrix();
    }

    private void drawColumn(float x0, float y0, float z0, float x1, float y1, float z1) {
        applet.beginShape(PApplet.QUADS);
        // +Z "front" face
        applet.vertex(x0, y0, z1);
        applet.vertex(x1, y0, z1);
        applet.vertex(x1, y1, z1);
        applet.vertex(x0, y1, z1);

        // -Z "back" face
        applet.vertex(x1, y0, z0);
        applet.vertex(x0, y0, z0);
        applet.vertex(x0, y1, z0);
        applet.vertex(x1, y1, z0);

        // +Y "bottom" face
        applet.vertex(x0, y1, z1);
        applet.vertex(x1, y1, z1);
        applet.vertex(x1, y1, z0);
        applet.vertex(x0, y1, z0);

        // -Y "top" face
        applet.vertex(x0, y0, z0);
        applet.vertex(x1, y0, z0);
        applet.vertex(x1, y0, z1);
        applet.vertex(x0, y0, z1);

        // +X "right" face
        applet.vertex(x1, y0, z1);
        applet.vertex(x1, y0, z0);
        applet.vertex(x1, y1, z0);
        applet.vertex(x1, y1, z1);

        // -X "left" face
        applet.vertex(x0, y0, z0);
        applet.vertex(x0, y0, z1);
        applet.vertex(x0, y1, z1);
        applet.vertex(x0, y1, z0);

        applet.endShape();
    }
}
