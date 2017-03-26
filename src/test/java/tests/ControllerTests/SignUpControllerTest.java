package tests.ControllerTests;

import tests.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import sample.Application;
import tests.OrderedRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lieroz on 24.03.17.
 */

@SpringBootTest(classes = Application.class)
@RunWith(OrderedRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String userMail = "example@mail.ru";
    private final String userLogin = "exampleUser";
    private final String pass = "examplePass";

    @Test
    @Order(order = 1)
    public void userCreateCreated() throws Exception {
        this.mockMvc.perform(
                post("/api/user/signup")
                        .contentType("application/json")
                        .content("{\"userMail\":\"" + userMail + "\"," +
                                "\"userLogin\":\"" + userLogin + "\"," +
                                "\"pass\":\"" + pass + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 2)
    public void userCreateConflict() throws Exception {
        this.mockMvc.perform(
                post("/api/user/signup")
                        .contentType("application/json")
                        .content("{\"userMail\":\"" + userMail + "\"," +
                                "\"userLogin\":\"" + userLogin + "\"," +
                                "\"pass\":\"" + pass + "\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 3)
    public void userCreateBadRequestLogin() throws Exception {
        this.mockMvc.perform(
                post("/api/user/signup")
                        .contentType("application/json")
                        .content("{\"userMail\":\"" + userMail + "\"," +
                                "\"userLogin\":" + null + "," +
                                "\"pass\":\"" + pass + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 4)
    public void userCreateBadRequestInvalidContent() throws Exception {
        this.mockMvc.perform(
                post("/api/user/signup")
                        .contentType("text/html"))
                .andExpect(status().isUnsupportedMediaType());
    }
}