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
import com.mxarcher.biue.models.User;

import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/24 23:04
 * @Description:
 */
public class UserSpinnerAdapter extends ArrayAdapter<User> {
    private static final int resourceID = R.layout.user_spinner_item;

    Context context;
    List<User> userList;

    public UserSpinnerAdapter(@NonNull Context context, @NonNull List<User> objects) {
        super(context, resourceID, objects);
        this.userList = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userList == null ? 0 : userList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return userList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(resourceID, parent, false);
            vh.tv = convertView.findViewById(R.id.user_spinner_item_text);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        User user = userList.get(position);
        String show = user.getId() + ": " + user.getName();
        vh.tv.setText(show);
        return convertView;
    }

    // 没查出来这里怎么使用ViewHolder
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
        User user = userList.get(position);
        String show = user.getId() + ": " + user.getName();
        tv.setText(show);
        return tv;

    }

    static class ViewHolder {
        TextView tv;
    }
}
