package com.blueride.gateway.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import com.blueride.gateway.routing.ActionCatalog;
import com.blueride.gateway.routing.ActionDefinition;
import com.blueride.gateway.routing.UpstreamClient;
import com.blueride.gateway.routing.UpstreamResponse;
import com.blueride.gateway.security.AuthPrincipal;
import com.blueride.gateway.security.JwtAuthenticator;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ApiController {

	public static final String ACTION_HEADER = "X-API-ACTION";

	private final ActionCatalog catalog;
	private final UpstreamClient upstreamClient;
	private final JwtAuthenticator jwtAuthenticator;
	private final JsonParser jsonParser = JsonParserFactory.getJsonParser();

	public ApiController(ActionCatalog catalog, UpstreamClient upstreamClient, JwtAuthenticator jwtAuthenticator) {
		this.catalog = catalog;
		this.upstreamClient = upstreamClient;
		this.jwtAuthenticator = jwtAuthenticator;
	}

	@PostMapping("/api")
	public ResponseEntity<ApiEnvelope> handle(
			@RequestHeader(value = ACTION_HEADER, required = false) String action,
			@RequestBody(required = false) byte[] body,
			HttpServletRequest request) {
		if (action == null || action.isBlank()) {
			return ResponseEntity.badRequest().body(ApiEnvelope.fail("", "MISSING_ACTION", "Header X-API-ACTION is required"));
		}
		Optional<ActionDefinition> found = catalog.find(action);
		if (found.isEmpty()) {
			return ResponseEntity.badRequest().body(ApiEnvelope.fail(action, "UNKNOWN_ACTION", "Unknown API action"));
		}
		ActionDefinition definition = found.get();
		if (definition.auth()) {
			Optional<AuthPrincipal> principal = jwtAuthenticator.authenticate(request);
			if (principal.isEmpty()) {
				return ResponseEntity.status(401).body(ApiEnvelope.fail(action, "UNAUTHORIZED", "Authentication required"));
			}
		}
		try {
			UpstreamResponse upstream = upstreamClient.execute(definition, body);
			Object data = parseBody(upstream.body());
			if (upstream.status() >= 200 && upstream.status() < 300) {
				return ResponseEntity.ok(ApiEnvelope.ok(action, data));
			}
			int status = upstream.status() >= 400 ? upstream.status() : 502;
			return ResponseEntity.status(status)
					.body(ApiEnvelope.fail(action, "UPSTREAM_ERROR", "Upstream returned HTTP " + upstream.status()));
		} catch (ResourceAccessException ex) {
			return ResponseEntity.status(502)
					.body(ApiEnvelope.fail(action, "UPSTREAM_UNAVAILABLE", "Upstream service is unavailable"));
		} catch (RuntimeException ex) {
			return ResponseEntity.status(502)
					.body(ApiEnvelope.fail(action, "UPSTREAM_UNAVAILABLE", "Upstream service is unavailable"));
		}
	}

	private Object parseBody(byte[] body) {
		if (body == null || body.length == 0) {
			return Map.of();
		}
		String text = new String(body).trim();
		if (text.isEmpty()) {
			return Map.of();
		}
		try {
			if (text.startsWith("{")) {
				return jsonParser.parseMap(text);
			}
			if (text.startsWith("[")) {
				List<Object> list = jsonParser.parseList(text);
				return list;
			}
		} catch (RuntimeException ignored) {
			return text;
		}
		return text;
	}
}
