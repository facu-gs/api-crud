package com.example.crud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.SpringApplication;
import static org.mockito.Mockito.*;

@SpringBootTest
class CrudApplicationTests {

	@Test
	void contextLoads() {
		//Do I need to use it?
	}

	@Test
	void testMain() {
		// Resources are closed after use. Useful for managing file streams, database connections, or in this case, a mock object.
		// mockStatic(SpringApplication.class): Mockito utility that allows you to mock static methods.
		// Here, it mocks the static SpringApplication.run() method,
		// so you can verify that it gets called without actually running the application.
		try (var mock = mockStatic(SpringApplication.class)) {
			// Invokes the main method of the CrudApplication class.
			// This simulates the application startup
			// as if you were running the application from the command line.
			//- An empty array of strings is passed as arguments (no command-line arguments are provided).
			CrudApplication.main(new String[]{});
			// mock.verify(...): The test checks if the static method SpringApplication.run()
			// was called as expected.

			//Lambda Expression: () -> SpringApplication.run(...)
			// is a lambda expression that captures the method call you want to verify.

			// Arguments:
			// CrudApplication.class: Ensures that SpringApplication.run()
			// is called with the CrudApplication class.
			// new String[]{}: Ensures that SpringApplication.run()
			// is called with an empty array of arguments.
			mock.verify(() -> SpringApplication.run(CrudApplication.class, new String[]{}));
		}
	}


}
