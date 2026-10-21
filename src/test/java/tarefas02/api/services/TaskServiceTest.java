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
        assertEquals("Estudar Java", resultado.getDescription());

        verify(taskRepository).findById(10L); 
    }

    @Test 
    public void deveLancarExcecaoQuandoTarefaNaoExistir() {
        when(taskRepository.findById(99L))
            .thenReturn(Optional.empty());

        //verifica se o serviço lança uma exceção para um id inexistente
        RuntimeException erro = assertThrows(
            RuntimeException.class, 
            () -> taskService.findById(99L)
        );

        assertTrue(erro.getMessage().contains("Tarefa nao encontrada"));
    }

    @Test 
    public void deveListarTarefasDeUmUsuario() {
        when(userService.findById(1L))
            .thenReturn(new User(1L, "aluno", "senha1234"));

        when(taskRepository.findByUser_Id(1L))
            .thenReturn(List.of(
                new Task(10L,
                    new User(1L, "aluno", "senha1234"),
                    "Estudar Java")
            ));
        
        List<Task> resultado = taskService.findByUserId(1L);

        assertEquals(1, resultado.size());
        assertEquals("Estudar Java",
            resultado.get(0).getDescription());

        verify(userService).findById(1L);
    }

    @Test 
    public void deveCriarTarefaAssociadaAoUsuarioExistente() {
        User user = new User(1L, "aluno", "senha1234");
        Task nova = new Task(50L, user, "Fazer trabalho");

        when(userService.findById(1L)).thenReturn(user);

        //simula o save e atribui um id à tarefa salva
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task salva = invocation.getArgument(0);
            salva.setId(20L);
            return salva;
        });

        Task resultado = taskService.create(nova);

        assertEquals(20L, resultado.getId());
        assertEquals(user, resultado.getUser());

        //confirma que o serviço solicitou a gravação da tarefa
        verify(taskRepository).save(nova);
    }

    @Test 
    public void deveAtualizarDescricaoDeTarefaExistente() {
        User user = new User(1L, "aluno", "senha1234");
        Task existente = new Task(10L, user, "Descrição antiga");
        Task alteracao = new Task(10L, user, "Descrição nova");

        when(taskRepository.findById(10L))
            .thenReturn(Optional.of(existente));

        when(taskRepository.save(any(Task.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        Task resultado = taskService.update(alteracao);

        assertEquals("Descrição nova", resultado.getDescription());

        //confirma que o objeto encontrado foi salvo após a alteração
        verify(taskRepository).save(existente);
    }

    @Test 
    public void deveExcluirTarefaExistente() {
        Task existente = new Task(
            10L,
            new User(1L, "aluno", "senha1234"),
            "Apagar tarefa"
        );

        when(taskRepository.findById(10L))
            .thenReturn(Optional.of(existente));

        taskService.delete(10L);

        verify(taskRepository).deleteById(10L);
    }
}
