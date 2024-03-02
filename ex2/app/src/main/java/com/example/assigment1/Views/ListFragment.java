package com.example.assigment1.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assigment1.Activitys.ScoreActivity;
import com.example.assigment1.Adapters.ScoresAdapter;
import com.example.assigment1.R;
import com.example.assigment1.Utillities.Score;
import com.example.assigment1.Utillities.SharedPreferencesManager;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListFragment extends Fragment {
    private RecyclerView scoreRecycler;
    SharedPreferencesManager spManager;

    public ListFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spManager = SharedPreferencesManager.getInstance(getContext());
        scoreRecycler = view.findViewById(R.id.main_LST_players);
        scoreRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        Set<Score> scoreSet = spManager.getScores();

        List<Score> sorted = scoreSet.stream()
                .sorted(Comparator.comparingInt(Score::getScore).reversed())
                .collect(Collectors.toList());
        Log.d("ListFragmnet", "List size: " + sorted.size());
        ScoresAdapter adapter = new ScoresAdapter(sorted, (ScoreActivity) getActivity());
        scoreRecycler.setAdapter(adapter);
    }
}


