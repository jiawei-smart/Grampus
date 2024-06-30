package org.grampus.core.environment;

import org.grampus.core.GContext;

import java.util.List;

public interface GEnvironment {
    String getConfigYaml();
    List<String> getServicesNameStem();
    GContext createContext();
}
