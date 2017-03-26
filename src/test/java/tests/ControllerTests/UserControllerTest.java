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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lieroz on 25.03.17.
 */

@SpringBootTest(classes = Application.class)
@RunWith(OrderedRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String userLogin = "exampleUser";
    private final String pass = "examplePass";

    @Test
    @Order(order = 1)
    public void userGetDataOk() throws Exception {
        this.mockMvc.perform(
                get("/api/user/data")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 2)
    public void user1GetDataInvalidLogin() throws Exception {
        this.mockMvc.perform(
                get("/api/user/data")
                        .sessionAttr("userLogin", "THIS_IS_TEST_LOGIN_BRO"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 3)
    public void userGetDataNullLogin() throws Exception {
        this.mockMvc.perform(
                get("/api/user/data"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 4)
    public void userChangeMailNull() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"strCont\":" + null + "}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 5)
    public void userChangeMailOk() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"strCont\":\"EXAMPLE@GMAIL.COM\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 6)
    public void userChangeMailInvalidSession() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"strCont\":\"EXAMPLE@GMAIL.COM\"}")
                        .sessionAttr("userLogin", "INVALID_LOGIN"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 7)
    public void userChangePassInvalidSession() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":\"" + pass + "\"," +
                        "\"newPass\":\"EXAMPLE_PASS\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 8)
    public void userChangePassNullOldPass() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":" + null + "," +
                        "\"newPass\":\"EXAMPLE_PASS\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 9)
    public void userChangePassNullNewPass() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":\"" + pass + "\"," +
                        "\"newPass\":" + null + "}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 10)
    public void userChangePassInvalidOldPass() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":\"123456789\"," +
                        "\"newPass\":\"NEW_PASS_HERE\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @Order(order = 11)
    public void userChangePassOk() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":\"" + pass + "\"," +
                        "\"newPass\":\"NEW_PASS_HERE\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }
}
