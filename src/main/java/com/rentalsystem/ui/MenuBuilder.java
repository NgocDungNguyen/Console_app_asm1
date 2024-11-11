package com.rentalsystem.ui;

import java.util.ArrayList;
import java.util.List;

public class MenuBuilder {
    private List<String> options;

    public MenuBuilder() {
        this.options = new ArrayList<>();
    }

    public MenuBuilder clear() {
        options.clear();
        return this;
    }

    public MenuBuilder addOption(String option) {
        options.add(option);
        return this;
    }

    public void display(String title) {
        System.out.println("\n--- " + title + " ---");
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
        System.out.println();
    }
}