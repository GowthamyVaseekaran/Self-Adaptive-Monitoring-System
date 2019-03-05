package monitoring.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@SpringBootApplication
@EnableScheduling
public class SelfAdaptiveMonitoringSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SelfAdaptiveMonitoringSystemApplication.class, args);
	}





}
