package com.emelyanova.rasterisation;

import com.emelyanova.line_utils.Dot;

import java.util.ArrayList;
import java.util.List;

public class BresenhamCircleRasteriser implements Rasteriser {
    private int cx;
    private int cy;
    private int r;

    private List<Dot> dots = new ArrayList<>();

    public BresenhamCircleRasteriser(int cx, int cy, int r) {
        this.cx = cx;
        this.cy = cy;
        this.r = r;
    }

    public void put8Points(int x, int y) {
        dots.add(new Dot(cx + x, cy + y));
        dots.add(new Dot(cx - x, cy + y));
        dots.add(new Dot(cx + x, cy - y));
        dots.add(new Dot(cx - x, cy - y));
        dots.add(new Dot(cx + y, cy + x));
        dots.add(new Dot(cx - y, cy + x));
        dots.add(new Dot(cx + y, cy - x));
        dots.add(new Dot(cx - y, cy - x));
    }

    @Override
    public void rasterise() {
        int x = 0, y = r;
        int fraction = 3 - 2 * r;
        put8Points(x, y);
        while (y >= x) {
            ++x;
            if (fraction > 0) {
                --y;
                fraction += 4 * (x - y) + 10;
            } else {
                fraction += 4 * x + 6;
            }
            put8Points(x, y);
        }
    }

    @Override
    public List<Dot> getResult() {
        return dots;
    }
}
