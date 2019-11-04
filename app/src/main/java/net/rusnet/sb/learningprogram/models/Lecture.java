package net.rusnet.sb.learningprogram.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import net.rusnet.sb.learningprogram.ListItemType;

public class Lecture implements ListItem, Parcelable {
    private final String number;
    private final String date;
    private final String theme;
    private final String lector;
    private final String[] subtopics;

    protected Lecture(Parcel in) {
        number = in.readString();
        date = in.readString();
        theme = in.readString();
        lector = in.readString();
        subtopics = in.createStringArray();
    }

    public static final Creator<Lecture> CREATOR = new Creator<Lecture>() {
        @Override
        public Lecture createFromParcel(Parcel in) {
            return new Lecture(in);
        }

        @Override
        public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };

    @Override
    public ListItemType getType() {
        return ListItemType.LECTURE;
    }

    public Lecture(@NonNull String number,
                   @NonNull String date,
                   @NonNull String theme,
                   @NonNull String lector,
                   @NonNull String[] subtopics) {
        this.number = number;
        this.date = date;
        this.theme = theme;
        this.lector = lector;
        this.subtopics = subtopics;
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

    public String[] getSubtopics() {
        return subtopics;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(date);
        dest.writeString(theme);
        dest.writeString(lector);
        dest.writeStringArray(subtopics);
    }
}
