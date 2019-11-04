package net.rusnet.sb.learningprogram;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.rusnet.sb.learningprogram.fragments.LectureDetailFragment;
import net.rusnet.sb.learningprogram.fragments.LectureListFragment;
import net.rusnet.sb.learningprogram.models.Lecture;

public class MainActivity extends AppCompatActivity implements LectureListFragment.OnLectureSelectedListener {

    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_placeholder, LectureListFragment.newInstance())
                .commit();

    }

    @Override
    public void onLectureSelected(Lecture lecture) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_placeholder, LectureDetailFragment.newInstance(lecture))
                .addToBackStack(null)
                .commit();
    }
}
