package tests.ControllerTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import sample.Application;
import tests.Order;
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
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String userLogin = "exampleUser";
    private final String pass = "examplePass";

    @Test
    @Order(order = 1)
    public void userLoginOk() throws Exception {
        this.mockMvc.perform(
                post("/api/user/login")
                        .contentType("application/json")
                        .content("{\"userLogin\":\"" + userLogin + "\"," +
                                "\"pass\":\"" + pass + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 2)
    public void userLoginBadRequestNullLogin() throws Exception {
        this.mockMvc.perform(
                post("/api/user/login")
                        .contentType("application/json")
                        .content("{\"userLogin\":" + null + "," +
                                "\"pass\":\"" + pass + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 3)
    public void userLoginBadRequestLoginNotExist() throws Exception {
        this.mockMvc.perform(
                post("/api/user/login")
                        .contentType("application/json")
                        .content("{\"userLogin\":\"TEST_IT_BRO_LOGIN\"," +
                                "\"pass\":\"" + pass + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 4)
    public void userLoginBadRequestIncorrectPass() throws Exception {
        this.mockMvc.perform(
                post("/api/user/login")
                        .contentType("application/json")
                        .content("{\"userLogin\":\"" + userLogin + "\"," +
                                "\"pass\":\"TEST_IT_BRO_PASS\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }
}
