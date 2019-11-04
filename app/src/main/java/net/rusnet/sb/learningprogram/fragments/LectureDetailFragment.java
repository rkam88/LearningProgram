package net.rusnet.sb.learningprogram.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.sb.learningprogram.R;
import net.rusnet.sb.learningprogram.adapters.LectureSubtopicsAdapter;
import net.rusnet.sb.learningprogram.models.Lecture;

public class LectureDetailFragment extends Fragment {

    private static final String ARG_LECTURE = "lecture";

    private Lecture mLecture = null;

    private TextView mNumber;
    private TextView mDate;
    private TextView mTheme;
    private TextView mLector;
    private String[] mTopics;

    public static LectureDetailFragment newInstance(Lecture lecture) {
        Bundle args = new Bundle();
        LectureDetailFragment fragment = new LectureDetailFragment();
        args.putParcelable(ARG_LECTURE, lecture);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLecture = getArguments().getParcelable(ARG_LECTURE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lecture_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

    }

    private void initViews(@NonNull View view) {
        mNumber = view.findViewById(R.id.number);
        mDate = view.findViewById(R.id.date);
        mTheme = view.findViewById(R.id.theme);
        mLector = view.findViewById(R.id.lector);

        if (mLecture != null) {
            mNumber.setText(mLecture.getNumber());
            mDate.setText(mLecture.getDate());
            mTheme.setText(mLecture.getTheme());
            mLector.setText(mLecture.getLector());
            mTopics = mLecture.getSubtopics();

            RecyclerView lectureTopicsRecycler = (RecyclerView) view.findViewById(R.id.lecture_subtopic_recycler);
            LectureSubtopicsAdapter adapter = new LectureSubtopicsAdapter(mTopics);
            lectureTopicsRecycler.setAdapter(adapter);
        }
    }
}
