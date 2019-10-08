package net.rusnet.sb.learningprogram.models;

import net.rusnet.sb.learningprogram.ListItemType;

public class Week implements ListItem{

    public static final String WEEK = "Неделя ";
    private String mWeek;

    public Week(int mWeek) {
        this.mWeek = (WEEK + mWeek);
    }

    @Override
    public ListItemType getType() {
        return ListItemType.WEEK;
    }

    public String getWeek() {
        return mWeek;
    }
}
