package com.blueride.gateway.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.blueride.gateway.routing.UpstreamClient;
import com.blueride.gateway.routing.UpstreamResponse;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UpstreamClient upstreamClient;

	@Test
	void missingActionReturns400() throws Exception {
		mockMvc.perform(post("/api").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error.code").value("MISSING_ACTION"));
	}

	@Test
	void unknownActionReturns400() throws Exception {
		mockMvc.perform(post("/api").header("X-API-ACTION", "trip.request"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.error.code").value("UNKNOWN_ACTION"));
	}

	@Test
	void corePingWrapsUpstreamJson() throws Exception {
		when(upstreamClient.execute(any(), any()))
				.thenReturn(new UpstreamResponse(200, "{\"service\":\"core-service\"}".getBytes(), "application/json"));
		mockMvc.perform(post("/api").header("X-API-ACTION", "core.ping"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.action").value("core.ping"))
				.andExpect(jsonPath("$.data.service").value("core-service"));
	}

	@Test
	void operatorPingWrapsUpstreamJson() throws Exception {
		when(upstreamClient.execute(any(), any()))
				.thenReturn(new UpstreamResponse(200, "{\"service\":\"operator-service\"}".getBytes(), "application/json"));
		mockMvc.perform(post("/api").header("X-API-ACTION", "operator.ping"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.action").value("operator.ping"))
				.andExpect(jsonPath("$.data.service").value("operator-service"));
	}
}
