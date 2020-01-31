package softuni.integrational;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import softuni.project.Application;

@SpringBootTest(classes = Application.class)
@ExtendWith(MockitoExtension.class)
public class TestBase {
    @BeforeEach
    private void setupTest() {
        MockitoAnnotations.initMocks(this);
        this.beforeEach();
    }

    protected void beforeEach() {
    }
}