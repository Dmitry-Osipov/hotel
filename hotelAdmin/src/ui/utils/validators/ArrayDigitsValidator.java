package ui.utils.validators;

/**
 * Класс является финальным и предоставляет статический метод для проверки, являются ли все элементы массива строк
 * числами.
 */
public final class ArrayDigitsValidator {
    private ArrayDigitsValidator() {
    }

    /**
     * Метод проверяет, являются ли все элемента списка числами.
     * @param array Массив строк.
     * @return true, если все элементы массива являются числами, иначе false.
     */
    public static boolean isArrayOfDigits(String[] array) {
        for (String str : array) {
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
}
