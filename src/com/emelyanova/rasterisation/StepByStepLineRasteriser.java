package com.emelyanova.rasterisation;

import com.emelyanova.line_utils.Dot;

import java.util.ArrayList;
import java.util.List;

public class StepByStepLineRasteriser implements Rasteriser {
    private int x0;
    private int y0;
    private int x1;
    private int y1;

    private List<Dot> dots = new ArrayList<>();

    public StepByStepLineRasteriser(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    @Override
    public void rasterise() {
        int x, y;
        double k;
        if (Math.abs(x1 - x0) >= Math.abs(y1 - y0)) {
            k = 1. * (y1 - y0) / (x1 - x0);
            for (x = Math.min(x0, x1); x <= Math.max(x0, x1); ++x) {
                y = (int) Math.round(k * (x - x0) + y0);
                dots.add(new Dot(x, y));
            }
        } else {
            k = 1. * (x1 - x0) / (y1 - y0);
            for (y = Math.min(y0, y1); y <= Math.max(y0, y1); ++y) {
                x = (int) Math.round(k * (y - y0) + x0);
                dots.add(new Dot(x, y));
            }
        }
    }

    @Override
    public List<Dot> getResult() {
        return dots;
    }
}
