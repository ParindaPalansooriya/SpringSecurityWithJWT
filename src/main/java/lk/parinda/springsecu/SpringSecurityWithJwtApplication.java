package lk.parinda.springsecu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableScheduling
public class SpringSecurityWithJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityWithJwtApplication.class, args);
	}

}
