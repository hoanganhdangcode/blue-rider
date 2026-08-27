package com.blueride.gateway.routing;

import java.util.List;

public record ActionDefinition(
		String name,
		String service,
		String method,
		String path,
		boolean auth,
		List<String> roles) {
}
