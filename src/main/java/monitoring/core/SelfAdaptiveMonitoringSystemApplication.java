package monitoring.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * main class : spring application.
 */

@SpringBootApplication
@EnableScheduling
public class SelfAdaptiveMonitoringSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SelfAdaptiveMonitoringSystemApplication.class, args);
	}





}
