package com.conquestreforged.gen.designer.app.renderer;

import com.conquestreforged.gen.designer.app.Applet;
import com.conquestreforged.gen.designer.heightmap.Heightmap;

public abstract class Renderer {

    protected final Applet applet;

    protected Renderer(Applet visualizer) {
        this.applet = visualizer;
    }

    public abstract void render(Heightmap heightmap, float zoom);

    protected float getHeight(float value, float waterLevel, float el) {
        if (value < waterLevel) {
            float temp = 0.5F;
            float tempDelta = temp > 0.5F ? temp - 0.5F : -(0.5F - temp);
            float tempAlpha = (tempDelta / 0.5F);
            float hueMod = 4 * tempAlpha;

            float depth = (waterLevel - value) /  (float) (90);
            float darkness = (1 - depth);
            float darknessMod = 0.5F + (darkness * 0.5F);

            applet.fill(60 - hueMod, 65, 90 * darknessMod);
            return waterLevel * el;
        } else {
            float hei = Math.min(1, Math.max(0, value - waterLevel) / (255F - waterLevel));
            float temp = 0.5F;
            float moist = Math.min(temp, 0.5F);

            float hue = 35 - (temp * (1 - moist)) * 25;
            float sat = 75 * (1 - hei);
            float bri = 50 + 40 * hei;
            applet.fill(hue, sat, bri);
            return value * el;
        }
    }
}
