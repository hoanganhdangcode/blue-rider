package com.blueride.gateway.routing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ActionCatalogTest {

	private final ActionCatalog catalog = new ActionCatalog();

	@Test
	void loadsPingActions() {
		assertTrue(catalog.find("core.ping").isPresent());
		assertTrue(catalog.find("operator.ping").isPresent());
		assertEquals("GET", catalog.find("core.ping").orElseThrow().method());
		assertFalse(catalog.find("core.ping").orElseThrow().auth());
		assertTrue(catalog.find("auth.login").isEmpty());
	}
}
