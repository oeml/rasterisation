package com.emelyanova.ui;

public class GraphContext {
    public static int SIZE = 720;

    private int xBottomLeft;
    private int yBottomLeft;
    private int xTopRight;
    private int yTopRight;

    public int width() {
        return Math.abs(xTopRight - xBottomLeft);
    }

    public int height() {
        return Math.abs(yTopRight - yBottomLeft);
    }

    public int scale() {
        return SIZE / Math.max(width(), height());
    }

    public int getXBottomLeft() {
        return xBottomLeft;
    }

    public int getYBottomLeft() {
        return yBottomLeft;
    }

    public int getXTopRight() {
        return xTopRight;
    }

    public int getYTopRight() {
        return yTopRight;
    }

    public void reset(int xBottomLeft, int yBottomLeft, int xTopRight, int yTopRight) {
        this.xBottomLeft = xBottomLeft;
        this.yBottomLeft = yBottomLeft;
        this.xTopRight = xTopRight;
        this.yTopRight = yTopRight;
    }

    public GraphContext() {}
}
