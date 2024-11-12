package com.rentalsystem.util;

import java.util.List;

public class AsciiTableGenerator {
    private static final String HORIZONTAL_SEP = "─";
    private static final String VERTICAL_SEP = "│";
    private static final String JOIN_SEP = "┼";
    private static final String TOP_LEFT_CORNER = "┌";
    private static final String TOP_RIGHT_CORNER = "┐";
    private static final String BOTTOM_LEFT_CORNER = "└";
    private static final String BOTTOM_RIGHT_CORNER = "┘";
    private static final String TOP_JOIN = "┬";
    private static final String BOTTOM_JOIN = "┴";
    private static final String LEFT_JOIN = "├";
    private static final String RIGHT_JOIN = "┤";

    public static String generateTable(String[] headers, List<String[]> data) {
        StringBuilder sb = new StringBuilder();

        // Calculate max width of each column
        int[] maxWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            maxWidths[i] = headers[i].length();
        }
        for (String[] row : data) {
            for (int i = 0; i < row.length; i++) {
                maxWidths[i] = Math.max(maxWidths[i], row[i].length());
            }
        }

        // Generate top border
        sb.append(TOP_LEFT_CORNER);
        for (int i = 0; i < maxWidths.length; i++) {
            sb.append(repeat(HORIZONTAL_SEP, maxWidths[i] + 2));
            sb.append(i == maxWidths.length - 1 ? TOP_RIGHT_CORNER : TOP_JOIN);
        }
        sb.append("\n");

        // Generate header row
        sb.append(VERTICAL_SEP);
        for (int i = 0; i < headers.length; i++) {
            sb.append(" ").append(padRight(headers[i], maxWidths[i])).append(" ").append(VERTICAL_SEP);
        }
        sb.append("\n");

        // Generate middle border
        sb.append(LEFT_JOIN);
        for (int i = 0; i < maxWidths.length; i++) {
            sb.append(repeat(HORIZONTAL_SEP, maxWidths[i] + 2));
            sb.append(i == maxWidths.length - 1 ? RIGHT_JOIN : JOIN_SEP);
        }
        sb.append("\n");

        // Generate data rows
        for (String[] row : data) {
            sb.append(VERTICAL_SEP);
            for (int i = 0; i < row.length; i++) {
                sb.append(" ").append(padRight(row[i], maxWidths[i])).append(" ").append(VERTICAL_SEP);
            }
            sb.append("\n");
        }

        // Generate bottom border
        sb.append(BOTTOM_LEFT_CORNER);
        for (int i = 0; i < maxWidths.length; i++) {
            sb.append(repeat(HORIZONTAL_SEP, maxWidths[i] + 2));
            sb.append(i == maxWidths.length - 1 ? BOTTOM_RIGHT_CORNER : BOTTOM_JOIN);
        }
        sb.append("\n");

        return sb.toString();
    }

    public static String generateMenuTable(String title, String[] options) {
        StringBuilder sb = new StringBuilder();
        int maxWidth = title.length();
        for (String option : options) {
            maxWidth = Math.max(maxWidth, option.length());
        }

        // Title
        sb.append(TOP_LEFT_CORNER).append(repeat(HORIZONTAL_SEP, maxWidth + 4)).append(TOP_RIGHT_CORNER).append("\n");
        sb.append(VERTICAL_SEP).append(" ").append(padCenter(title, maxWidth + 2)).append(" ").append(VERTICAL_SEP).append("\n");
        sb.append(LEFT_JOIN).append(repeat(HORIZONTAL_SEP, maxWidth + 4)).append(RIGHT_JOIN).append("\n");

        // Options
        for (int i = 0; i < options.length; i++) {
            sb.append(VERTICAL_SEP).append(" ").append(padRight(options[i], maxWidth + 2)).append(" ").append(VERTICAL_SEP).append("\n");
        }

        // Bottom border
        sb.append(BOTTOM_LEFT_CORNER).append(repeat(HORIZONTAL_SEP, maxWidth + 4)).append(BOTTOM_RIGHT_CORNER).append("\n");

        return sb.toString();
    }

    public static String generateInputPromptTable(String title, String[] prompts) {
        StringBuilder sb = new StringBuilder();
        int maxWidth = title.length();
        for (String prompt : prompts) {
            maxWidth = Math.max(maxWidth, prompt.length());
        }

        // Title
        sb.append(TOP_LEFT_CORNER).append(repeat(HORIZONTAL_SEP, maxWidth + 4)).append(TOP_RIGHT_CORNER).append("\n");
        sb.append(VERTICAL_SEP).append(" ").append(padCenter(title, maxWidth + 2)).append(" ").append(VERTICAL_SEP).append("\n");
        sb.append(LEFT_JOIN).append(repeat(HORIZONTAL_SEP, maxWidth + 4)).append(RIGHT_JOIN).append("\n");

        // Prompts
        for (int i = 0; i < prompts.length; i++) {
            sb.append(VERTICAL_SEP).append(" ").append(padRight(prompts[i], maxWidth + 2)).append(" ").append(VERTICAL_SEP).append("\n");
            if (i < prompts.length - 1) {
                sb.append(LEFT_JOIN).append(repeat(HORIZONTAL_SEP, maxWidth + 4)).append(RIGHT_JOIN).append("\n");
            }
        }

        // Bottom border
        sb.append(BOTTOM_LEFT_CORNER).append(repeat(HORIZONTAL_SEP, maxWidth + 4)).append(BOTTOM_RIGHT_CORNER).append("\n");

        return sb.toString();
    }

    public static String generateConfirmationTable(String title, String message) {
        StringBuilder sb = new StringBuilder();
        int maxWidth = Math.max(title.length(), message.length());

        // Title
        sb.append(TOP_LEFT_CORNER).append(repeat(HORIZONTAL_SEP, maxWidth + 4)).append(TOP_RIGHT_CORNER).append("\n");
        sb.append(VERTICAL_SEP).append(" ").append(padCenter(title, maxWidth + 2)).append(" ").append(VERTICAL_SEP).append("\n");
        sb.append(LEFT_JOIN).append(repeat(HORIZONTAL_SEP, maxWidth + 4)).append(RIGHT_JOIN).append("\n");

        // Message
        sb.append(VERTICAL_SEP).append(" ").append(padRight(message, maxWidth + 2)).append(" ").append(VERTICAL_SEP).append("\n");

        // Bottom border
        sb.append(BOTTOM_LEFT_CORNER).append(repeat(HORIZONTAL_SEP, maxWidth + 4)).append(BOTTOM_RIGHT_CORNER).append("\n");

        return sb.toString();
    }

    private static String repeat(String s, int times) {
        return new String(new char[times]).replace("\0", s);
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private static String padCenter(String s, int width) {
        return String.format("%-" + width + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }
}