package me.integrate.socialbank.event;

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
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    void shouldReturnCreatedStatus() throws Exception {
        Map<String, String> event = new HashMap<>();
        event.put("id", "1");
        event.put("creatorEmail", "pepito@peptio.com");

        given(eventService.saveEvent(any())).willReturn(EventTestUtils.createEvent(1));

        this.mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(event)))
                .andExpect(status().isCreated());
    }

    @Test
    @Disabled
    void shouldReturnOkStatus() throws Exception {
        Map<String, String> event = new HashMap<>();
        event.put("id", "1");

        given(eventService.getEventById(1)).willReturn(EventTestUtils.createEvent(1));

        this.mockMvc.perform(
                get("/events/1"))
                .andExpect(status().isOk());
    }
}