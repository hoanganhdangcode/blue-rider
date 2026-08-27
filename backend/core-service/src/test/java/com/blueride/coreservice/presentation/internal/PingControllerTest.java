package com.blueride.coreservice.presentation.internal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PingControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void pingRequiresInternalKey() throws Exception {
		mockMvc.perform(get("/internal/v1/ping"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void pingReturnsServiceName() throws Exception {
		mockMvc.perform(get("/internal/v1/ping").header("X-Internal-Key", "dev-internal-key"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.service").value("core-service"));
	}

	@Test
	void healthIsPublic() throws Exception {
		mockMvc.perform(get("/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("ok"));
	}
}
