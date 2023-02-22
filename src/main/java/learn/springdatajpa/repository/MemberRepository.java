package learn.springdatajpa.repository;

import learn.springdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 쿼리 메서드 기능:
     * 미리 약속된 규칙에 맞춰 메서드의 이름을 지으면 JPQL 쿼리를 자동 생성해줌
     * 예시: username이 username과 같고, age가 age보다 큰 Member 조회
     *
     * 장점: 실수로 엔티티에 존재하지 않는 필드를 가지고 작명한 경우, 컴파일 에러가 발생함
     * 단점: 사용할 필드가 많아지면 메서드 이름이 너무 복잡하고 길어짐
     */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    /**
     * 네임드 쿼리:
     * 엔티티 클래스에 @NamedQuery로 규정한 JPQL 쿼리를 해당 메서드와 매핑함
     * 해당 JPQL에 named parameter가 있는 경우, @Param으로 파라미터 이름과 매핑해주어야함
     *
     * @Query() 생략 가능:
     * 이미 정의되어있는 네임드 쿼리의 이름(Member.findByUsername)과, 리포지토리의 타입 및
     * 메서드명(Member + findByUsername)으로 해당 메서드가 네임드 쿼리와 자동 매핑됨.
     * 만약 네임드 쿼리가 없다면 쿼리 메서드 기능으로 생성한 쿼리가 매핑됨.
     *
     * 장점: @NamedQuery에 정의된 쿼리는 애플리케이션 로딩 시점에 파싱되고 문법 오류가 있으면 예외가 발생함
     * 단점: 쿼리가 정의된 곳(엔티티 클래스)과 매핑된 곳이 떨어져 있음
     */
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    /**
     * 네밈드 쿼리2:
     * 인터페이스 메서드 쪽에 직접 JPQL 쿼리를 작성해서 매핑함 (이름 없는 네임드 쿼리)
     * 네임드 쿼리의 장점을 유지하면서 단점을 커버한 방식
     *
     * 실무에서 많이 사용하는 기능
     * 간단한 쿼리는 쿼리 메서드로, 복잡한 쿼리는 이 방식으로 해결함
     */
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findMember(@Param("username") String username, @Param("age") int age);

}
