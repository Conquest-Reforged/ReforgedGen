package com.conquestreforged.gen.designer.heightmap;

public class Constants {

    public static final boolean BLOCKY_MESH = true;

    public static final int RESOLUTION = 160;
    public static final int SEA_LEVEL = 63;
    public static final int GROUND_LEVEL = 64;
    public static final int WORLD_HEIGHT = 255;
    public static final float GROUND = GROUND_LEVEL / (float) WORLD_HEIGHT;

    public static final float EROSION = 0.3F;
    public static final float DEPOSITION = 0.5F;
    public static final int EROSION_ITERATIONS = 12000;

    public static final float MAX_FILTER_ZOOM_LEVEL = 1.75F;

    public static float ground(int blocks) {
        return GROUND + ((float) blocks) / WORLD_HEIGHT;
    }
}
