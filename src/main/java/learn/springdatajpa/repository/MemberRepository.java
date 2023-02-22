package learn.springdatajpa.repository;

import learn.springdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

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


}
