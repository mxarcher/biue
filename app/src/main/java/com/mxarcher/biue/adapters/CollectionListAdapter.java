package com.mxarcher.biue.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.mxarcher.biue.R;
import com.mxarcher.biue.databinding.CollectionCardCellBinding;
import com.mxarcher.biue.models.AdapterCallback;
import com.mxarcher.biue.models.Collection;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/25 11:52
 * @Description:
 */
public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.ViewHolder> {
    private static final String TAG = "CollectionListAdapter";
    List<Collection> collectionList;
    AdapterCallback<Collection> adapterCallback;

    public CollectionListAdapter(AdapterCallback<Collection> callback) {
        adapterCallback = callback;
    }

    public void setCollectionList(List<Collection> collectionList) {
        this.collectionList = collectionList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CollectionCardCellBinding binding = CollectionCardCellBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }


    // pgsql 中的Timestamp with timezone 用OffsetDateTime来进行解析

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Collection collection = collectionList.get(position);
        holder.collectionCardCellBinding.collectionCardCellId.setText(String.valueOf(collection.getId()));
        holder.collectionCardCellBinding.collectionCardCellUid.setText(String.valueOf(collection.getUid()));
        holder.collectionCardCellBinding.collectionCardCellName.setText(collection.getName());
        OffsetDateTime ost = OffsetDateTime.parse(collection.getUpdated_at());
        String time = ost.atZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 \n HH时mm分ss秒"));
        holder.collectionCardCellBinding.collectionCardCellTime.setText(time);
        holder.collectionCardCellBinding.collectionCardCellLocation.setText(collection.getLocation());
        holder.collectionCardCellBinding.collectionCardProgram.setText(collection.getProgram());
        holder.collectionCardCellBinding.collectionCardCellComments.setText(collection.getComments());
        holder.collectionCardCellBinding.collectionCardCellPath.setText(collection.getPath());
        holder.collectionCardCellBinding.collectionCardCellMore.setOnClickListener(view -> {
            Log.d(TAG, "onBindViewHolder: enter");
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(item -> {

                // https://blog.csdn.net/yj82871002/article/details/109228828
                // resourceIDs will be non-final 解决
                int id = item.getItemId();
                if (id == R.id.popup_edit) {
                    adapterCallback.onEdit(collection);
                    return true;
                } else if (id == R.id.popup_delete) {
                    adapterCallback.onDelete(collection);
                    return true;
                } else {
                    return false;
                }
            });
            popupMenu.show();
        });
    }

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
        return collectionList == null ? 0 : collectionList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CollectionCardCellBinding collectionCardCellBinding;

        public ViewHolder(@NonNull CollectionCardCellBinding collectionCardCellBinding) {
            super(collectionCardCellBinding.getRoot());
            this.collectionCardCellBinding = collectionCardCellBinding;
        }
    }
}
