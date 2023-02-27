package learn.springdatajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class JpaBaseEntity {
    /*
     등록일, 수정일은 원활한 운영을 위해 모든 데이터에 필요한 항목임
     따라서 @MappedSuperclass로 만들어 여러 엔티티가 상속받아 사용할 수 있게 하면 편리함
     */

    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @PrePersist // 영속성 컨텍스트에 영속화 전에 호출
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updatedDate = now;
    }

    @PreUpdate // 영속성 컨텍스트에 업데이트 전에 호출
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
