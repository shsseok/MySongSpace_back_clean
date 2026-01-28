package com.hyeonmusic.MySongSpace.seed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeedRunner implements ApplicationRunner {

    private final List<SeedLoader> loaders;

    @Value("${seed.enabled:false}")
    private boolean seedEnabled;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {

        if (!seedEnabled) {
            log.info("[SEED] seed.enabled=false -> skip");
            return;
        }

        SeedContext ctx = new SeedContext();

        log.info("=== SEED START (loaders={}) ===", loaders.size());

        long totalStart = System.currentTimeMillis();

        for (SeedLoader loader : loaders) {
            long s = System.currentTimeMillis();
            log.info("[SEED] start: {}", loader.name());

            loader.load(ctx);

            long e = System.currentTimeMillis();
            log.info("[SEED] done : {} ({} ms)", loader.name(), (e - s));
        }

        log.info("=== SEED DONE (total {} ms) ===",
                System.currentTimeMillis() - totalStart);
    }
}
