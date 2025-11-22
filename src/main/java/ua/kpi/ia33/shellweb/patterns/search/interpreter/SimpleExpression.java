package ua.kpi.ia33.shellweb.patterns.search.interpreter;

import ua.kpi.ia33.shellweb.domain.SearchResult;


public class SimpleExpression implements FilterExpression {

    private final String field;
    private final String value;

    public SimpleExpression(String field, String value) {
        this.field = field.toLowerCase();
        this.value = value;
    }

    @Override
    public boolean matches(SearchResult file) {
        return switch (field) {
            case "name" -> matchName(file);
            case "ext", "extension" -> matchExt(file);
            case "type" -> matchType(file);
            default -> false;
        };
    }

    private boolean matchName(SearchResult file) {
        String name = file.getName() == null ? "" : file.getName().toLowerCase();
        String pattern = value.toLowerCase();

        if (pattern.startsWith("*") && pattern.endsWith("*") && pattern.length() > 2) {
            String inner = pattern.substring(1, pattern.length() - 1);
            return name.contains(inner);
        } else if (pattern.startsWith("*")) {
            String suffix = pattern.substring(1);
            return name.endsWith(suffix);
        } else if (pattern.endsWith("*")) {
            String prefix = pattern.substring(0, pattern.length() - 1);
            return name.startsWith(prefix);
        } else {
            return name.equals(pattern);
        }
    }

    private boolean matchExt(SearchResult file) {
        String name = file.getName();
        if (name == null) {
            return false;
        }
        int dot = name.lastIndexOf('.');
        String ext = (dot >= 0 && dot < name.length() - 1)
                ? name.substring(dot + 1).toLowerCase()
                : "";
        return ext.equals(value.toLowerCase());
    }

    private boolean matchType(SearchResult file) {
        String type = file.getType();
        if (type == null) return false;
        return type.equalsIgnoreCase(value);
    }
}
