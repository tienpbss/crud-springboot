package vn.hoidanit.todo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.todo.entity.Todo;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public Todo index() {
        Todo todo = new Todo(1L, "thuvienquocgia", true);
        return todo;
    }
}
