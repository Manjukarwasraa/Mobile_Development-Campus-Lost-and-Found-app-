package com.example.project1;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    private final ArrayList<Item> items;
    private final OnItemClickListener listener;

    public ItemAdapter(ArrayList<Item> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.title.setText(item.title);
        holder.type.setText(item.type);
        holder.category.setText(item.category);
        holder.date.setText(item.date);
        holder.status.setText(item.resolved == 1 ? "Resolved" : "Active");

        if (item.imageUri != null && !item.imageUri.isEmpty()) {
            try {
                holder.image.setImageURI(Uri.parse(item.imageUri));
            } catch (Exception e) {
                holder.image.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            holder.image.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, type, category, date, status;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.itemImage);
            title = itemView.findViewById(R.id.itemTitle);
            type = itemView.findViewById(R.id.itemType);
            category = itemView.findViewById(R.id.itemCategory);
            date = itemView.findViewById(R.id.itemDate);
            status = itemView.findViewById(R.id.itemStatus);
        }
    }
}
