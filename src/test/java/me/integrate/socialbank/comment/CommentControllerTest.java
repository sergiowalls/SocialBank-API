package me.integrate.socialbank.comment;

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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    void shouldReturnOkStatus() throws Exception {
        this.mockMvc.perform(get("/events/123/comments")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void shouldReturnNoContentStatus() throws Exception {
        this.mockMvc.perform(delete("/events/123/comments/456")).andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void shouldReturnCreatedStatus() throws Exception {
        String json = "{\n" + "  \"content\": \"abc\"\n" + "}";
        this.mockMvc.perform(post("/events/123/comments").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void whenGetAllCommentsReturnCorrectComment() throws Exception {
        int id = 123;
        Comment c1 = new Comment();
        Comment c2 = new Comment();
        c1.setId(3);
        c2.setId(4);
        List<Comment> cList = new ArrayList<>();
        cList.add(c1);
        cList.add(c2);
        when(commentService.getAllComments(id)).thenReturn(cList);
        this.mockMvc.perform(get("/events/" + id + "/comments")).andExpect(jsonPath("$", hasSize(cList.size())))
                .andExpect(jsonPath("$.[*].id", hasItems(3, 4))).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenGetCommentByIdThenReturnOkStatus() throws Exception {
        this.mockMvc.perform(get("/events/123/comments/456")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void whenUpdatedThenReturnAcceptedStatus() throws Exception {
        this.mockMvc.perform(put("/events/123/comments/456").param("content", "newContent")).andExpect(status()
                .isAccepted());
    }

}

