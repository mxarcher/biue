package com.mxarcher.biue.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.mxarcher.biue.R;
import com.mxarcher.biue.databinding.UserCardCellBinding;
import com.mxarcher.biue.models.AdapterCallback;
import com.mxarcher.biue.models.User;

import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/24 18:49
 * @Description: 使用回调把viewModel对数据的操作回到fragment上 此时就不需要在adapter中加入viewModel了
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private static final String TAG = "UserListAdapter";
    private final AdapterCallback<User> callback;
    private List<User> userList;

    public UserListAdapter(AdapterCallback<User> callback) {
        this.callback = callback;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserCardCellBinding binding = UserCardCellBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.binding.userCardCellId.setText(String.valueOf(user.getId()));
        holder.binding.userCardCellName.setText(user.getName());
        holder.binding.userCardCellSex.setText(user.isSex() ? "男" : "女");
        holder.binding.userCardCellHeight.setText(String.valueOf(user.getHeight()));
        holder.binding.userCardCellWeight.setText(String.valueOf(user.getWeight()));
        holder.binding.userCardCellComments.setText(user.getComments());
        holder.binding.userCardCellMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.popup_edit) {
                    callback.onEdit(user);
                    return true;
                } else if (id == R.id.popup_delete) {
                    callback.onDelete(user);
                    return true;
                } else {
                    return false;
                }
            });
            popupMenu.show();

        });
        Log.d(TAG, "onBindViewHolder: " + position + "  " + userList.get(position).getId() + userList.get(position).getName());
    }

    // 不加getItemId和getIteViewType可能会出现显示错误
    // https://stackoverflow.com/questions/53003175/i-have-a-problem-recyclerview-showing-wrong-data-when-scrolling
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final UserCardCellBinding binding;

        public ViewHolder(@NonNull UserCardCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}