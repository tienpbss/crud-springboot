package vn.hoidanit.todo.service;

import org.springframework.stereotype.Service;
import vn.hoidanit.todo.entity.Todo;
import vn.hoidanit.todo.repository.TodoRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private TodoRepository todoRepository;
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    public Todo createTodo() {
        Todo todo = new Todo("thuvienquocgia", true);
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

    public Todo updateTodo(Long id)  {
        Todo todo = this.todoRepository.findById(id).orElse(null);
        if (todo != null) {
            todo.setUsername("todo updated");
            this.todoRepository.save(todo);
            return todo;
        }
        return null;
    }

    public void deleteTodoById(Long id) {
        this.todoRepository.deleteById(id);
    }
}
