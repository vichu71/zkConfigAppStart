package cestel.sercom;

import javax.validation.Validation;
import javax.validation.Validator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

//para probar en local
public class ZkConfigAppStartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZkConfigAppStartApplication.class, args);
	}
//para la generacioon de war para el servidor de aplicaciones
//public class ZkConfigAppStartApplication extends SpringBootServletInitializer{	
//	@Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(ZkConfigAppStartApplication.class);
//    }
	
	@Bean
	public Validator validator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}
}
