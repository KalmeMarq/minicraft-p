package me.kalmemarq.minicraft.util;

import java.util.Objects;

public class Identifier implements Comparable<Identifier> {
    private final String namespace;
    private final String path;

    public Identifier(String path) {
        this("minicraft", path);
    }

    public Identifier(String namespace, String path) {
        this.namespace = namespace;
        this.path = path;
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
