package com.blueride.coreservice.presentation.internal;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1")
public class PingController {

	@GetMapping("/ping")
	public Map<String, String> ping() {
		return Map.of("service", "core-service");
	}
}
