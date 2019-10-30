package net.rusnet.sb.learningprogram.models;

import net.rusnet.sb.learningprogram.ListItemType;

public class Week implements ListItem {

    private String mWeek;

    public Week(String weekText, int weekNumber) {
        this.mWeek = String.format(weekText, weekNumber);
    }

    @Override
    public ListItemType getType() {
        return ListItemType.WEEK;
    }

    public String getWeek() {
        return mWeek;
    }
}
