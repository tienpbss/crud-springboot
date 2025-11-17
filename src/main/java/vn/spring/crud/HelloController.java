package vn.spring.crud;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.spring.crud.entity.Todo;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public Todo index() {
        Todo todo = new Todo(1L, "thuvienquocgia", true);
        return todo;
    }
}
