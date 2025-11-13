package cmdotender.TaskLine;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class AbstractPostgresIT {

	@Container
	static PostgreSQLContainer<?> postgres =
			new PostgreSQLContainer<>("postgres:15-alpine")
					.withDatabaseName("testdb")
					.withUsername("test")
					.withPassword("test");

	@DynamicPropertySource
	static void registerProps(DynamicPropertyRegistry r) {
		r.add("spring.datasource.url", postgres::getJdbcUrl);
		r.add("spring.datasource.username", postgres::getUsername);
		r.add("spring.datasource.password", postgres::getPassword);
		r.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

		r.add("spring.flyway.url", postgres::getJdbcUrl);
		r.add("spring.flyway.user", postgres::getUsername);
		r.add("spring.flyway.password", postgres::getPassword);

	}

	@BeforeAll
	static void started() {
	}
}
