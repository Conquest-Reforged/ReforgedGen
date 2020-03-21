package com.conquestreforged.gen.designer.heightmap;

import me.dags.noise.util.NoiseUtil;

public class Cell {

    private static final Cell EMPTY = new Cell() {
        @Override
        public boolean isAbsent() {
            return true;
        }
    };

    public float continent;
    public float continentEdge;

    public float value;
    public float biome;
    public float biomeMoisture;
    public float biomeTemperature;
    public float moisture;
    public float temperature;
    public float steepness;
    public float erosion;
    public float sediment;

    public float mask = 1F;
    public float biomeMask = 1F;
    public float biomeTypeMask = 1F;
    public float regionMask = 1F;
    public float riverMask = 1F;

    public void copy(Cell other) {
        continent = other.continent;
        continentEdge = other.continentEdge;

        value = other.value;
        biome = other.biome;

        biomeMask = other.biomeMask;
        riverMask = other.riverMask;
        biomeMoisture = other.biomeMoisture;
        biomeTemperature = other.biomeTemperature;
        mask = other.mask;
        regionMask = other.regionMask;
        moisture = other.moisture;
        temperature = other.temperature;
        steepness = other.steepness;
        erosion = other.erosion;
        sediment = other.sediment;
        biomeTypeMask = other.biomeTypeMask;
    }

    public float combinedMask(float clamp) {
        return NoiseUtil.map(biomeMask * regionMask, 0, clamp, clamp);
    }

    public float biomeMask(float clamp) {
        return NoiseUtil.map(biomeMask, 0, clamp, clamp);
    }

    public float regionMask(float clamp) {
        return NoiseUtil.map(regionMask, 0, clamp, clamp);
    }

    public boolean isAbsent() {
        return false;
    }

    public interface Visitor {

        void visit(Cell cell, int dx, int dz);
    }
}
