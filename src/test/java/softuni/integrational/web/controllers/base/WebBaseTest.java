package softuni.integrational.web.controllers.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import softuni.integrational.TestBase;
import softuni.project.Application;


@AutoConfigureMockMvc
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class WebBaseTest extends TestBase {
    @Autowired
    protected MockMvc mockMvc;
}
