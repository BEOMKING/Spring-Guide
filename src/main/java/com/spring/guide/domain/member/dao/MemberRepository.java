package com.spring.guide.domain.member.dao;

import com.spring.guide.domain.member.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberRepository {
    private final Map<Long, Member> memberMap = new HashMap<>();
    private Long id = 0L;

    public Member signUp(final Member member) {
        member.setId(++id);
        memberMap.put(id, member);
        return member;
    }

    public boolean existsByEmail(final String email) {
        return memberMap.values().stream()
                .anyMatch(member -> member.getEmail().equals(email));
    }
}
