package me.kalmemarq.minicraft.main.optionparser;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

public class ArgOption<T> {
    protected boolean required;
    @Nullable
    protected String alias;
    @Nullable
    protected T defaultValue;

    protected List<T> values;

    protected String name;

    protected Class<T> type;

    ArgOption(Class<T> type, String name) {
        this.type = type;
        this.name = name;
        this.values = new ArrayList<>();
    }

    @Nullable
    public String getAlias() {
      return alias;
    }

    public String getName() {
      return name;
    }

    public Class<T> getType() {
      return type;
    }

    public List<T> getValues() {
      return values;
    }

    public @Nullable T getDefaultValue() {
      return defaultValue;
    }

    public ArgOption<T> defaultsTo(T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ArgOption<T> alias(String alias) {
        this.alias = alias;
        return this;
    }

    public ArgOption<T> required() {
        this.required = true;
        return this;
    }

    public boolean hasValue() {
        return this.values.size() > 0;
    }

    @SuppressWarnings("unchecked")
    public T getValue() {
        T v;

        try {
            v = this.getValue(0);
        } catch(Exception e) {
            v = this.defaultValue;
        }
        
        return v == null ? this.isBoolean() ? (T)(Object)false : v : v;
    }

    @SuppressWarnings("unchecked")
    public T getValue(int index) {
        T v = this.values.get(index);
        if (v == null) v = defaultValue;

        return v == null ? this.isBoolean() ? (T)(Object)false : v : v;
    }

    private Boolean isboolean = null;
        private boolean isBoolean() {
            if (this.isboolean == null) {
                try {
                    Object obj = type.getMethod("valueOf", String.class).invoke(null, "true");
                    
                    if (obj instanceof Boolean) {
                        this.isboolean = true;
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            return isboolean != null;
        }

    @SuppressWarnings("unchecked")
    public void parseValues(List<String> values) {
        if (values.isEmpty()) {
            try {
                Object obj = type.getMethod("valueOf", String.class).invoke(null, "true");
                if (obj instanceof Boolean) {
                    isboolean = true;
                    this.defaultValue = ((T)(Object)false);
                    this.values.add((T)(Object)true);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else {
            values.forEach(val -> {
                try {
                    if (this.type.isPrimitive()) {
                        Object obj = type.getMethod("valueOf", String.class).invoke(null, val);

                        if (obj != null) {
                            this.values.add((T) obj);
                        }
                    } else {
                        T obj = type.getConstructor(new Class[] { String.class }).newInstance(val);

                        this.values.add(obj);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
