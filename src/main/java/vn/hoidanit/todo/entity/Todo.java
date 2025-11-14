package vn.hoidanit.todo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private boolean done;

    public Todo() {
    }

    public Todo(String username, boolean done) {
        this.username = username;
        this.done = done;
    }

    public Todo(Long id, String username, boolean done) {
        this.id = id;
        this.username = username;
        this.done = done;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", done=" + done +
                '}';
    }

    public boolean isDone() {
        return done;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
