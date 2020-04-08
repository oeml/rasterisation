package com.emelyanova.rasterisation;

import com.emelyanova.line_utils.Dot;

import java.util.List;

public interface Rasteriser {
    void rasterise();
    List<Dot> getResult();
}
