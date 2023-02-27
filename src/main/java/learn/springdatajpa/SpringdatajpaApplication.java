package learn.springdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class SpringdatajpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringdatajpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorAware() {
		/*
		등록이나 수정이 발생할 때 마다 auditorAware()가 호출되어 행위의 주체가 누군지 반환하고,
		반환된 값은 BaseEntity의 createdBy와 lastModifiedBy의 값으로 주입됨

		예시로 랜덤 UUID를 사용했지만, 실제로는 사용자 세션 등으로부터 신원 데이터를 가져와 사용함
		 */
		return () -> Optional.of(UUID.randomUUID().toString());
	}

}
