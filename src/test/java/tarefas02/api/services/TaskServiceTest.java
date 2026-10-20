package tarefas02.api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tarefas02.api.models.Task;
import tarefas02.api.models.User;
import tarefas02.api.repositories.TaskRepository;

//ativa o mockito no JUnit sem iniciar todo o Spring Boot
@ExtendWith (MockitoExtension.class)
public class TaskServiceTest {
    
    @Mock 
    private TaskRepository taskRepository;

    @Mock 
    private UserService userService;

    @InjectMocks 
    private TaskService taskService;

    @Test 
    public void deveBuscarTarefaPorIdQuandoExistir() {
        User user = new User(1L, "aluno", "senha1234");
        Task task = new Task(1L, user, "Estudar Java");

        //define o resultado que o repositorio deve devolver para esse id
        when(taskRepository.findById(10L))
            .thenReturn(Optional.of(task));

        Task resultado = taskService.findById(10L);

        assertNotNull(resultado);
        assertEquals("Estuadar Java", resultado.getDescription());

        verify(taskRepository).findById(10L); 
    }
}
