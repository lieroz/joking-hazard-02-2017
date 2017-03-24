package ControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import sample.Controllers.SignUpController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lieroz on 24.03.17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
public class SignUpControllerTest {

    @InjectMocks
    private SignUpController signUpController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.signUpController).build();
    }

    @Test
    public void testUserCreateSuccessfully() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(
                post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userMail\":\"example@mail.ru\"," +
                                "\"userLogin\":\"123456\"," +
                                "\"pass\":\"pass123456\"}"))
        .andExpect(status().isOk());
    }
}