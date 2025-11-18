package vn.spring.crud;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
class TodoApplicationTests {

    @Value("${spring.application.name}")
    private String applicationName;

	@Test
	void contextLoads() {
        assertEquals("todo-test", applicationName);
	}

}
