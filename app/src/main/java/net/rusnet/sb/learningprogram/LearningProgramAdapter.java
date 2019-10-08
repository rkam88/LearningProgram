package net.rusnet.sb.learningprogram;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.sb.learningprogram.models.Lecture;
import net.rusnet.sb.learningprogram.models.ListItem;
import net.rusnet.sb.learningprogram.models.Week;

import java.util.ArrayList;
import java.util.List;


public class LearningProgramAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Lecture> mLectures;
    private List<ListItem> mListItems;

    public void setLectures(List<Lecture> lectures, Boolean showWeeks) {
        mLectures = (lectures == null ? null : new ArrayList<>(lectures));

        mListItems = new ArrayList<>();
        for(Lecture l: mLectures) {
            mListItems.add(l);
        }

        int weekToInsert = 0;
        if (showWeeks) {
            for (int i = mListItems.size() - 1; i >= 0; i--) {
                int number = Integer.parseInt(((Lecture) mListItems.get(i)).getNumber());
                int currentItemWeek = (number -1)/3 + 1;

                if (weekToInsert == 0) {
                    weekToInsert = currentItemWeek;
                }
                if (currentItemWeek != weekToInsert) {
                    mListItems.add(i+1,new Week(weekToInsert));
                    weekToInsert = currentItemWeek;
                }
                if (i == 0) {
                    mListItems.add(0, new Week(weekToInsert));
                }
            }
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View weekView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week, parent, false);
            return new WeekHolder(weekView);
        } else {
            View lectureView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture, parent, false);
            return new LectureHolder(lectureView);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 0) {
            LectureHolder lectureHolder = (LectureHolder) holder;

            Lecture lecture = (Lecture) mListItems.get(position);
            lectureHolder.mNumber.setText(lecture.getNumber());
            lectureHolder.mDate.setText(lecture.getDate());
            lectureHolder.mTheme.setText(lecture.getTheme());
            lectureHolder.mLector.setText(lecture.getLector());
        } else {
            WeekHolder weekHolder = (WeekHolder) holder;
            Week week = (Week) mListItems.get(position);
            weekHolder.mWeek.setText(week.getWeek());
        }

    }

    @Override
    public int getItemCount() {
        return (mListItems == null ? 0 : mListItems.size());
    }

    @Override
    public int getItemViewType(int position) {
        if (mListItems.get(position).getType().equals(ListItemType.LECTURE)) {
            return 0;
        } else {
            return 1;
        }

    }

    static class LectureHolder extends RecyclerView.ViewHolder {
        private final TextView mNumber;
        private final TextView mDate;
        private final TextView mTheme;
        private final TextView mLector;

        public LectureHolder(@NonNull View itemView) {
            super(itemView);
            mNumber = itemView.findViewById(R.id.number);
            mDate = itemView.findViewById(R.id.date);
            mTheme = itemView.findViewById(R.id.theme);
            mLector = itemView.findViewById(R.id.lector);
        }
    }

    static class WeekHolder extends RecyclerView.ViewHolder {
        private final TextView mWeek;

        public WeekHolder(@NonNull View itemView) {
            super(itemView);
            mWeek = itemView.findViewById(R.id.week);
        }
    }
}
