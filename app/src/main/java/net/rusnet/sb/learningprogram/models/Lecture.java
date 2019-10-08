package net.rusnet.sb.learningprogram.models;

import androidx.annotation.NonNull;

import net.rusnet.sb.learningprogram.ListItemType;

public class Lecture implements ListItem {
    private final String number;
    private final String date;
    private final String theme;
    private final String lector;

    @Override
    public ListItemType getType() {
        return ListItemType.LECTURE;
    }

    public Lecture(@NonNull String number,
                   @NonNull String date,
                   @NonNull String theme,
                   @NonNull String lector) {
        this.number = number;
        this.date = date;
        this.theme = theme;
        this.lector = lector;
    }


    public String getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public String getTheme() {
        return theme;
    }

    public String getLector() {
        return lector;
    }

}