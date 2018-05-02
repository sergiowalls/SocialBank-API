package me.integrate.socialbank.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.MvcResult;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@Transactional
class EventIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "pepito@pepito.com")
    void shouldReturnCreatedStatus() throws Exception {
        Map<String, String> user = new HashMap<>();
        user.put("email", "pepito@pepito.com");
        user.put("password", "sergiFeo");
        user.put("name", "Sergi");
        user.put("surname", "Ibanyez");
        user.put("birthdate", "1995-02-25");
        user.put("gender", "MALE");

        this.mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)));

        String json = "{\n" +
                "  \"creatorEmail\": \"pepito@pepito.com\",\n" +
                "  \"description\": \"string\",\n" +
                "  \"id\": 0,\n" +
                "  \"image\": \"string\",\n" +
                "  \"iniDate\": \"2019-04-25T15:12:44Z\",\n" +
                "  \"endDate\": \"2019-04-25T16:12:44Z\",\n" +
                "  \"location\": \"string\",\n" +
                "  \"title\": \"string\",\n" +
                "  \"demand\": \"true\"" +
                "}";

        this.mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult result = this.mockMvc.perform(
                get("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        List<HashMap<String, Object>> myObjects = objectMapper.readValue(content, new TypeReference<List<HashMap<String, Object>>>() {
        });

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'GMT''Z'");

        assertEquals(sdf1.parse("2019-04-25T15:12:44Z"), sdf2.parse(myObjects.get(0).get("iniDate").toString()));
    }
}