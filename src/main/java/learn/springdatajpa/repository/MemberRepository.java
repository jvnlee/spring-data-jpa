package learn.springdatajpa.repository;

import learn.springdatajpa.dto.MemberDto;
import learn.springdatajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /**
     * 값 조회:
     * 위까지는 엔티티 조회 방식. 이건 특정 필드 값 조회
     */
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    /**
     * Dto로 조회:
     * JPQL에서 DTO 타입을 사용 시, 패키지 경로를 포함한 풀네임 사용
     */
    @Query("select new learn.springdatajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    /**
     * 파라미터 바인딩 (컬렉션 타입):
     * 네임드 파라미터로 컬렉션 타입도 넘길 수 있음
     */
    @Query("select m from Member m where m.username in :usernames")
    List<Member> findByUsernames(@Param("usernames") List<String> usernames);

    /**
     * 페이징 쿼리
     *
     * @param age
     * @param pageable Pageable 인터페이스의 구현체인 PageRequest 객체에 원하는 페이지 번호, 크기, 정렬 조건 등을 넣어 넘길 수 있음
     * @return 반환 타입은 Page, Slice, List 모두 가능
     * Page인 경우, 페이징에 필요한 count 쿼리도 내부적으로 함께 나감. 덕분에 전체 페이지 개수나 전체 원소 개수를 알 수 있음
     * Slice인 경우, PageRequest에서 정한 limit 보다 1 큰 개수를 조회함. count 쿼리 없이도 다음 페이지 확인 가능 (더보기 기능 같은 것을 구현할 때 활용)
     * List인 경우, count 쿼리 없이 특정 페이지 결과 자체만 반환함
     */
    Page<Member> findPageByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    /**
     * 페이징 쿼리 - 카운트 쿼리 최적화
     *
     * 페이징 쿼리에서 반환 타입을 Page로 하는 경우 count 쿼리도 자동적으로 함께 나가는데, 만약 데이터의 개수가 굉장히 많다면
     * 전체 개수를 세는 count 쿼리로 인해 성능 이슈가 생길 수 있음. 따라서 개발자가 직접 최적화된 count 쿼리를 작성해서 지정해줄 수 있음.
     * 예시에서 원본 쿼리는 left outer join이 이루어졌는데, count 시에는 결국 해당 조인이 의미 없으므로 빼준 모습
습    */
    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m.username) from Member m")
    Page<Member> findPageByAge2(int age, Pageable pageable);

    /**
     * 벌크 업데이트 쿼리
     * 반드시 @Modifying을 붙여줘야 해당 쿼리가 데이터 변경에 관한 것임을 명시할 수 있음
     *
     * 주의:
     * 벌크 연산을 하면 영속성 컨텍스트를 건너뛰고 DB에 곧바로 쿼리를 날리기 때문에 영속성 컨텍스트와 DB 데이터간 정합성 문제가 생길 수 있음
     * 그래서 벌크 연산 호출 이후에는 반드시 영속성 컨텍스트를 깨끗이 비워줘야함
     * (한 트랜잭션 내에서 벌크 연산 이후 조회하는 케이스에 해당되고, 트랜잭션 내에서 벌크연산만 단독 수행이면 상관 없음)
     * 수동으로 clear()를 호출하는 대신, @Modifying에 clearAutomatically = true 옵션을 넣으면 자동으로 벌크 연산 이후 clear 해줌
     */
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAddAge(@Param("age") int age);

    /**
     * EntityGraph으로 페치 조인
     *
     * 페치 조인을 사용할 때, @Query에 JPQL을 작성하는 방식도 가능하지만 @EntityGraph를 활용하면 직접 JPQL을 작성하지 않고도 가능함
     * 연관 엔티티의 필드명을 attributePaths에 명시하면 됨
     *
     * findAll()은 개발자가 작성하는 커스텀 메서드가 아닌 Spring Data JPA의 기본 제공 메서드에서 활용하는 법에 대한 예시이고,
     * findAllCustom()은 @Query로 JPQL을 직접 작성해서 사용하는 메서드에서 명시적 페치 조인 없이 @EntityGraph로 페치 조인을 하는 방법에 대한 예시
     * findByAgeGreaterThan()은 쿼리 메서드 방식에서 사용하는 예시
     */
    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findAllCustom();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findByAgeGreaterThan(int age);
}
