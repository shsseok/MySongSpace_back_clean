package com.hyeonmusic.MySongSpace.seed.loaders;

import com.hyeonmusic.MySongSpace.entity.Genre;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Mood;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.seed.SeedConstants;
import com.hyeonmusic.MySongSpace.seed.SeedContext;
import com.hyeonmusic.MySongSpace.seed.SeedLoader;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class TrackSeedLoader implements SeedLoader {

    private final EntityManager em;

    private static final String[] COMMON_KEYWORDS = {
            "희망찬", "신나는", "잔잔한", "몽환적인", "어두운", "사랑스러운",
            "기타", "피아노", "드럼", "신디사이저",
            "BPM", "CITYPOP", "JAZZ", "HIPHOP", "ROCK", "EDM"
    };

    @Override
    public String name() {
        return "TrackSeedLoader";
    }

    @Override
    public void load(SeedContext ctx) {
        List<Member> members = ctx.getMembers();
        if (members == null || members.isEmpty()) {
            throw new IllegalStateException("TrackSeedLoader 실행 전 MemberSeedLoader가 먼저 실행되어야 합니다.");
        }

        int total = SeedConstants.TRACK_TOTAL;
        int batch = SeedConstants.BATCH_SIZE;

        Genre[] genres = Genre.values();
        Mood[] moods = Mood.values();

        long start = System.currentTimeMillis();

        for (int i = 1; i <= total; i++) {
            Member member = members.get(i % members.size());

            List<Genre> randomGenres = getRandomEnums(genres, 1, 3);
            List<Mood> randomMoods = getRandomEnums(moods, 1, 3);

            Track track = new Track(
                    buildTitle(i),
                    buildDescription(i),
                    "/music/" + uuidShort() + ".mp3",
                    "/cover/" + uuidShort() + ".jpg",
                    randomDuration(),
                    randomGenres,
                    randomMoods,
                    member,
                    randomPlayCount()
            );

            em.persist(track);

            if (i % batch == 0) {
                em.flush();
                em.clear();
                logProgress(i, total, start);
            }
        }

        em.flush();
        em.clear();

        log.info("[SEED][Track] done total={}", total);
    }

    private String buildTitle(int i) {
        ThreadLocalRandom r = ThreadLocalRandom.current();
        String k1 = COMMON_KEYWORDS[r.nextInt(COMMON_KEYWORDS.length)];
        String k2 = COMMON_KEYWORDS[r.nextInt(COMMON_KEYWORDS.length)];
        int bpm = r.nextInt(60, 181);
        return k1 + " " + k2 + " BPM" + bpm + " Track-" + i;
    }

    private String buildDescription(int i) {
        ThreadLocalRandom r = ThreadLocalRandom.current();

        StringBuilder sb = new StringBuilder(600);
        sb.append("이 트랙은 검색 성능 테스트용 더미 데이터입니다. index=").append(i).append(". ");

        int keywordCount = r.nextInt(3, 7);
        sb.append("keywords=");
        for (int j = 0; j < keywordCount; j++) {
            sb.append(COMMON_KEYWORDS[r.nextInt(COMMON_KEYWORDS.length)]);
            if (j < keywordCount - 1) sb.append(",");
        }
        sb.append(". content=").append(randomText(250));

        return sb.toString();
    }

    private int randomDuration() {
        return ThreadLocalRandom.current().nextInt(45, 420);
    }

    private <E extends Enum<E>> List<E> getRandomEnums(E[] values, int min, int max) {
        int count = ThreadLocalRandom.current().nextInt(min, max + 1);
        List<E> list = new ArrayList<>(Arrays.asList(values));
        Collections.shuffle(list);
        return list.subList(0, Math.min(count, list.size()));
    }

    private String randomText(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789 ";
        ThreadLocalRandom r = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) sb.append(chars.charAt(r.nextInt(chars.length())));
        return sb.toString();
    }

    private String uuidShort() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    private void logProgress(int inserted, int total, long start) {
        long elapsed = System.currentTimeMillis() - start;
        double perSec = inserted / (elapsed / 1000.0 + 0.001);
        log.info("[SEED][Track] inserted={}/{} ({} rows/sec)", inserted, total, (int) perSec);
    }

    private long randomPlayCount() {
        int p = ThreadLocalRandom.current().nextInt(100);
        if (p < 70) return ThreadLocalRandom.current().nextLong(0, 200);        // 70%: 0~199
        if (p < 95) return ThreadLocalRandom.current().nextLong(200, 5_000);    // 25%: 200~4999
        return ThreadLocalRandom.current().nextLong(5_000, 200_000);            // 5% : 5천~20만
    }

}
