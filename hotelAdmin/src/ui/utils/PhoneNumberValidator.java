package ui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PhoneNumberValidator {
    public static final String RUS_PHONE_NUMBER_REGEX = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}\\d{2}$";

    private PhoneNumberValidator() {}

    public static boolean validatePhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(RUS_PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
