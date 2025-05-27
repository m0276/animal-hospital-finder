package MJ.animal_Hospital_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AnimalHospitalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimalHospitalServiceApplication.class, args);
	}

}
