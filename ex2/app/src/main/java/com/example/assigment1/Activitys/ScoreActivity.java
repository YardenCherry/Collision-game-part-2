package com.example.assigment1.Activitys;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assigment1.Interfaces.OnScoreClicked;
import com.example.assigment1.R;
import com.example.assigment1.Utillities.Score;
import com.example.assigment1.Views.ListFragment;
import com.example.assigment1.Views.MapFragment;

public class ScoreActivity extends AppCompatActivity implements OnScoreClicked {


    private ListFragment listFragment;
    private MapFragment mapFragment;

    private ImageView existScoresBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_activity);
        existScoresBtn = findViewById(R.id.existScoresBtn);
        listFragment = new ListFragment();
        mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_FRAME_list,listFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.mapFragmentContainer, mapFragment).commit();

        existScoresBtn.setOnClickListener(v -> finish());
    }

    @Override
    public void onScoreClicked(Score score) {
        mapFragment.zoom(score.getLatitude(), score.getLongitude());
    }
}
