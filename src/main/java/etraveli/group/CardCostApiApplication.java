package etraveli.group;

import static etraveli.group.util.ScopeUtils.calculateScopeSuffix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CardCostApiApplication {

	public static void main(String[] args) {
		calculateScopeSuffix();
		SpringApplication.run(CardCostApiApplication.class, args);
	}
}
