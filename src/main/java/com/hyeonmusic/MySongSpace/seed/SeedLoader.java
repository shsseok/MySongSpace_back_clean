package com.hyeonmusic.MySongSpace.seed;

public interface SeedLoader {
    String name();
    void load(SeedContext ctx);
}
