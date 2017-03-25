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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lieroz on 25.03.17.
 */

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final String userLogin = "exampleUser";
    private final String pass = "examplePass";

    @Test
    public void user0GetDataOk() throws Exception {
        this.mockMvc.perform(
                get("/api/user/data")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void user1GetDataInvalidLogin() throws Exception {
        this.mockMvc.perform(
                get("/api/user/data")
                        .sessionAttr("userLogin", "THIS_IS_TEST_LOGIN_BRO"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void user2GetDataNullLogin() throws Exception {
        this.mockMvc.perform(
                get("/api/user/data"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void user3ChangeMailNull() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"strCont\":" + null + "}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void user4ChangeMailOk() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"strCont\":\"EXAMPLE@GMAIL.COM\"}")
                        .sessionAttr("userLogin", userLogin))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void user5ChangeMailInvalidSession() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changeMail")
                        .contentType("application/json")
                        .content("{\"strCont\":\"EXAMPLE@GMAIL.COM\"}")
                        .sessionAttr("userLogin", "INVALID_LOGIN"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void user6ChangePassInvalidSession() throws Exception {
        this.mockMvc.perform(
                post("/api/user/changePass")
                        .contentType("application/json")
                        .content("{\"oldPass\":\"" + pass + "\"," +
                        "\"newPass\":\"EXAMPLE_PASS\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void user7ChangePassNullOldPass() throws Exception {
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
    public void user8ChangePassNullNewPass() throws Exception {
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
    public void user9ChangePassInvalidOldPass() throws Exception {
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
    public void userrr1ChangePassOk() throws Exception {
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
