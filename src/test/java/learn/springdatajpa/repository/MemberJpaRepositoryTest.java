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
    void crudAndCount() {
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

    @Test
    void paging() {
        repository.save(new Member("m1", 10));
        repository.save(new Member("m2", 10));
        repository.save(new Member("m3", 10));
        repository.save(new Member("m4", 10));
        repository.save(new Member("m5", 10));

        List<Member> members = repository.findByAge(10, 0, 3);
        long totalCount = repository.totalCount(10);

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    void bulkAddAge() {
        repository.save(new Member("m1", 10));
        repository.save(new Member("m2", 19));
        repository.save(new Member("m3", 20));
        repository.save(new Member("m4", 24));
        repository.save(new Member("m5", 40));

        int resultCount = repository.bulkAddAge(20);

        assertThat(resultCount).isEqualTo(3);
    }

}