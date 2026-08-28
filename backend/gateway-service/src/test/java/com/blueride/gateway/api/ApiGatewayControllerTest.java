package com.blueride.gateway.api;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiGatewayControllerTest {

    private static final MockWebServer AUTH_SERVER = new MockWebServer();
    private static final MockWebServer CORE_SERVER = new MockWebServer();

    static {
        try {
            AUTH_SERVER.start();
            CORE_SERVER.start();
        } catch (IOException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @AfterAll
    static void stopServers() throws IOException {
        AUTH_SERVER.shutdown();
        CORE_SERVER.shutdown();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("app.auth-base-url", () -> AUTH_SERVER.url("/").toString().replaceAll("/$", ""));
        registry.add("app.core-base-url", () -> CORE_SERVER.url("/").toString().replaceAll("/$", ""));
        registry.add("app.internal-api-key", () -> "dev-internal-key");
    }

    @Test
    void healthIsPublic() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void missingActionReturns400() throws Exception {
        mockMvc.perform(post("/api").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("MISSING_ACTION"));
    }

    @Test
    void unknownActionReturns400() throws Exception {
        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-ACTION", "unknown.action"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.action").value("unknown.action"))
                .andExpect(jsonPath("$.error.code").value("UNKNOWN_ACTION"));
    }

    @Test
    void authPingForwardsAndWraps() throws Exception {
        AUTH_SERVER.enqueue(new MockResponse()
                .setBody("{\"service\":\"auth\",\"status\":\"ok\"}")
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-ACTION", "auth.ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.action").value("auth.ping"))
                .andExpect(jsonPath("$.data.service").value("auth"))
                .andExpect(jsonPath("$.error").value(nullValue()));

        RecordedRequest recorded = AUTH_SERVER.takeRequest();
        assertEquals("/internal/v1/ping", recorded.getPath());
        assertEquals("dev-internal-key", recorded.getHeader("X-Internal-Key"));
        assertEquals("GET", recorded.getMethod());
    }

    @Test
    void corePingForwardsAndWraps() throws Exception {
        CORE_SERVER.enqueue(new MockResponse()
                .setBody("{\"service\":\"core\",\"status\":\"ok\"}")
                .addHeader("Content-Type", "application/json"));

        mockMvc.perform(post("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-API-ACTION", "core.ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.action").value("core.ping"))
                .andExpect(jsonPath("$.data.service").value("core"));
    }
}
