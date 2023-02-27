package learn.springdatajpa.repository;

import learn.springdatajpa.entity.Member;

import java.util.List;

public interface MemberCustomRepository {
    /*
    사용자 정의 기능

    Spring Data JPA가 제공하는 인터페이스 형태의 리포지토리 외에 타 DB 접근 기술을 같이 사용하고자 할 때는 커스텀 메서드를 위한 인터페이스를 따로 생성.
    여기에 메서드를 정의하고, 구현 클래스를 생성해서 사용하고자 하는 기술에 맞게 구현함.
    그리고 나서 Spring Data JPA의 인터페이스 리포지토리가 이 인터페이스를 상속받게 하면 됨.

    실무에서는 주로 Spring Data JPA와 QueryDSL을 함께 사용하기 위해 이러한 방식을 사용함.
     */

    List<Member> findMemberCustom();

}
