package com.blueride.auth.internal;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1")
public class PingController {

    @GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> ping() {
        return Map.of("service", "auth", "status", "ok");
    }
}
