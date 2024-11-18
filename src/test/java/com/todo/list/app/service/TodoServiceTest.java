package com.todo.list.app.service;

import com.todo.list.app.model.Todo;
import com.todo.list.app.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test getAllTodos - when repository returns a list")
    void givenTodosExist_whenGetAllTodos_thenReturnListOfTodos() {
        // given
        List<Todo> expectedTodos = List.of(
                new Todo(1L, "Title 1", "Description 1"),
                new Todo(2L, "Title 2", "Description 2")
        );
        given(todoRepository.findAll()).willReturn(expectedTodos);

        // when
        List<Todo> actualTodos = todoService.getAllTodos();

        // then
        assertThat(actualTodos).isNotNull();
        assertThat(actualTodos).hasSize(2).containsExactlyElementsOf(expectedTodos);
        then(todoRepository).should(times(1)).findAll();
    }

    @Test
    @DisplayName("Test getTodoById - when Todo exists")
    void givenTodoExists_whenGetTodoById_thenReturnTodo() {
        // given
        Long id = 1L;
        Todo expectedTodo = new Todo(id, "Title 1", "Description 1");
        given(todoRepository.findById(id)).willReturn(Optional.of(expectedTodo));

        // when
        Todo actualTodo = todoService.getTodoById(id);

        // then
        assertThat(actualTodo).isNotNull();
        assertThat(actualTodo).isEqualTo(expectedTodo);
        then(todoRepository).should(times(1)).findById(id);
    }

    @Test
    @DisplayName("Test getTodoById - when Todo does not exist")
    void givenTodoDoesNotExist_whenGetTodoById_thenThrowException() {
        // given
        Long id = 1L;
        given(todoRepository.findById(id)).willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> todoService.getTodoById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todo with id: 1 not found");

        then(todoRepository).should(times(1)).findById(id);
    }

    @Test
    @DisplayName("Test createTodo - when saving a new Todo")
    void givenNewTodo_whenCreateTodo_thenTodoIsSaved() {
        // given
        Todo newTodo = new Todo(null, "New Title", "New Description");
        Todo savedTodo = new Todo(1L, "New Title", "New Description");
        given(todoRepository.save(newTodo)).willReturn(savedTodo);

        // when
        Todo actualTodo = todoService.createTodo(newTodo);

        // then
        assertThat(actualTodo).isNotNull();
        assertThat(actualTodo.getId()).isEqualTo(1L);
        assertThat(actualTodo).isEqualTo(savedTodo);
        then(todoRepository).should(times(1)).save(newTodo);
    }

    @Test
    @DisplayName("Test updateTodo - when updating an existing Todo")
    void givenExistingTodo_whenUpdateTodo_thenTodoIsUpdated() {
        // given
        Long id = 1L;
        Todo existingTodo = new Todo(id, "Old Title", "Old Description");
        Todo updatedDetails = new Todo(null, "Updated Title", "Updated Description");
        Todo updatedTodo = new Todo(id, "Updated Title", "Updated Description");

        given(todoRepository.findById(id)).willReturn(Optional.of(existingTodo));
        given(todoRepository.save(existingTodo)).willReturn(updatedTodo);

        // when
        Todo actualTodo = todoService.updateTodo(id, updatedDetails);

        // then
        assertThat(actualTodo).isNotNull();
        assertThat(actualTodo).isEqualTo(updatedTodo);
        assertThat(actualTodo.getTitle()).isEqualTo("Updated Title");
        assertThat(actualTodo.getDescription()).isEqualTo("Updated Description");

        then(todoRepository).should(times(1)).findById(id);
        then(todoRepository).should(times(1)).save(existingTodo);
    }

    @Test
    @DisplayName("Test deleteTodo - when Todo exists")
    void givenExistingTodo_whenDeleteTodo_thenTodoIsDeleted() {
        // given
        Long id = 1L;
        Todo existingTodo = new Todo(id, "Title", "Description");
        given(todoRepository.findById(id)).willReturn(Optional.of(existingTodo));

        // when
        todoService.deleteTodo(id);

        // then
        then(todoRepository).should(times(1)).findById(id);
        then(todoRepository).should(times(1)).delete(existingTodo);
    }

    @Test
    @DisplayName("Test deleteTodo - when Todo does not exist")
    void givenTodoDoesNotExist_whenDeleteTodo_thenThrowException() {
        // given
        Long id = 1L;
        given(todoRepository.findById(id)).willReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> todoService.deleteTodo(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Todo with id: 1 not found");

        then(todoRepository).should(times(1)).findById(id);
        then(todoRepository).shouldHaveNoMoreInteractions();
    }
}