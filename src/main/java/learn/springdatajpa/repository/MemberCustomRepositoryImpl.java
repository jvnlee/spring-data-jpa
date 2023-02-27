package learn.springdatajpa.repository;

import learn.springdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final EntityManager em;

    /**
     * Spring Data JPA와 함께 순수 JPA를 사용하고 싶다고 가정.
     * 순수 JPA를 사용하는 커스텀 메서드
     */
    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

}
