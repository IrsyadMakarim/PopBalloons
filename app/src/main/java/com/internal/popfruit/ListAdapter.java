package com.internal.popfruit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private ArrayList<Items> items = new ArrayList<>();

    ListAdapter(ArrayList<Items> items){
        this.items.clear();
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvScore;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            tvScore = itemView.findViewById(R.id.leaderboardScore);
            itemView.setOnClickListener(this);
        }

        public void bind (Items items){
            tvScore.setText(items.getScore());
        }

        @Override
        public void onClick(View view) {
            Items item = items.get(getAdapterPosition());
        }
    }
}
