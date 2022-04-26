package com.mxarcher.biue.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.mxarcher.biue.R;
import com.mxarcher.biue.databinding.FileCardCellBinding;

import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/26 16:13
 * @Description:
 */
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private static final String TAG = "FileInfoListAdapter";
    private final Callback callback;
    private List<String> fileList;

    public FileListAdapter(Callback callback) {
        this.callback = callback;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FileCardCellBinding binding = FileCardCellBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String filename = fileList.get(position);
        holder.binding.fileCardCellFilename.setText(filename);
        holder.binding.fileCardCellMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_file_action_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.popup_file_upload) {
                    callback.onUpload(filename);
                    return true;
                } else if (id == R.id.popup_file_delete) {
                    callback.onDelete(filename);
                    return true;
                } else return false;

            });
            popupMenu.show();
        });
        Log.d(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return fileList == null ? 0 : fileList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface Callback {
        void onUpload(String filename);

        void onDelete(String filename);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        FileCardCellBinding binding;

        public ViewHolder(@NonNull FileCardCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
