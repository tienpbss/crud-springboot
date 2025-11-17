package vn.spring.crud.service;

import org.springframework.stereotype.Service;
import vn.spring.crud.entity.Todo;
import vn.spring.crud.repository.TodoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    public Todo createTodo(Todo todo) {
        return this.todoRepository.save(todo);
    }

    public List<Todo> getAllTodos() {
        return this.todoRepository.findAll();
    }

    public Todo getTodoById(Long id) {
        Optional<Todo> todo = this.todoRepository.findById(id);
        return todo.orElse(null);
    }

    public Todo getTodoByUsername(String username) {
        Optional<Todo> todo = this.todoRepository.findByUsername(username);
        return todo.orElse(null);
    }

    public Todo updateTodo(Long id, Todo body)  {
        Todo todo = this.todoRepository.findById(id).orElse(null);
        if (todo != null) {
            todo.setUsername(body.getUsername());
            todo.setDone(body.isDone());
            return this.todoRepository.save(todo);
        }
        return null;
    }

    public void deleteTodoById(Long id) {
        this.todoRepository.deleteById(id);
    }
}
