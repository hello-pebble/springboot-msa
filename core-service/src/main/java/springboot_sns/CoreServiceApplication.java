package springboot_sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ConfigurationPropertiesScan
public class CoreServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreServiceApplication.class, args);
	}

}
