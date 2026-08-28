package com.blueride.gateway.catalog;

public record ActionDefinition(String method, String upstream, String path) {
}
