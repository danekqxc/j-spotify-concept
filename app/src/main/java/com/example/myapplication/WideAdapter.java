package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.RelativeCornerSize;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.List;

public class WideAdapter extends RecyclerView.Adapter<WideAdapter.ViewHolder> {

    private List<Object> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Object item);
    }

    public WideAdapter(List<Object> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_wide, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = items.get(position);
        if (item instanceof Artist) {
            Artist artist = (Artist) item;
            holder.title.setText(artist.getName());
            if (artist.getImageResId() != 0) {
                holder.image.setImageResource(artist.getImageResId());
            } else {
                holder.image.setImageResource(android.R.drawable.ic_menu_gallery);
            }
            holder.image.setShapeAppearanceModel(
                holder.image.getShapeAppearanceModel()
                    .toBuilder()
                    .setAllCornerSizes(new RelativeCornerSize(0.5f))
                    .build()
            );
        } else if (item instanceof Album) {
            Album album = (Album) item;
            holder.title.setText(album.getTitle());
            if (album.getImageResId() != 0) {
                holder.image.setImageResource(album.getImageResId());
            } else {
                holder.image.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
        
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView image;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = (ShapeableImageView) itemView.findViewById(R.id.wide_image);
            title = itemView.findViewById(R.id.wide_title);
        }
    }
}