package net.rusnet.sb.learningprogram.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class LectorSpinnerAdapter extends BaseAdapter {

    //
    private List<String> mLector;

    public LectorSpinnerAdapter(@NonNull List<String> lector) {
        this.mLector = new ArrayList<>(lector);
    }

    @Override
    public int getCount() { //возвращает количество элементов в адаптере
        return mLector.size();
    }

    @Override
    public String getItem(int position) { //меняем Object на String т.к. список стрингов
        return mLector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    android.R.layout.simple_list_item_1,
                    parent,
                    false
            );
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.mLectorName.setText(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        private final TextView mLectorName;

        private ViewHolder(View view) {
            mLectorName = view.findViewById(android.R.id.text1);
        }
    }

}
