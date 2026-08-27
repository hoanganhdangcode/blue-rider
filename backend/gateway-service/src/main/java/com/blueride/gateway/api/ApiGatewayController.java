package com.blueride.gateway.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.blueride.gateway.catalog.ActionCatalog;
import com.blueride.gateway.catalog.ActionDefinition;
import com.blueride.gateway.forward.UpstreamClient;

@RestController
public class ApiGatewayController {

    private final ActionCatalog catalog;
    private final UpstreamClient upstreamClient;

    public ApiGatewayController(ActionCatalog catalog, UpstreamClient upstreamClient) {
        this.catalog = catalog;
        this.upstreamClient = upstreamClient;
    }

    @PostMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Envelope> handle(
            @RequestHeader(value = "X-API-ACTION", required = false) String action
    ) {
        if (action == null || action.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Envelope.fail("", "MISSING_ACTION", "Missing API action"));
        }
        ActionDefinition definition = catalog.find(action);
        if (definition == null) {
            return ResponseEntity.badRequest()
                    .body(Envelope.fail(action, "UNKNOWN_ACTION", "Unknown API action"));
        }
        Envelope envelope = upstreamClient.forward(action, definition);
        if (!envelope.success()) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(envelope);
        }
        return ResponseEntity.ok(envelope);
    }
}
