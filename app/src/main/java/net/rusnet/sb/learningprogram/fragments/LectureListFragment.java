package net.rusnet.sb.learningprogram.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.sb.learningprogram.R;
import net.rusnet.sb.learningprogram.adapters.LearningProgramAdapter;
import net.rusnet.sb.learningprogram.adapters.LectorSpinnerAdapter;
import net.rusnet.sb.learningprogram.dataprovider.LearningProgramProvider;
import net.rusnet.sb.learningprogram.models.Lecture;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LectureListFragment extends Fragment {

    private static final int POSITION_ALL = 0;

    private RecyclerView mRecyclerView;
    private Spinner mWeekSpinner;
    private Spinner mSpinner;
    private LinearLayoutManager mLinearLayoutManager;
    private LearningProgramProvider mLearningProgramProvider = new LearningProgramProvider();
    private LearningProgramAdapter mAdapter;

    private boolean mShowWeeks;
    private boolean mFilterByLector;
    private String mLectorName;

    private Resources mResources;

    private OnLectureSelectedListener mListener;

    public interface OnLectureSelectedListener {
        public void onLectureSelected(Lecture lecture);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLectureSelectedListener) {
            mListener = (OnLectureSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement LectureListFragment.OnLectureSelectedListener");
        }
    }

    public static Fragment newInstance() {
        return new LectureListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lecture_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWeekSpinner = view.findViewById(R.id.week_selection_spinner);
        mSpinner = view.findViewById(R.id.lectors_spinner);
        mRecyclerView = view.findViewById(R.id.learning_program_recycler);

        initRecyclerView();
        mResources = getResources();
        new LoadLecturesFromWebTask(this).execute();
    }

    private void initRecyclerView() {

        mLinearLayoutManager = new LinearLayoutManager(
                mRecyclerView.getContext(),
                RecyclerView.VERTICAL,
                false
        );
        mRecyclerView.setLayoutManager(mLinearLayoutManager); //можно задать и в .xml
        mAdapter = new LearningProgramAdapter();
        mAdapter.setListener(mListener);

        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(divider);
    }

    private void updateAdapter() {
        mAdapter.setLectures(mResources, mLearningProgramProvider.provideLectures(), mShowWeeks);
    }

    private void initSpinner() {

        List<String> lectors = mLearningProgramProvider.provideLectors();
        Collections.sort(lectors);
        lectors.add(POSITION_ALL, getResources().getString(R.string.all));


        final LectorSpinnerAdapter spinnerAdapter = new LectorSpinnerAdapter(lectors);
        mSpinner.setAdapter(spinnerAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mLectorName = "";
                List<Lecture> filteredLectures;
                if (position == POSITION_ALL) {
                    filteredLectures = mLearningProgramProvider.provideLectures();
                    mFilterByLector = false;
                } else {
                    mLectorName = spinnerAdapter.getItem(position);
                    filteredLectures = mLearningProgramProvider.filterBy(mLectorName);
                    mFilterByLector = true;
                }
                mAdapter.setLectures(mResources, filteredLectures, mShowWeeks);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int spinnerPosition = mSpinner.getSelectedItemPosition();
        mFilterByLector = spinnerPosition != POSITION_ALL;

    }

    private void initWeekSelectionSpinner() {

        final List<String> weekSelectionSpinnerItems = new ArrayList<>();
        weekSelectionSpinnerItems.add(getResources().getString(R.string.hide_weeks));
        weekSelectionSpinnerItems.add(getResources().getString(R.string.show_weeks));

        ArrayAdapter weekAdapter = new ArrayAdapter<String>(
                mWeekSpinner.getContext(),
                android.R.layout.simple_spinner_item,
                weekSelectionSpinnerItems);

        mWeekSpinner.setAdapter(weekAdapter);

        mWeekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String spinnerValue = mWeekSpinner.getSelectedItem().toString();
                if (spinnerValue.equals(getResources().getString(R.string.hide_weeks))) {
                    mShowWeeks = false;
                } else {
                    mShowWeeks = true;
                }

                if (!mFilterByLector) {
                    mAdapter.setLectures(mResources, mLearningProgramProvider.provideLectures(), mShowWeeks);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.setLectures(mResources, mLearningProgramProvider.filterBy(mLectorName), mShowWeeks);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String spinnerValue = mWeekSpinner.getSelectedItem().toString();
        if (spinnerValue.equals(getResources().getString(R.string.hide_weeks))) {
            mShowWeeks = false;
        } else {
            mShowWeeks = true;
        }

    }

    private static class LoadLecturesFromWebTask extends AsyncTask<Void, Void, List<Lecture>> {
        private final WeakReference<LectureListFragment> mFragmentRef;
        private final LearningProgramProvider mProvider;

        private LoadLecturesFromWebTask(LectureListFragment fragment) {
            mFragmentRef = new WeakReference<>(fragment);
            mProvider = fragment.mLearningProgramProvider;
        }

        @Override
        protected List<Lecture> doInBackground(Void... voids) {
            return mProvider.downloadLectures();
        }

        @Override
        protected void onPostExecute(List<Lecture> lectures) {
            LectureListFragment fragment = mFragmentRef.get();
            if (lectures == null) {
                Toast.makeText(fragment.getContext(), R.string.loading_data_failed, Toast.LENGTH_SHORT).show();
            } else {
                fragment.initSpinner();
                fragment.initWeekSelectionSpinner();
                fragment.updateAdapter();
            }
        }
    }
}
