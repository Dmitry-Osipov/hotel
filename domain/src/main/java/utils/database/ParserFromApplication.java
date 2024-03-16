package utils.database;

import java.lang.reflect.Field;

// TODO: дока
public final class ParserFromApplication {
    private ParserFromApplication() {
    }

    public static String parseColumnNameFromCamelCase(String columnName) {
        StringBuilder snakeCase = new StringBuilder();
        char[] chars = columnName.toCharArray();
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                snakeCase.append("_").append(Character.toLowerCase(c));
            } else {
                snakeCase.append(c);
            }
        }

        return snakeCase.toString();
    }

    public static <T> Object parseValueFromApplication(T essence, Field[] fields, int i) throws
            IllegalAccessException {
        Object value;
        if (fields[i].getType().isEnum()) {
            value = String.valueOf(fields[i].get(essence));
        } else {
            value = fields[i].get(essence);
        }
        return value;
    }
}
