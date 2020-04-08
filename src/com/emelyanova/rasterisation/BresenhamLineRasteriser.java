package com.emelyanova.rasterisation;

import com.emelyanova.line_utils.Dot;

import java.util.ArrayList;
import java.util.List;

public class BresenhamLineRasteriser implements Rasteriser {
    private int x0;
    private int y0;
    private int x1;
    private int y1;

    private List<Dot> dots = new ArrayList<>();

    public BresenhamLineRasteriser(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    @Override
    public void rasterise() {
        int fraction;
        int dx = x1 - x0, dy = y1 - y0;
        int x = x0, y = y0;
        int stepX = 1, stepY = 1;
        if (dy < 0) {
            dy = -dy;
            stepY = -1;
        }
        if (dx < 0) {
            dx = -dx;
            stepX = -1;
        }
        dy <<= 1;
        dx <<= 1;
        dots.add(new Dot(x, y));

        if (dx > dy) {
            fraction = dy - (dx >> 1);
            while (x != x1) {
                x += stepX;
                if (fraction >= 0) {
                    y += stepY;
                    fraction -= dx;
                }
                fraction += dy;
                dots.add(new Dot(x, y));
            }
        } else {
            fraction = dx - (dy >> 1);
            while (y != y1) {
                if (fraction >= 0) {
                    x += stepX;
                    fraction -= dy;
                }
                y += stepY;
                fraction += dx;
                dots.add(new Dot(x, y));
            }
        }
    }

    @Override
    public List<Dot> getResult() {
        return dots;
    }
}
