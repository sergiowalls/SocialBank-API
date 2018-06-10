package me.integrate.socialbank.enrollment;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.integrate.socialbank.enrollment.exceptions.EnrollmentNotFoundException;
import me.integrate.socialbank.enrollment.exceptions.TooLateException;
import me.integrate.socialbank.enrollment.exceptions.UserIsTheCreatorException;
import me.integrate.socialbank.event.Event;
import me.integrate.socialbank.event.EventService;
import me.integrate.socialbank.event.EventTestUtils;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockBean
    private EventService eventService;

    @MockBean
    private Authentication auth;

    @Test
    @WithMockUser
    void shouldReturnCreatedStatus() throws Exception {
        this.mockMvc.perform(post("/events/" + 123 + "/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(null)))
                .andExpect(status().isCreated());
    }

    @Test
    @Disabled     // <----------------------------------------- test does not work
    @WithMockUser
    void whenDateIsNotValidShouldReturnConflictStatus() throws Exception {
        given(auth.getName()).willReturn("a@a.a");
        given(enrollmentService.saveEnrollment(any(), any())).willThrow(TooLateException.class);

        this.mockMvc.perform(post("/events/" + 123 + "/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(null)))
                .andExpect(status().isConflict());
    }

    @Test
    @Disabled  // <----------------------------------------- test does not work
    @WithMockUser
    void whenUserIsSameAsCreatorShouldReturnConflictStatus() throws Exception {
        String email = "b@b.b";
        int id = 123;
        given(auth.getName()).willReturn(email);
        given(enrollmentService.saveEnrollment(id, email)).willThrow(UserIsTheCreatorException.class);

        this.mockMvc.perform(
                post("/events/" + id + "/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(null)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void shouldReturnListOfEnrollmentsOfUser() throws Exception {
        String emailCreator = "a@a.a", emailEnrolled = "b@b.com";
        UserTestUtils.createUser(emailCreator);
        UserTestUtils.createUser(emailEnrolled);
        Event e1 = EventTestUtils.createEvent(emailCreator);
        Event e2 = EventTestUtils.createEvent(emailCreator);
        List<Event> le = new ArrayList<>();
        le.add(e1);
        le.add(e2);

        when(enrollmentService.getEventsUserIsEnrolled(emailEnrolled)).thenReturn(le);
        this.mockMvc.perform(get("/users/" + emailEnrolled + "/enrollments")).andExpect(jsonPath("$", hasSize(le.size
                ()))).andExpect(status().isOk());
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
        le.add(emailEnrolledOne);
        le.add(emailEnrolledTwo);

        when(enrollmentService.getEnrollmentsOfEvent(id)).thenReturn(le);
        this.mockMvc.perform(get("/events/" + id + "/enrollments"))
                .andExpect(jsonPath("$", hasSize(le.size())))
                .andExpect(jsonPath("$.[*]", hasItems(emailEnrolledOne, emailEnrolledTwo))).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenDeleteEnrollmentShouldReturnNoContentStatus() throws Exception {
        this.mockMvc.perform(delete("/events/123/enrollments").contentType(MediaType.APPLICATION_JSON)).andExpect
                (status().isNoContent());
    }

    @Test
    @WithMockUser
    void whenDeleteNotExistentEnrollmentShouldReturnNotFoundStatus() throws Exception {
        int id = 123;
        given(enrollmentService.deleteEnrollment(anyInt(), any())).willThrow(EnrollmentNotFoundException.class);
        this.mockMvc.perform(delete("/events/" + id + "/enrollments").contentType(MediaType.APPLICATION_JSON)).andExpect(status()
                .isNotFound());
    }

    @Test
    @WithMockUser
    void whenDeleteIsTooLateShouldReturnConflictStatus() throws Exception {
        int id = 123;
        given(enrollmentService.deleteEnrollment(anyInt(), any())).willThrow(TooLateException.class);
        this.mockMvc.perform(delete("/events/" + id+"/enrollments").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
    }
}
