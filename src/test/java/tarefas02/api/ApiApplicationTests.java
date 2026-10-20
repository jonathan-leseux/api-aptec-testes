package tarefas02.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//verifica se a aplicação Spring Boot consegue iniciar no ambiente de teste
@SpringBootTest
@ActiveProfiles("test")
class ApiApplicationTests {

	@Test
	void contextLoads() {
		//se o contexto iniciar sem erros, o teste passa
	}

}
