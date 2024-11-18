package com.todo.list.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.list.app.model.Todo;
import com.todo.list.app.service.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Autowired
    private ObjectMapper objectMapper; // For JSON serialization/deserialization

    @Test
    @DisplayName("GET /api/todos - Success")
    void givenTodos_whenGetAllTodos_thenReturnsTodos() throws Exception {
        // given
        Todo todo1 = new Todo(1L, "Title 1", "Description 1");
        Todo todo2 = new Todo(2L, "Title 2", "Description 2");
        BDDMockito.given(todoService.getAllTodos()).willReturn(Arrays.asList(todo1, todo2));

        // when / then
        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title 1"))
                .andExpect(jsonPath("$[1].description").value("Description 2"));
    }

    @Test
    @DisplayName("GET /api/todos/{id} - Success")
    void givenTodoId_whenGetTodoById_thenReturnsTodo() throws Exception {
        // given
        Todo todo = new Todo(1L, "Title", "Description");
        BDDMockito.given(todoService.getTodoById(1L)).willReturn(todo);

        // when / then
        mockMvc.perform(get("/api/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    @DisplayName("POST /api/todos - Success")
    void givenTodo_whenCreateTodo_thenReturnsCreatedTodo() throws Exception {
        // given
        Todo todo = new Todo(null, "Title", "Description");
        Todo savedTodo = new Todo(1L, "Title", "Description");
        BDDMockito.given(todoService.createTodo(any(Todo.class))).willReturn(savedTodo);

        // when / then
        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Description"));
    }

    @Test
    @DisplayName("PUT /api/todos/{id} - Success")
    void givenTodo_whenUpdateTodo_thenReturnsUpdatedTodo() throws Exception {
        // given
        Todo updatedDetails = new Todo(null, "Updated Title", "Updated Description");
        Todo updatedTodo = new Todo(1L, "Updated Title", "Updated Description");
        BDDMockito.given(todoService.updateTodo(eq(1L), any(Todo.class))).willReturn(updatedTodo);

        // when / then
        mockMvc.perform(put("/api/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    @DisplayName("DELETE /api/todos/{id} - Success")
    void givenTodoId_whenDeleteTodo_thenReturnsNoContent() throws Exception {
        // given
        BDDMockito.doNothing().when(todoService).deleteTodo(1L);

        // when / then
        mockMvc.perform(delete("/api/todos/1"))
                .andExpect(status().isNoContent());
    }
}