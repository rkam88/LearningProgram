package net.rusnet.sb.learningprogram.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class LectureSubtopicsAdapter
        extends RecyclerView.Adapter<LectureSubtopicsAdapter.ViewHolder> {

    private String[] mSubtopics;

    public LectureSubtopicsAdapter(String[] subtopics) {
        mSubtopics = Arrays.copyOf(subtopics, subtopics.length);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View lectureSubtopicView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(lectureSubtopicView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String subtopic = mSubtopics[position];
        TextView textView = holder.mLectureSubtopicTextView;
        textView.setText(subtopic);
    }

    @Override
    public int getItemCount() {
        return mSubtopics.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mLectureSubtopicTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mLectureSubtopicTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
