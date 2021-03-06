package me.integrate.socialbank.event;

import me.integrate.socialbank.user.User;
import me.integrate.socialbank.user.UserService;
import me.integrate.socialbank.user.UserTestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class EventIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Test
    @WithMockUser
    void whenUpdateEventShouldReturnUpdatedEvent() throws Exception {
        User user = UserTestUtils.createUser("aaa@aaa.aaa");
        user.setBalance(10000f);
        userService.saveUser(user);
        Event event = EventTestUtils.createEvent("aaa@aaa.aaa");
        eventService.saveEvent(event);

        String json = "{" +
                "\"image\": \"image\"," +
                "\"description\": \"Description\"" +
                "}";
        this.mockMvc.perform(
                put("/events/" + event.getId()).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.description", is("Description")))
                .andExpect(jsonPath("$.image", is("image")));
    }

    @Test
    @WithMockUser("aaa@aaa.aaa")
    void whenInsertEventShouldReturnCreatedEvent() throws Exception {
        User user = UserTestUtils.createUser("aaa@aaa.aaa");
        userService.saveUser(user);
        String json = "{\n" +
                "  \"creatorEmail\": \"aaa@aaa.aaa\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"endDate\": \"2019-04-25T15:12:44.867Z\",\n" +
                "  \"id\": 0,\n" +
                "  \"image\": \"string\",\n" +
                "  \"iniDate\": \"2019-04-25T15:12:44.865Z\",\n" +
                "  \"location\": \"string\",\n" +
                "  \"title\": \"string\",\n" +
                "  \"demand\": \"false\",\n" +
                "  \"category\": \"CULTURE\",\n" +
                "  \"capacity\": \"10\",\n" +
                "  \"tags\": []" +
                "}";
        this.mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

}