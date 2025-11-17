package vn.spring.crud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.spring.crud.entity.Todo;

import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    Optional<Todo> findByUsername(String username);
}
