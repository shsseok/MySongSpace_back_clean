package com.hyeonmusic.MySongSpace.seed;

import com.hyeonmusic.MySongSpace.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SeedContext {
    private List<Member> members;
}
