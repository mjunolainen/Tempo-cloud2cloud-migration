package cloud2cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Cloud2cloudMigrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(Cloud2cloudMigrationApplication.class, args);
	}

}
