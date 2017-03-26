package tests.APITests;

import com.github.javafaker.Faker;
import org.junit.BeforeClass;
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

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lieroz on 24.03.17.
 */

@SpringBootTest(classes = Application.class)
@RunWith(OrderedRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class ChangeMailIT {

    @Autowired
    private MockMvc mockMvc;
    private static Faker faker;

    private static String userMail;
    private static String userLogin;
    private static String pass;

    @BeforeClass
    public static void setUp() {
        faker = new Faker(new Locale("en-US"));
        userMail = faker.internet().emailAddress();
        userLogin = faker.name().username();
        pass = faker.internet().password();
    }

    @Test
    @Order(order = 1)
    public void createUser() throws Exception {
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
    public void changeMailOk() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"strCont\":\"" + faker.internet().emailAddress() + "\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 3)
    public void changeMailNull() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"strCont\":" + null + "}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 4)
    public void changeMailInvalidSession() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"strCont\":\"" + faker.internet().emailAddress() + "\"}")
                        .sessionAttr("userLogin", faker.name().username()))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 5)
    public void invalidContent() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("text/html"))
                .andExpect(status().isUnsupportedMediaType());
    }
}