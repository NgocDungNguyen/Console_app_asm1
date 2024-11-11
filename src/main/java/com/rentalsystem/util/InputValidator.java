package com.rentalsystem.util;

import java.util.Scanner;
import java.util.regex.Pattern;

public class InputValidator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    public static int getIntInput(Scanner scanner, String prompt, int min, int max) {
        int input;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextInt()) {
                System.out.println("That's not a number!");
                scanner.next();
                System.out.print(prompt);
            }
            input = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (input < min || input > max) {
                System.out.println("Please enter a number between " + min + " and " + max);
            }
        } while (input < min || input > max);
        return input;
    }

    public static String getStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static double getDoubleInput(Scanner scanner, String prompt) {
        double input;
        do {
            System.out.print(prompt);
            while (!scanner.hasNextDouble()) {
                System.out.println("That's not a valid number!");
                scanner.next();
                System.out.print(prompt);
            }
            input = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
        } while (input < 0);
        return input;
    }

    public static String getDateInput(Scanner scanner, String prompt) {
    String date;
    do {
        System.out.print(prompt);
        date = scanner.nextLine().trim();
        if (date.isEmpty() || DATE_PATTERN.matcher(date).matches()) {
            return date;
        }
        System.out.println("Invalid date format. Please use YYYY-MM-DD or leave empty.");
    } while (true);
}

public static String getEmailInput(Scanner scanner, String prompt) {
    String email;
    do {
        System.out.print(prompt);
        email = scanner.nextLine().trim();
        if (email.isEmpty() || EMAIL_PATTERN.matcher(email).matches()) {
            return email;
        }
        System.out.println("Invalid email format. Please try again or leave empty.");
    } while (true);
}

public static String getPhoneInput(Scanner scanner, String prompt) {
    String phone;
    do {
        System.out.print(prompt);
        phone = scanner.nextLine().trim();
        if (phone.isEmpty() || PHONE_PATTERN.matcher(phone).matches()) {
            return phone;
        }
        System.out.println("Invalid phone format. Please enter 10 digits or leave empty.");
    } while (true);
}
}