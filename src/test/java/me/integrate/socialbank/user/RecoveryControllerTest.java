package me.integrate.socialbank.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class RecoveryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecoveryService recoveryService;

    @Test
    @WithMockUser
    void shouldReturnNoContent() throws Exception {
        String email = "ejemplo@integrate.me";

        mockMvc.perform(
                post("/recover")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(email))
                .andExpect(status().isNoContent());
    }


}
