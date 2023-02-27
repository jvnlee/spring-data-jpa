package learn.springdatajpa.repository;

import learn.springdatajpa.dto.MemberDto;
import learn.springdatajpa.entity.Member;
import learn.springdatajpa.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember() {
        Member member = new Member("memberA", 20);
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).orElseThrow();

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void testAll() {
        Member memberA = new Member("A", 10);
        Member memberB = new Member("B", 10);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        Member findMemberA = memberRepository.findById(memberA.getId()).orElseThrow();
        Member findMemberB = memberRepository.findById(memberB.getId()).orElseThrow();

        assertThat(findMemberA).isEqualTo(memberA);
        assertThat(findMemberB).isEqualTo(memberB);

        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(2);

        memberRepository.delete(memberA);
        memberRepository.delete(memberB);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        Member memberA = new Member("A", 10);
        Member memberB = new Member("B", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("B", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("B");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void findByUsername() {
        Member memberA = new Member("A", 10);
        Member memberB = new Member("B", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findByUsername("A");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(memberA);
    }

    @Test
    void findByUsernameAndAge() {
        Member memberA = new Member("A", 10);
        Member memberB = new Member("B", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findMember("B", 20);
        assertThat(result.get(0)).isEqualTo(memberB);
    }

    @Test
    void findUsernameList() {
        Member memberA = new Member("A", 10);
        Member memberB = new Member("B", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<String> result = memberRepository.findUsernameList();
        assertThat(result.get(0)).isEqualTo(memberA.getUsername());
        assertThat(result.get(1)).isEqualTo(memberB.getUsername());
    }

    @Test
    void findMemberDto() {
        Member memberA = new Member("A", 10);
        memberRepository.save(memberA);

        Team teamA = new Team("TeamA");
        teamRepository.save(teamA);

        memberA.changeTeam(teamA);

        List<MemberDto> result = memberRepository.findMemberDto();
        assertThat(result.get(0).getTeamName()).isEqualTo(teamA.getName());
    }

    @Test
    void findByUsernames() {
        Member memberA = new Member("A", 10);
        Member memberB = new Member("B", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<String> usernames = Arrays.asList(memberA.getUsername(), memberB.getUsername());

        List<Member> result = memberRepository.findByUsernames(usernames);
        assertThat(result.get(0).getUsername()).isEqualTo(memberA.getUsername());
        assertThat(result.get(1).getUsername()).isEqualTo(memberB.getUsername());
    }

    @Test
    void findByAge() {
        memberRepository.save(new Member("m1", 10));
        memberRepository.save(new Member("m2", 10));
        memberRepository.save(new Member("m3", 10));
        memberRepository.save(new Member("m4", 10));
        memberRepository.save(new Member("m5", 10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findPageByAge(10, pageRequest);
        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
    }

    @Test
    void bulkAddAge() {
        memberRepository.save(new Member("m1", 10));
        memberRepository.save(new Member("m2", 19));
        memberRepository.save(new Member("m3", 20));
        memberRepository.save(new Member("m4", 24));
        memberRepository.save(new Member("m5", 40));

        int resultCount = memberRepository.bulkAddAge(20);

        List<Member> result = memberRepository.findByUsername("m5");
        Member m5 = result.get(0);

        assertThat(resultCount).isEqualTo(3);
        assertThat(m5.getAge()).isEqualTo(41);
    }

    @Test
    void findAll() {
        Team teamA = new Team("TeamA");
        Team teamB = new Team("TeamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("m1", 10, teamA);
        Member member2 = new Member("m2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findAll(); // 오버라이드 된 findAll() 사용

        for (Member member : result) {
            System.out.println("member name = " + member.getUsername());
            System.out.println("team class = " + member.getTeam().getClass()); // 프록시가 아닌 원본 엔티티 클래스
            System.out.println("team name = " + member.getTeam().getName());

        }
        /*
         쿼리 로그로 @EntityGraph가 페치 조인처럼 동작하는 것 확인.
         연관 엔티티에 대한 추가 쿼리가 나가는 N+1 문제가 발생하지 않음
         */
    }

    @Test
    void findReadOnlyByUsername() {
        Member member = new Member("m1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername("m1");
        findMember.changeUsername("m2");

        em.flush(); // 업데이트를 위해 flush 강제 호출
        // 그러나 readOnly 메서드로 조회했기 때문에 더티 체킹이 불가능하고, update 쿼리 자체가 발생하지 않음
    }

    @Test
    void findLockByUsername() {
        Member member = new Member("m1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        List<Member> result = memberRepository.findLockByUsername("m1");
        // 쿼리 로그를 살펴보면 "select ... for update" 구문이 나간 것을 확인할 수 있음
    }
}