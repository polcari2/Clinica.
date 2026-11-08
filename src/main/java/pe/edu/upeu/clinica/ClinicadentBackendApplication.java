package pe.edu.upeu.clinica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ClinicadentBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicadentBackendApplication.class, args);
	}

}
