package me.kalmemarq.optionparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

public class ArgOptionParser {
    private final List<ArgOption<?>> optionsList = new ArrayList<>();

    public <T> ArgOption<T> accepts(String name, Class<T> clazz) {
        ArgOption<T> arg = new ArgOption<T>(clazz, name);
        optionsList.add(arg);
        return arg;
    }

    public void parse(String[] args) {
        Map<String, List<String>> params = Maps.newHashMap();

        List<String> options = null;
        for (int i = 0; i < args.length; i++) {
            final String a = args[i];

            if (a.charAt(0) == '-' || (a.charAt(0) == '-' && a.charAt(1) == '-')) {
                boolean isD = a.charAt(1) == '-';
                if ((isD && a.length() < 3) || a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    return;
                }

                options = new ArrayList<>();
                
                if (a.contains("=")) {
                    String n = a.substring(isD ? 2 : 1, a.indexOf("="));
                    params.put(n, options);
                } else {
                    params.put(a.substring(isD ? 2 : 1), options);
                }
            }
            else if (options != null) {
                options.add(a);
            }
            else {
                System.err.println("Illegal parameter usage");
                return;
            }
        }

        for (ArgOption<?> op : optionsList) {
            List<String> l = params.get(op.getName());
            
            if (l == null) {
                l = params.get(op.getAlias());
            }

            if (l != null) {
                op.parseValues(l);
            }
        }
    }
}
