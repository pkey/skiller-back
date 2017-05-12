package lt.swedbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan({"lt.swedbank.service.auth.AuthenticationService"})
@SpringBootApplication
@EnableAutoConfiguration
public class SkillerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillerApplication.class, args);
	}
}
