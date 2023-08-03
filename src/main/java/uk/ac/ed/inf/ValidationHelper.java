package uk.ac.ed.inf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class containing static helper methods for validation.
 */
public class ValidationHelper {

    /**
     * Checks if a string is numeric.
     * @param strNum String to be checked.
     * @return True if string is numeric, false otherwise.
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Long.parseLong(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Luhn algorithm for validating credit card number. The implementation is taken from the source below.
     * https://www.geeksforgeeks.org/luhn-algorithm/
     * @param cardNo Card number.
     * @return True if valid, otherwise false.
     */
    public static boolean checkLuhn(String cardNo) {
        int nDigits = cardNo.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = cardNo.charAt(i) - '0';
            if (isSecond == true)
                d = d * 2;
            // We add two digits to handle cases that make two digits after doubling
            nSum += d / 10;
            nSum += d % 10;
            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    /**
     * Checks a string is valid for a regular expression.
     * @param regex Regular expression.
     * @param expression String to be checked against regular expression.
     * @return True if the string matched regular expression, false otherwise.
     */
    public static boolean checkRegex(String regex, String expression) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(expression);
        return matcher.matches();
    }

    /**
     * Ensures the date is a valid date in the Georgian calendar.
     * @param dateFormatString Format to check the string against.
     * @param dateString Date string to be checked.
     * @return Date if valid, null otherwise.
     */
    public static LocalDate validateDate(String dateFormatString, String dateString) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(dateFormatString).withResolverStyle(ResolverStyle.STRICT);
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, dateFormat);
        } catch (DateTimeParseException e) {
            System.err.println(e);
            return null;
        }
        return date;
    }
}
