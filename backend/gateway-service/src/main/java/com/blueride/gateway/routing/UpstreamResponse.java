package com.blueride.gateway.routing;

public record UpstreamResponse(int status, byte[] body, String contentType) {
}
