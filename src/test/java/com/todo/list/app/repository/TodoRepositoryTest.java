package com.todo.list.app.repository;

import com.todo.list.app.model.Todo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    @DisplayName("Test save and find by id - when Todo is saved")
    void givenTodo_whenSave_thenTodoIsSavedAndFound() {
        // given
        Todo todo = new Todo(null, "Test Title", "Test Description");

        // when
        Todo savedTodo = todoRepository.save(todo);
        Optional<Todo> foundTodo = todoRepository.findById(savedTodo.getId());

        // then
        assertThat(savedTodo).isNotNull();
        assertThat(savedTodo.getId()).isNotNull();
        assertThat(foundTodo).isPresent();
        assertThat(foundTodo.get()).isEqualTo(savedTodo);
    }

    @Test
    @DisplayName("Test find all - when multiple Todos exist")
    void givenMultipleTodos_whenFindAll_thenReturnAllTodos() {
        // given
        Todo todo1 = new Todo(null, "Title 1", "Description 1");
        Todo todo2 = new Todo(null, "Title 2", "Description 2");
        todoRepository.save(todo1);
        todoRepository.save(todo2);

        // when
        List<Todo> todos = todoRepository.findAll();

        // then
        assertThat(todos).isNotNull();
        assertThat(todos).hasSize(2);
        assertThat(todos).extracting("title").contains("Title 1", "Title 2");
    }

    @Test
    @DisplayName("Test update - when Todo is updated")
    void givenExistingTodo_whenUpdate_thenTodoIsUpdated() {
        // given
        Todo todo = new Todo(null, "Original Title", "Original Description");
        Todo savedTodo = todoRepository.save(todo);

        // when
        savedTodo.setTitle("Updated Title");
        savedTodo.setDescription("Updated Description");
        Todo updatedTodo = todoRepository.save(savedTodo);
        Optional<Todo> foundTodo = todoRepository.findById(updatedTodo.getId());

        // then
        assertThat(updatedTodo).isNotNull();
        assertThat(foundTodo).isPresent();
        assertThat(foundTodo.get().getTitle()).isEqualTo("Updated Title");
        assertThat(foundTodo.get().getDescription()).isEqualTo("Updated Description");
    }

    @Test
    @DisplayName("Test delete - when Todo is deleted")
    void givenExistingTodo_whenDelete_thenTodoIsDeleted() {
        // given
        Todo todo = new Todo(null, "Title", "Description");
        Todo savedTodo = todoRepository.save(todo);

        // when
        todoRepository.delete(savedTodo);
        Optional<Todo> foundTodo = todoRepository.findById(savedTodo.getId());

        // then
        assertThat(foundTodo).isNotPresent();
    }

    @Test
    @DisplayName("Test find by id - when Todo does not exist")
    void givenNonExistingId_whenFindById_thenReturnEmptyOptional() {
        // when
        Optional<Todo> foundTodo = todoRepository.findById(999L);

        // then
        assertThat(foundTodo).isNotPresent();
    }
}