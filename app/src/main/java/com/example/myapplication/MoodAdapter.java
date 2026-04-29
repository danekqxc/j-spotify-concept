package com.example.myapplication;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MoodAdapter extends RecyclerView.Adapter<MoodAdapter.MoodViewHolder> {

    private List<Mood> moods;
    private OnMoodClickListener listener;
    private int selectedPosition = -1;

    public interface OnMoodClickListener {
        void onMoodClick(Mood mood);
    }

    public MoodAdapter(List<Mood> moods, OnMoodClickListener listener) {
        this.moods = moods;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mood, parent, false);
        return new MoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodViewHolder holder, int position) {
        Mood mood = moods.get(position);
        holder.title.setText(mood.getTitle());
        holder.card.setCardBackgroundColor(mood.getColor());
        
        if (mood.getIconRes() != 0) {
            holder.icon.setImageResource(mood.getIconRes());
        }

        if (selectedPosition == position) {
            holder.card.setStrokeWidth(4);
            holder.card.setStrokeColor(holder.itemView.getContext().getResources().getColor(R.color.spotify_green));
        } else {
            holder.card.setStrokeWidth(0);
        }

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);
            listener.onMoodClick(mood);
        });
    }

    @Override
    public int getItemCount() {
        return moods.size();
    }

    static class MoodViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        com.google.android.material.card.MaterialCardView card;

        public MoodViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_mood_title);
            icon = itemView.findViewById(R.id.iv_mood_icon);
            card = (com.google.android.material.card.MaterialCardView) itemView;
        }
    }
}