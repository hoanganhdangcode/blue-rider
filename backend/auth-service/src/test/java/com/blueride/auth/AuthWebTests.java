package com.blueride.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.blueride.auth.config.InternalApiKeyFilter;
import com.blueride.auth.config.InternalApiProperties;
import com.blueride.auth.health.HealthController;
import com.blueride.auth.internal.PingController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {HealthController.class, PingController.class})
@Import(InternalApiKeyFilter.class)
@EnableConfigurationProperties(InternalApiProperties.class)
@TestPropertySource(properties = "app.internal-api-key=dev-internal-key")
class AuthWebTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void healthIsPublic() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void pingRequiresInternalKey() throws Exception {
        mockMvc.perform(get("/internal/v1/ping"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void pingSucceedsWithInternalKey() throws Exception {
        mockMvc.perform(get("/internal/v1/ping").header("X-Internal-Key", "dev-internal-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.service").value("auth"))
                .andExpect(jsonPath("$.status").value("ok"));
    }
}
