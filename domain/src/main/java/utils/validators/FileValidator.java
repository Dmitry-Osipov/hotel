package utils.validators;

import utils.file.csv.CsvHeaderArrays;
import utils.file.csv.CsvHeaderLength;

import java.util.Arrays;

/**
 * Утилитарный класс для проверки свойств файлов.
 */
public final class FileValidator {
    private FileValidator() {
    }

    /**
     * Проверяет, является ли предоставленное имя файла допустимым.
     *
     * @param fileName Имя файла для проверки.
     * @return {@code true}, если имя файла допустимо, иначе {@code false}.
     */
    public static boolean isValidFileName(String fileName) {
        String regex = "^[a-zA-Z0-9_-]+$";
        return fileName.matches(regex);
    }

    /**
     * Проверяет заголовок CSV-файла на соответствие ожидаемому заголовку и длине.
     *
     * @param header Фактический заголовок, полученный из CSV-файла.
     * @param length Ожидаемая длина заголовка.
     * @param arrays Enum, представляющий возможные заголовки CSV для сравнения.
     * @return {@code true}, если заголовок допустим, иначе {@code false}.
     */
    public static boolean isValidCsvHeader(String[] header, CsvHeaderLength length, CsvHeaderArrays arrays) {
        boolean lengthIsCorrect = header.length == length.getHeaderLength();
        boolean allHeadersMatched = Arrays.stream(arrays.getHeaders())
                .allMatch(expectedHeader -> Arrays.asList(header).contains(expectedHeader));
        return lengthIsCorrect && allHeadersMatched;
    }

}
