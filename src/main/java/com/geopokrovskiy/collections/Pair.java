package com.geopokrovskiy.collections;

import lombok.Data;

@Data
public class Pair<F, S> {
    public F first;
    public S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
}
