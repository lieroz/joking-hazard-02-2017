package ControllerTests;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import sample.Application;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lieroz on 24.03.17.
 */

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String userMail = "example@mail.ru";
    private final String userLogin = "exampleUser";
    private final String pass = "examplePass";

    @Test
    public void userCreateA() throws Exception {
        this.mockMvc.perform(
                post("/api/user/signup")
                        .contentType("application/json")
                        .content("{\"userMail\":\"" + userMail + "\"," +
                                "\"userLogin\":\"" + userLogin + "\"," +
                                "\"pass\":\"" + pass + "\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.result").value("true"))
                .andExpect(jsonPath("$.errorMsg").value("OK en"));
    }

    @Test
    public void userCreateB() throws Exception {
        this.mockMvc.perform(
                post("/api/user/signup")
                        .contentType("application/json")
                        .content("{\"userMail\":\"" + userMail + "\"," +
                                "\"userLogin\":\"" + userLogin + "\"," +
                                "\"pass\":\"" + pass + "\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.result").value("false"))
                .andExpect(jsonPath("$.errorMsg").value("Login is occupied en"));
    }
}