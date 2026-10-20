package tarefas02.api.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import tarefas02.api.models.Task;
import tarefas02.api.models.User;
import tarefas02.api.repositories.TaskRepository;
import tarefas02.api.repositories.UserRepository;

@SpringBootTest //inicia a aplicação para testes de integração entre as camadas
@AutoConfigureMockMvc(addFilters = false) //permite simular requisições HTTP sem iniciar o servidor externo
@ActiveProfiles("test") //usa as configurações do application-test.properties
@Transactional //desfaz as alterações no banco de dados ao final de cada teste
public class TaskControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void prepararDados() {
        //cada teste começa com dados conhecidos
        taskRepository.deleteAll();
        userRepository.deleteAll();

        //cria um usuario para associar as tarefas
        user = userRepository.save(
            new User(null, "usuario_teste", "senha123456")
        );
    }

    @Test 
    public void deveConsultarTarefaEDevolverJson() throws Exception {
        Task task = taskRepository.save(
            new Task(null, user, "Tarefa de teste")
        );

        //executa GET e valida status HTTP, tipo de conteudo e JSON
        mockMvc.perform(get("/tasks/{id}", task.getId()))
            .andExpect(status().isOk())
            .andExpect(content()
                .contentType(
                    MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(task.getId()))
            .andExpect(jsonPath("$.description")
                .value("Estudar testes")); 
    }

    @Test 
    public void deveCriarTarefaEDevolverStatus201EHeaderLocation() throws Exception {
        //monta o JSON que sera enviado no corpo do POST
        String json = "{\"description\":\"Nova tarefa\","
            +"\"user\":{\"id\":" + user.getId() + "}}";

        //HTTP 201 indica que o recurso foi criado
        //Location informa o endereço do recurso criado
        mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated())
            .andExpect(header().string(
                "Location", containsString("/tasks/")));
    }

    @Test 
    public void deveExcluirTarefaEDevolverStatus204() throws Exception {
        Task task = taskRepository.save(
            new Task(null, user, "Tarefa para excluir")
        );

        //HTTP 204 indica que a requisição foi processada com sucesso
        mockMvc.perform(delete("/tasks/{id}", task.getId()))
            .andExpect(status().isNoContent());
    }

    @Test 
    public void deveRetornarErroAoCriarTarefaSemDescricao() throws Exception {
        String json = "{\"description\":\"\","
            +"\"user\":{\"id\":" + user.getId() + "}}";

        //envia uma descrição vazia e espera HTTP 400
        mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
    }
}
