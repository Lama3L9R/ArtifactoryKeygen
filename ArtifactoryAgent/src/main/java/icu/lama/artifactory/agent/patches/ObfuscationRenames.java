package icu.lama.artifactory.agent.patches;

import java.util.HashMap;
import java.util.Map;

public class ObfuscationRenames {
    private static ObfuscationRenames instance = new ObfuscationRenames();
    private final Map<String, String> names = new HashMap<>();

    private ObfuscationRenames() {
        add("org.jfrog.license.api.a", "org.jfrog.license.api.LicenseParser");
    }

    private void add(String original, String deobfName) {
        names.put(original, deobfName);
    }

    public String rename(String obfName) {
        if (names.containsKey(obfName)) {
            return names.get(obfName);
        }
        return obfName;
    }

    public static ObfuscationRenames getInstance() {
        return instance;
    }
}
