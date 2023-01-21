package me.kalmemarq.minicraft.util;

import java.util.Objects;
import java.util.regex.Pattern;

public class Identifier implements Comparable<Identifier> {
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("^[a-z]+$");
    private static final Pattern PATH_PATTERN = Pattern.compile("^[a-z0-9_/.]+$");

    private final String namespace;
    private final String path;

    public Identifier(String id) {
        String[] arr = new String[] { "minicraft", id };

        int sepIdx = id.indexOf(':');
        if (sepIdx >= 0) {
            arr[1] = id.substring(sepIdx + 1);

            if (sepIdx > 0) {
                arr[0] = id.substring(0, sepIdx);
            }
        }

        this.namespace = arr[0];
        this.path = arr[1];
        
        this.validate();
    }

    public Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;

        this.validate();
    }

    private void validate() {
        if (!NAMESPACE_PATTERN.matcher(this.namespace).matches()) {
            throw new RuntimeException("Identifier namespace does not match regex " + NAMESPACE_PATTERN.pattern() + "' - '" + this.namespace + "'");
        }

        if (!PATH_PATTERN.matcher(this.path).matches()) {
            throw new RuntimeException("Identifier path does not match regex " + PATH_PATTERN.pattern() + "' - '" + this.path + "'");
        }
    }

    public String getNamespace() {
      return namespace;
    }

    public String getPath() {
      return path;
    }

    public String toString() {
        return this.namespace + ":" + this.path;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Identifier other)) return false;

        return Objects.equals(other.namespace, this.namespace) && Objects.equals(other.path, this.path);
    }

    @Override
    public int hashCode() {
        return 31 * this.namespace.hashCode() + this.path.hashCode();
    }

    @Override
    public int compareTo(Identifier other) {
        int i = this.path.compareTo(other.path);

        if (i == 0) {
            i = this.namespace.compareTo(other.namespace);
        }

        return 0;
    }
}
