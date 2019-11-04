package net.rusnet.sb.learningprogram;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.sb.learningprogram.adapters.LearningProgramAdapter;
import net.rusnet.sb.learningprogram.adapters.LectorSpinnerAdapter;
import net.rusnet.sb.learningprogram.dataprovider.LearningProgramProvider;
import net.rusnet.sb.learningprogram.models.Lecture;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int POSITION_ALL = 0;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private LearningProgramProvider mLearningProgramProvider = new LearningProgramProvider();
    private LearningProgramAdapter adapter;

    private boolean showWeeks;
    private boolean filterByLector;
    private String lectorName;

    private Resources mResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();

        mResources = getResources();

        new LoadLecturesFromWebTask(this).execute();

    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.learning_program_recycler);
        layoutManager = new LinearLayoutManager(
                this,
                RecyclerView.VERTICAL,
                false
        );
        recyclerView.setLayoutManager(layoutManager); //можно задать и в .xml
        adapter = new LearningProgramAdapter();

        recyclerView.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
    }

    private void updateAdapter() {
        adapter.setLectures(mResources, mLearningProgramProvider.provideLectures(), showWeeks);
    }

    private void initSpinner() {
        Spinner spinner = findViewById(R.id.lectors_spinner);
        List<String> lectors = mLearningProgramProvider.provideLectors();
        Collections.sort(lectors);
        lectors.add(POSITION_ALL, getResources().getString(R.string.all));


        final LectorSpinnerAdapter spinnerAdapter = new LectorSpinnerAdapter(lectors);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lectorName = "";
                List<Lecture> filteredLectures;
                if (position == POSITION_ALL) {
                    filteredLectures = mLearningProgramProvider.provideLectures();
                    filterByLector = false;
                } else {
                    lectorName = spinnerAdapter.getItem(position);
                    filteredLectures = mLearningProgramProvider.filterBy(lectorName);
                    filterByLector = true;
                }
                adapter.setLectures(mResources, filteredLectures, showWeeks);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int spinnerPosition = spinner.getSelectedItemPosition();
        filterByLector = spinnerPosition != POSITION_ALL;

    }

    private void initWeekSelectionSpinner() {
        final Spinner weekSpinner = findViewById(R.id.week_selection_spinner);
        final List<String> weekSelectionSpinnerItems = new ArrayList<>();
        weekSelectionSpinnerItems.add(getResources().getString(R.string.hide_weeks));
        weekSelectionSpinnerItems.add(getResources().getString(R.string.show_weeks));

        ArrayAdapter weekAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                weekSelectionSpinnerItems);

        weekSpinner.setAdapter(weekAdapter);

        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String spinnerValue = weekSpinner.getSelectedItem().toString();
                if (spinnerValue.equals(getResources().getString(R.string.hide_weeks))) {
                    showWeeks = false;
                } else {
                    showWeeks = true;
                }

                if (!filterByLector) {
                    adapter.setLectures(mResources, mLearningProgramProvider.provideLectures(), showWeeks);
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.setLectures(mResources, mLearningProgramProvider.filterBy(lectorName), showWeeks);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String spinnerValue = weekSpinner.getSelectedItem().toString();
        if (spinnerValue.equals(getResources().getString(R.string.hide_weeks))) {
            showWeeks = false;
        } else {
            showWeeks = true;
        }

    }

    private static class LoadLecturesFromWebTask extends AsyncTask<Void, Void, List<Lecture>> {
        private final WeakReference<MainActivity> mActivityRef;
        private final LearningProgramProvider mProvider;

        private LoadLecturesFromWebTask(MainActivity activity) {
            mActivityRef = new WeakReference<>(activity);
            mProvider = activity.mLearningProgramProvider;
        }

        @Override
        protected List<Lecture> doInBackground(Void... voids) {
            return mProvider.downloadLectures();
        }

        @Override
        protected void onPostExecute(List<Lecture> lectures) {
            MainActivity activity = mActivityRef.get();
            if (lectures == null) {
                Toast.makeText(activity, R.string.loading_data_failed, Toast.LENGTH_SHORT).show();
            } else {
                activity.initSpinner();
                activity.initWeekSelectionSpinner();
                activity.updateAdapter();
            }
        }
    }
}
