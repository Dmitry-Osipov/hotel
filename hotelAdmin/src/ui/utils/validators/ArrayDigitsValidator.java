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
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }
        }

        return true;
    }
}
