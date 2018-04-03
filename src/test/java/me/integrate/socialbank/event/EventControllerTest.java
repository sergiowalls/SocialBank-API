package me.integrate.socialbank.event;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    @WithMockUser
    void shouldReturnCreatedStatus() throws Exception {
        int id = 1;
        Event event = EventTestUtils.createEvent();
        given(eventService.saveEvent(any())).willReturn(event);
        this.mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(event)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void shouldReturnOkStatus() throws Exception {
        int id = 1;
        given(eventService.getEventById(id)).willReturn(EventTestUtils.createEvent());

        this.mockMvc.perform(
                get("/events/"+id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundStatus() throws Exception {
        given(eventService.getEventById(123))
                .willThrow(EventNotFoundException.class);

        this.mockMvc.perform(
                get("/events/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUnauthorizedStatus() throws Exception {
        this.mockMvc.perform(
                get("/events/1"))
                .andExpect(status().isForbidden());
    }
}