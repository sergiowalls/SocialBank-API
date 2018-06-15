package me.integrate.socialbank.purchase;

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
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseService purchaseService;

    @Test
    @WithMockUser
    void shouldReturnCreatedStatus() throws Exception {
        given(purchaseService.purchaseHours(any(), any()))
                .willReturn(
                        new PurchaseResult(
                                PurchaseResult.TransactionResults.ACCEPTED,
                                10));

        Purchase purchase = new Purchase();
        purchase.setAmount(99.9);
        purchase.setPackageName("Basic Package");
        purchase.setNonce("f9SjkjeRa1VmS983Sl2");

        this.mockMvc.perform(
                post("/purchase")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(purchase)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void shouldReturnNotFound() throws Exception {
        given(purchaseService.purchaseHours(any(), any()))
                .willThrow(PackageNotFoundException.class);

        Purchase purchase = new Purchase();
        purchase.setAmount(99.9);
        purchase.setPackageName("UNEXISTING PACKAGE");
        purchase.setNonce("f9SjkjeRa1VmS983Sl2");

        this.mockMvc.perform(
                post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(purchase)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturnConflict() throws Exception {
        given(purchaseService.purchaseHours(any(), any()))
                .willThrow(TransactionAlreadyExistsException.class);

        Purchase purchase = new Purchase();
        purchase.setAmount(99.9);
        purchase.setPackageName("Basic Package");
        purchase.setNonce("repeated nonce");

        this.mockMvc.perform(
                post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(purchase)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnForbiddenStatus() throws Exception {
        Purchase purchase = new Purchase();
        purchase.setAmount(99.9);
        purchase.setPackageName("Basic Package");
        purchase.setNonce("repeated nonce");

        this.mockMvc.perform(
                post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(purchase)))
                .andExpect(status().isForbidden());
    }
}
