package com.mxarcher.biue.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.mxarcher.biue.R;
import com.mxarcher.biue.databinding.HandingCardCellBinding;
import com.mxarcher.biue.models.AdapterCallback;
import com.mxarcher.biue.models.Handling;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Author: MXArcher Lee
 * @Date: 2022/4/25 15:43
 * @Description:
 */
public class HandlingListAdapter extends RecyclerView.Adapter<HandlingListAdapter.ViewHolder> {
    private static final String TAG = "HandlingListAdapter";
    private List<Handling> handlingList;
    private final AdapterCallback<Handling> adapterCallback;

    public HandlingListAdapter(AdapterCallback<Handling> callback) {
        this.adapterCallback = callback;
    }
    public void setHandlingList(List<Handling>handlingList){
        this.handlingList = handlingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "-------onCreateViewHolder: ${++handlingViewCount}");
        HandingCardCellBinding binding = HandingCardCellBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Handling handling = handlingList.get(position);
        holder.binding.handlingCardCellComments.setText(handling.getComments());
        holder.binding.handlingCardCellAlgorithm.setText(handling.getAlgorithm());
        holder.binding.handlingCardCellId.setText(String.valueOf(handling.getId()));
        holder.binding.handlingCardCellName.setText(handling.getName());
        holder.binding.handlingCardCellPath.setText(handling.getPath());
        OffsetDateTime ost = OffsetDateTime.parse(handling.getUpdated_at());
        String time = ost.atZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 \n HH时mm分ss秒"));
        holder.binding.handlingCardCellTime.setText(time);
        holder.binding.handlingCardCellResults.setText(handling.getResults());
        holder.binding.handlingCardCellMore.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(item -> {

                // https://blog.csdn.net/yj82871002/article/details/109228828
                // resourceIDs will be non-final 解决
                int id = item.getItemId();
                if (id == R.id.popup_edit) {
                    adapterCallback.onEdit(handling);
                    return true;
                } else if (id == R.id.popup_delete) {
                    adapterCallback.onDelete(handling);
                    return true;
                } else {
                    return false;
                }
            });
            popupMenu.show();
        });
    }


    @Override
    public int getItemCount() {
        return handlingList == null ? 0 : handlingList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final HandingCardCellBinding binding;

        public ViewHolder(@NonNull HandingCardCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
