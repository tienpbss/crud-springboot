package vn.hoidanit.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.hoidanit.todo.entity.Todo;
import vn.hoidanit.todo.service.TodoService;

import java.util.List;

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

    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo body) {
        Todo newTodo = this.todoService.createTodo(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTodo);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> getAllTodos() {
        List<Todo> todos = this.todoService.getAllTodos();
        return ResponseEntity.ok().body(todos);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo  = this.todoService.getTodoById(id);
        return ResponseEntity.ok().body(todo);
    }

//    @GetMapping("/get-todo-by-username")
//    public Todo getTodoByUsername(String id) {
//        return this.todoService.getTodoByUsername("todo updated");
//    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo  body) {
        Todo updatedTodo = this.todoService.updateTodo(id, body);
        return  ResponseEntity.ok().body(updatedTodo);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id) {
        this.todoService.deleteTodoById(id);
        return ResponseEntity.ok().body("Deleted todo have id = " + id);
    }
}
