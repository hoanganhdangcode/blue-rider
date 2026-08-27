package com.blueride.gateway.routing;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.blueride.gateway.config.GatewayProperties;

@Component
public class RestUpstreamClient implements UpstreamClient {

	public static final String INTERNAL_KEY_HEADER = "X-Internal-Key";

	private final RestClient restClient;
	private final GatewayProperties properties;

	public RestUpstreamClient(RestClient restClient, GatewayProperties properties) {
		this.restClient = restClient;
		this.properties = properties;
	}

	@Override
	public UpstreamResponse execute(ActionDefinition action, byte[] body) {
		String url = properties.baseUrl(action.service()) + action.path();
		HttpMethod method = HttpMethod.valueOf(action.method());
		var spec = restClient.method(method)
				.uri(url)
				.header(INTERNAL_KEY_HEADER, properties.getInternalApiKey());
		if (shouldSendBody(method, body)) {
			spec = spec.contentType(MediaType.APPLICATION_JSON).body(body);
		}
		return spec.exchange((request, response) -> {
			byte[] responseBody = response.getBody().readAllBytes();
			MediaType contentType = response.getHeaders().getContentType();
			String type = contentType == null ? MediaType.APPLICATION_JSON_VALUE : contentType.toString();
			return new UpstreamResponse(response.getStatusCode().value(), responseBody, type);
		});
	}

	private static boolean shouldSendBody(HttpMethod method, byte[] body) {
		if (body == null || body.length == 0) {
			return false;
		}
		return method != HttpMethod.GET && method != HttpMethod.HEAD;
	}

	public static String bodyAsString(byte[] body) {
		if (body == null || body.length == 0) {
			return "";
		}
		return new String(body, StandardCharsets.UTF_8);
	}
}
