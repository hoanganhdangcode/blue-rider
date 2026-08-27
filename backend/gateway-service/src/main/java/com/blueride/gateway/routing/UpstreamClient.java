package com.blueride.gateway.routing;

public interface UpstreamClient {

	UpstreamResponse execute(ActionDefinition action, byte[] body);
}
