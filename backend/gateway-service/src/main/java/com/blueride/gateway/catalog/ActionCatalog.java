package com.blueride.gateway.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ActionCatalog {

    private final Map<String, ActionDefinition> actions;

    public ActionCatalog() {
        this.actions = load();
    }

    public ActionDefinition find(String action) {
        if (action == null) {
            return null;
        }
        return actions.get(action);
    }

    private static Map<String, ActionDefinition> load() {
        ClassPathResource resource = new ClassPathResource("actions.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try (InputStream input = resource.getInputStream()) {
            ActionsFile file = mapper.readValue(input, ActionsFile.class);
            if (file.actions() == null) {
                return Collections.emptyMap();
            }
            return Map.copyOf(file.actions());
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to load actions.yml", ex);
        }
    }

    private record ActionsFile(Map<String, ActionDefinition> actions) {
    }
}
