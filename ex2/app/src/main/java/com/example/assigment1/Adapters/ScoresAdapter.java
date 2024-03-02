package com.example.assigment1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assigment1.Interfaces.OnScoreClicked;
import com.example.assigment1.R;
import com.example.assigment1.Utillities.Score;

import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder> {
    private List<Score> scores;

    private OnScoreClicked onScoreClicked;
    public ScoresAdapter(List<Score> scoresSorted, OnScoreClicked onScoreClicked) {
        this.scores = scoresSorted;
        this.onScoreClicked = onScoreClicked;
    }
    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
       return new ScoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scores.get(position);
        holder.populate(score,onScoreClicked);
    }

    @Override
    public int getItemCount() {
        return scores.size();
    }

    static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTv, longitudeTv, latitudeTv, scoreTv;
        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.player_LBL_name);
            longitudeTv = itemView.findViewById(R.id.player_LBL_longitude);
            latitudeTv = itemView.findViewById(R.id.player_LBL_latitude);
            scoreTv = itemView.findViewById(R.id.player_LBL_score);
        }

        public void populate(Score score, OnScoreClicked onScoreClicked) {
            this.nameTv.setText(score.getPlayerName());
            this.longitudeTv.setText(String.valueOf(score.getLongitude()));
            this.latitudeTv.setText(String.valueOf(score.getLatitude()));
            this.scoreTv.setText(String.valueOf(score.getScore()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onScoreClicked.onScoreClicked(score);
                }
            });
        }
    }

    public void updateWith(List<Score> scores) {
        this.scores = scores;
        notifyDataSetChanged();
    }
}
