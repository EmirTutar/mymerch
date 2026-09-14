package tutar.techs.mymerch;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MymerchApplication {

	public static void main(String[] args) {
		// Configure dotenv to load environment variables
		Dotenv dotenv = Dotenv.configure()
				.directory(".")
				.ignoreIfMissing()
				.load();

		dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
		
		
		SpringApplication.run(MymerchApplication.class, args);
	}

}
