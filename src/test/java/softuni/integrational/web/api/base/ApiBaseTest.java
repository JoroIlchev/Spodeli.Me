package softuni.integrational.web.api.base;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import softuni.integrational.TestBase;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiBaseTest extends TestBase {

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    protected int port;

    protected String getFullUrl(String route) {
        return "http://localhost:" + port + route;
    }

    protected TestRestTemplate getRestTemplate() {
        return new TestRestTemplate();
    }

    protected TestRestTemplate getRestTemplate(String username, String password) {
        if (username != null && password != null) {
            return new TestRestTemplate(username, password);
        }

        return getRestTemplate();
    }
}
