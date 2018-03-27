package me.integrate.socialbank.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnCreatedStatus() throws Exception {
        Map<String, String> user = new HashMap<>();
        user.put("email", "pepito@pepito.com");
        user.put("password", "sergiFeo");

        given(userService.saveUser(any())).willReturn(UserTestUtils.createUser("pepito@pepito.com"));

        this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    @Disabled
    void shouldReturnOkStatus() throws Exception {
        Map<String, String> user = new HashMap<>();
        user.put("email", "pepito@pepito.com");
        user.put("password", "sergiFeo");

        given(userService.getUserByEmail("pepito@pepito.com")).willReturn(UserTestUtils.createUser("pepito@pepito.com"));

        this.mockMvc.perform(
                get("/users/pepito@pepito.com"))
                .andExpect(status().isOk());
    }
}