package vn.hoidanit.todo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.todo.entity.Todo;
import vn.hoidanit.todo.service.TodoService;

@RestController
public class TodoController {
    private TodoService todoService;
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }
    @GetMapping("/")
    public String index() {
        return "Hello Java Spring Boot!";
    }

    @GetMapping("/create-todo")
    public String createTodo() {
        return this.todoService.createTodo().toString();
    }

    @GetMapping("/get-all-todos")
    public String getAllTodos() {
        return this.todoService.toString();
    }

    @GetMapping("/get-todo-by-id")
    public Todo getTodoById(String id) {
        return this.todoService.getTodoById(3L);
    }

    @GetMapping("/get-todo-by-username")
    public Todo getTodoByUsername(String id) {
        return this.todoService.getTodoByUsername("todo updated");
    }

    @GetMapping("/update-todo")
    public Todo updateTodo(String id) {
        return this.todoService.updateTodo(3L);
    }

    @GetMapping("/delete-todo")
    public String deleteTodo(String id) {
        this.todoService.deleteTodoById(3L);
        return "Deleted todo have id = 3";
    }
}
