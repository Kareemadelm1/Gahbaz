

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class test {
    @Autowired
    private YourRepository repository; // Replace with your repository

    @Test
    public void testDatabaseConnection() {
        // Example: Fetch data or save a test entity
        assertNotNull(repository.findAll(), "Database connection failed");
    }
}
