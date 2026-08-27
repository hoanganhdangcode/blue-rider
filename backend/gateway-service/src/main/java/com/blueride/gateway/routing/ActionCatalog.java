package com.blueride.gateway.routing;

import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

@Component
public class ActionCatalog {

	private final Map<String, ActionDefinition> actions;

	public ActionCatalog() {
		this.actions = load();
	}

	public Optional<ActionDefinition> find(String action) {
		if (action == null || action.isBlank()) {
			return Optional.empty();
		}
		return Optional.ofNullable(actions.get(action.trim()));
	}

	private static Map<String, ActionDefinition> load() {
		try (InputStream in = new ClassPathResource("actions.yml").getInputStream()) {
			Yaml yaml = new Yaml();
			Map<String, Object> root = yaml.load(in);
			Object rawActions = root == null ? null : root.get("actions");
			if (!(rawActions instanceof Map<?, ?> actionMap)) {
				return Map.of();
			}
			Map<String, ActionDefinition> result = new LinkedHashMap<>();
			actionMap.forEach((key, value) -> {
				if (key == null || !(value instanceof Map<?, ?> spec)) {
					return;
				}
				String name = String.valueOf(key);
				String service = String.valueOf(spec.get("service"));
				Object methodRaw = spec.get("method");
				String method = String.valueOf(methodRaw == null ? "POST" : methodRaw).toUpperCase(Locale.ROOT);
				String path = String.valueOf(spec.get("path"));
				Object authRaw = spec.get("auth");
				boolean auth = Boolean.parseBoolean(String.valueOf(authRaw == null ? Boolean.FALSE : authRaw));
				List<String> roles = List.of();
				Object rolesRaw = spec.get("roles");
				if (rolesRaw instanceof List<?> roleList) {
					roles = roleList.stream().map(String::valueOf).toList();
				}
				result.put(name, new ActionDefinition(name, service, method, path, auth, roles));
			});
			return Collections.unmodifiableMap(result);
		} catch (Exception ex) {
			throw new IllegalStateException("Unable to load actions.yml", ex);
		}
	}
}
