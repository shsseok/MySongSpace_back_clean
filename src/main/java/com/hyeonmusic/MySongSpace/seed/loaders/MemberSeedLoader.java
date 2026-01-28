package com.hyeonmusic.MySongSpace.seed.loaders;

import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Role;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.seed.SeedConstants;
import com.hyeonmusic.MySongSpace.seed.SeedContext;
import com.hyeonmusic.MySongSpace.seed.SeedLoader;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class MemberSeedLoader implements SeedLoader {

    private final MemberRepository memberRepository;
    private final EntityManager em;

    @Override
    public String name() {
        return "MemberSeedLoader";
    }

    @Override
    public void load(SeedContext ctx) {

        List<Member> existing = memberRepository.findAll();
        int target = SeedConstants.MEMBER_TOTAL;

        log.info("[SEED][Member] existing members={}", existing.size());

        if (existing.size() >= target) {
            ctx.setMembers(existing.subList(0, target));
            log.info("[SEED][Member] reuse members={}", target);
            return;
        }

        int need = target - existing.size();
        log.info("[SEED][Member] creating members need={}", need);

        List<Member> members = new ArrayList<>(existing);

        for (int i = 1; i <= need; i++) {
            Member member = createMember(existing.size() + i);
            em.persist(member);
            members.add(member);

            if (i % 1000 == 0) {
                em.flush();
                em.clear();
                log.info("[SEED][Member] created={}/{}", i, need);
            }
        }

        em.flush();
        em.clear();

        ctx.setMembers(members);
        log.info("[SEED][Member] total members ready={}", members.size());
    }
    private Member createMember(int index) {

        String username = "user" + index;
        String email = "user" + index + "@seed.com";
        String profileImage = "https://picsum.photos/seed/" + index + "/200/200";
        String memberKey = UUID.randomUUID().toString();

        return new Member(
                username,
                email,
                profileImage,
                memberKey,
                Role.USER
        );
    }
}
