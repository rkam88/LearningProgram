package net.rusnet.sb.learningprogram.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.sb.learningprogram.ListItemType;
import net.rusnet.sb.learningprogram.R;
import net.rusnet.sb.learningprogram.fragments.LectureListFragment;
import net.rusnet.sb.learningprogram.models.Lecture;
import net.rusnet.sb.learningprogram.models.ListItem;
import net.rusnet.sb.learningprogram.models.Week;

import java.util.ArrayList;
import java.util.List;


public class LearningProgramAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int ITEM_VIEW_TYPE_LECTURE = 0;
    private final static int ITEM_VIEW_TYPE_WEEK = 1;

    private List<Lecture> mLectures;
    private List<ListItem> mListItems;

    private LectureListFragment.OnLectureSelectedListener mListener;

    public void setListener(LectureListFragment.OnLectureSelectedListener listener) {
        mListener = listener;
    }

    public void setLectures(Resources resources, List<Lecture> lectures, Boolean showWeeks) {
        mLectures = (lectures == null ? null : new ArrayList<>(lectures));

        mListItems = new ArrayList<>();
        mListItems.addAll(mLectures);

        int weekToInsert = 0;
        String weekText = resources.getString(R.string.week);
        if (showWeeks) {
            for (int i = mListItems.size() - 1; i >= 0; i--) {
                int number = Integer.parseInt(((Lecture) mListItems.get(i)).getNumber());
                int currentItemWeek = (number - 1) / 3 + 1;

                if (weekToInsert == 0) {
                    weekToInsert = currentItemWeek;
                }
                if (currentItemWeek != weekToInsert) {
                    mListItems.add(i + 1, new Week(weekText, weekToInsert));
                    weekToInsert = currentItemWeek;
                }
                if (i == 0) {
                    mListItems.add(0, new Week(weekText, weekToInsert));
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
        if (holder.getItemViewType() == ITEM_VIEW_TYPE_LECTURE) {
            LectureHolder lectureHolder = (LectureHolder) holder;

            Lecture lecture = (Lecture) mListItems.get(position);
            lectureHolder.mNumber.setText(lecture.getNumber());
            lectureHolder.mDate.setText(lecture.getDate());
            lectureHolder.mTheme.setText(lecture.getTheme());
            lectureHolder.mLector.setText(lecture.getLector());
        } else if (holder.getItemViewType() == ITEM_VIEW_TYPE_WEEK) {
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

    class LectureHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mListItems.get(position).getType().equals(ListItemType.LECTURE)) {
                Lecture lecture = (Lecture) mListItems.get(position);
                mListener.onLectureSelected(lecture);
            }
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
