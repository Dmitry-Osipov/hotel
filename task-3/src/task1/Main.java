package task1;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int randomNumber = (new Random()).nextInt(900) + 100;
        String digits = String.format("%d", randomNumber);
        byte counter = 0;
        for (int i = 0; i < digits.length(); i++) counter += Byte.parseByte(String.valueOf(digits.charAt(i)));
        System.out.println(randomNumber);
        System.out.println(counter);
    }
}
