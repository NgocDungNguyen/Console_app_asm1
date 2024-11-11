package com.rentalsystem.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsciiTableGenerator {
    private static final String HORIZONTAL_SEP = "-";
    private static final String VERTICAL_SEP = "|";
    private static final String JOIN_SEP = "+";

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
        sb.append(JOIN_SEP);
        for (int width : maxWidths) {
            sb.append(repeat(HORIZONTAL_SEP, width + 2)).append(JOIN_SEP);
        }
        sb.append("\n");

        // Generate header row
        sb.append(VERTICAL_SEP);
        for (int i = 0; i < headers.length; i++) {
            sb.append(" ").append(padRight(headers[i], maxWidths[i])).append(" ").append(VERTICAL_SEP);
        }
        sb.append("\n");

        // Generate middle border
        sb.append(JOIN_SEP);
        for (int width : maxWidths) {
            sb.append(repeat(HORIZONTAL_SEP, width + 2)).append(JOIN_SEP);
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
        sb.append(JOIN_SEP);
        for (int width : maxWidths) {
            sb.append(repeat(HORIZONTAL_SEP, width + 2)).append(JOIN_SEP);
        }
        sb.append("\n");

        return sb.toString();
    }

    private static String repeat(String s, int times) {
        return new String(new char[times]).replace("\0", s);
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
}