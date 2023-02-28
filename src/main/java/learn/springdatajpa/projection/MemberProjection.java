package learn.springdatajpa.projection;

public interface MemberProjection {
    /*
    get~() 메서드의 ~부분과 네이티브 쿼리에서 지정한 필드 이름을 동일하게 맞춰주면 됨
    >> select m.member_id as id, m.username, t.name as teamName...
     */

    Long getId();

    String getUsername();

    String getTeamName();

}
