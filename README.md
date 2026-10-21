 API de Gerenciamento de Tarefas 

• Descrição 
Este projeto é uma API REST para gerenciamento de tarefas vinculadas a usuários. Foi desenvolvido em Java com Spring Boot e utiliza uma arquitetura organizada em controllers, services, repositories e models. O projeto também possui configuração de segurança com Spring Security e JWT. 

•  Tecnologias utilizadas
- Java 21 - Spring Boot 3.5.5 
- Spring Web para criação dos endpoints REST 
- Spring Data JPA para persistência de dados 
- Spring Security e JWT para segurança/autenticação 
- Bean Validation para validação dos dados recebidos 
- MySQL como banco de dados da aplicação 
- Maven para gerenciamento do projeto 
- JUnit 5, Mockito e Spring Boot Test para testes automatizados 
- JaCoCo para medir a cobertura de código 

• Organização do projeto 

src/
 main/
   java/tarefas02/api/
      configs/   # Configurações da aplicação e segurança 
      controllers/   # Endpoints REST 
      models/  # Entidades, como User e Task 
      repositories/   # Acesso aos dados com Spring Data JPA 
      security/    # Componentes relacionados a JWT 
      services/    # Regras de negócio 
      resources/     # Configurações da aplicação 
test/ 
      java/ # Testes automatizados 

• Endpoints de tarefas 

A classe TaskController define os seguintes endpoints: 

| Método | Rota | Finalidade 

•GET  /task/{id} Busca uma tarefa pelo ID  
•GET /task/user/{userId} Lista as tarefas de um usuário 
•POST /task  Cria uma tarefa  
•PUT /task/{id}  Atualiza a descrição de uma tarefa 
•DELETE  /task/{id}  Exclui uma tarefa 

Os endpoints podem estar sujeitos às regras de autenticação e autorização configuradas na aplicação. 

• Configuração e execução 

1. Instale o JDK 21. 
2. Configure o banco de dados MySQL e as propriedades da aplicação em src/main/resources/application.properties. 
3. Na raiz do projeto, execute: 

Linux/macOS: 
bash 
./mvnw spring-boot:run 

Windows:
 powershell 
.\mvnw.cmd spring-boot:run

• Executar os testes Para executar a suíte de testes: 

Linux/macOS: 
bash 
./mvnw test 

Windows: 
powershell 
.\mvnw.cmd test 

 Para limpar, executar os testes e gerar o relatório JaCoCo (após a configuração do plugin no pom.xml): 

powershell 
.\mvnw.cmd clean verify 
 
O relatório HTML do JaCoCo normalmente fica em: 

target/site/jacoco/index.html 

