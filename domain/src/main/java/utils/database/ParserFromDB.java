package utils.database;

import essence.room.RoomStatusTypes;
import essence.service.ServiceNames;
import essence.service.ServiceStatusTypes;
import utils.exceptions.TechnicalException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

// TODO: дока
public final class ParserFromDB {
    private ParserFromDB() {
    }

    public static String parseColumnNameFromSnakeCase(String columnName) {
        String[] parts = columnName.split("_");
        StringBuilder camelCase = new StringBuilder(parts[0]);

        for (int i = 1; i < parts.length; i++) {
            camelCase.append(Character.toUpperCase(parts[i].charAt(0)))
                    .append(parts[i].substring(1));
        }

        return camelCase.toString();
    }

    public static Object parseValueFromDB(String columnName, String table, ResultSet resultSet, int index) throws
            SQLException {
        Object columnValue;
        if (columnName.equals("status")) {
            columnValue = parseStatusValueFromDB(columnName, table, resultSet, index);
        } else if (table.equals("service") && columnName.equals("name")) {
            columnValue = ServiceNames.valueOf((String) resultSet.getObject(index));
        } else if (columnName.contains("Time")) {
            columnValue = parseTimeValueFromDB(resultSet, index);
        } else {
            columnValue = resultSet.getObject(index);
        }
        return columnValue;
    }

    private static Object parseTimeValueFromDB(ResultSet resultSet, int index) throws SQLException {
        Object columnValue;
        columnValue = resultSet.getObject(index);
        if (columnValue != null) {
            Timestamp timestamp = (Timestamp) columnValue;
            columnValue = timestamp.toLocalDateTime();
        }
        return columnValue;
    }

    private static Object parseStatusValueFromDB(String columnName, String table, ResultSet resultSet, int index) throws
            SQLException {
        Object columnValue;
        if (table.equals("room")) {
            columnValue = RoomStatusTypes.valueOf((String) resultSet.getObject(index));
        } else if (table.equals("service")) {
            columnValue = ServiceStatusTypes.valueOf((String) resultSet.getObject(index));
        } else {
            throw new TechnicalException(String.format("Таблица %s имеет колонку %s, но нет специального " +
                    "обработчика", table, columnName));
        }
        return columnValue;
    }
}
