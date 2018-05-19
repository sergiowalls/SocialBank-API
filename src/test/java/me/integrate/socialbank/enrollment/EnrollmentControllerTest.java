package me.integrate.socialbank.enrollment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EnrollmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @Test
    @WithMockUser
    void shouldReturnCreatedStatus() throws Exception {
        int id = 123;

        Enrollment enrollment = new Enrollment("a@a.a", id);
        given(enrollmentService.saveEnrollment(enrollment)).willReturn(enrollment);

        this.mockMvc.perform(
                post("/events/"+id+"/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(null)))
                .andExpect(status().isCreated());
    }
}
