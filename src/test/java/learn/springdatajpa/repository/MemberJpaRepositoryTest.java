package learn.springdatajpa.repository;

import learn.springdatajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository repository;

    @Test
    void testAll() {
        Member memberA = new Member("A", 10, null);
        Member memberB = new Member("B", 10, null);
        repository.save(memberA);
        repository.save(memberB);

        Member findMemberA = repository.findById(memberA.getId()).orElseThrow();
        Member findMemberB = repository.findById(memberB.getId()).orElseThrow();

        assertThat(findMemberA).isEqualTo(memberA);
        assertThat(findMemberB).isEqualTo(memberB);

        List<Member> members = repository.findAll();
        assertThat(members.size()).isEqualTo(2);

        repository.delete(memberA);
        repository.delete(memberB);

        long count = repository.count();
        assertThat(count).isEqualTo(0);
    }

}