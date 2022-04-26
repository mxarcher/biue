package com.mxarcher.biue.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mxarcher.biue.R;
import com.mxarcher.biue.models.Collection;

import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/26 19:48
 * @Description:
 */
public class CollectionSpinnerAdapter extends ArrayAdapter<Collection> {
    private static final int resourceID = R.layout.collection_spinner_item;
    List<Collection> collectionList;
    Context context;


    public CollectionSpinnerAdapter(@NonNull Context context, @NonNull List<Collection> objects) {
        super(context, resourceID, objects);
        this.collectionList = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return collectionList == null ? 0 : collectionList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // 这是显示主要行的
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceID, parent, false);
            vh.tv = convertView.findViewById(R.id.collection_spinner_item_text);

        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Collection collection = collectionList.get(position);
        // 显示ID+方案
        String show = collection.getId() + ": " + collection.getProgram();
        vh.tv.setText(show);
        return convertView;
    }

    // 这是显示具体行的
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
        Collection collection = collectionList.get(position);
        // 显示ID+方案
        String show = collection.getId() + ": " + collection.getProgram();
        tv.setText(show);
        return tv;
    }

    static class ViewHolder {
        TextView tv;
    }
}
