package com.emelyanova.rasterisation;

import com.emelyanova.line_utils.Dot;

import java.util.ArrayList;
import java.util.List;

public class DDALineRasteriser implements Rasteriser {
    private int x0;
    private int y0;
    private int x1;
    private int y1;

    private List<Dot> dots = new ArrayList<>();

    public DDALineRasteriser(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    @Override
    public void rasterise() {
        int deltaX = x1 - x0;
        int deltaY = y1 - y0;
        int steps = Math.max(Math.abs(deltaX), Math.abs(deltaY));

        float dx = 1.f * deltaX / steps;
        float dy = 1.f * deltaY / steps;

        int i;
        float x = x0, y = y0;
        for (i = 0; i <= steps; ++i) {
            dots.add(new Dot(Math.round(x), Math.round(y)));
            x += dx;
            y += dy;
        }
    }

    @Override
    public List<Dot> getResult() {
        return dots;
    }
}
