package learn.springdatajpa.repository;

import learn.springdatajpa.entity.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * 순수 JPA를 사용하는 리포지토리
 */
@Repository
@RequiredArgsConstructor
public class TeamJpaRepository {

    private final EntityManager em;

    public Team save(Team team) {
        em.persist(team);
        return team;
    }

    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(em.find(Team.class, id));
    }

    public List<Team> findAll() {
        return em.createQuery("select m from Team m", Team.class)
                .getResultList();
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public long count() {
        return em.createQuery("select count(t) from Team t", Long.class)
                .getSingleResult();
    }

}
