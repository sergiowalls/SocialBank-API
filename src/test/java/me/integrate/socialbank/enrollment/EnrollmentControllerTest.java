package me.integrate.socialbank.enrollment;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.integrate.socialbank.event.Event;
import me.integrate.socialbank.event.EventTestUtils;
import me.integrate.socialbank.user.UserTestUtils;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Test
    @WithMockUser
    void shouldReturnListOfEnrollmentsOfUser() throws Exception {
        String emailCreator = "a@a.a", emailEnrolled = "b@b.com";
        UserTestUtils.createUser(emailCreator);
        UserTestUtils.createUser(emailEnrolled);
        Event e1 = EventTestUtils.createEvent(emailCreator);
        Event e2 = EventTestUtils.createEvent(emailCreator);
        List<Integer> le = new ArrayList<>();
        le.add(e1.getId()); le.add(e2.getId());

        when(enrollmentService.getEnrollmentsOfUser(emailEnrolled)).thenReturn(le);
        this.mockMvc.perform(get("/users/"+emailEnrolled+"/enrollments"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(le.size())))
                .andExpect(jsonPath("$.[*]", hasItems(e1.getId(), e2.getId())))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser
    void shouldReturnListOfEnrollmentsOfEvent() throws Exception {
        String emailCreator = "a@a.a", emailEnrolledOne = "b@b.com", emailEnrolledTwo = "c@c.c";
        UserTestUtils.createUser(emailCreator);
        UserTestUtils.createUser(emailEnrolledOne);
        UserTestUtils.createUser(emailEnrolledTwo);
        int id = EventTestUtils.createEvent(emailCreator).getId();
        List<String> le = new ArrayList<>();
        le.add(emailEnrolledOne); le.add(emailEnrolledTwo);

        when(enrollmentService.getEnrollmentsOfEvent(id)).thenReturn(le);
        this.mockMvc.perform(get("/events/"+id+"/enrollments"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(le.size())))
                .andExpect(jsonPath("$.[*]", hasItems(emailEnrolledOne, emailEnrolledTwo)))
                .andExpect(status().isOk())
                .andReturn();
    }
}
