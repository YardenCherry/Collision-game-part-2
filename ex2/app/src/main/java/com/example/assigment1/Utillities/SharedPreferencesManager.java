package com.example.assigment1.Utillities;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class SharedPreferencesManager {
    private static SharedPreferencesManager instance = null;
    private static final String DB_FILE = "com.example.assigment1";
    private SharedPreferences sharedPref;

    public void putString(String key, String value) {
        sharedPref.edit().putString(key,value).apply();
    }

    public Set<Score> getScores() {
        Set<String> scores_strings = sharedPref.getStringSet("scores", new HashSet<>());
        Set<Score> scores = new HashSet<>();
        for(String scoreString: scores_strings) {
            scores.add(Score.fromString(scoreString));
        }
        return scores;
    }
    private void saveScores(Set<Score> scores) {
        Set<String> scores_strings = new HashSet<>();
        for(Score score : scores ) {
            scores_strings.add(score.toJson());
        }
        sharedPref.edit()
                .putStringSet("scores", scores_strings)
                .apply();
    }

    public void addNewScore(Score score) {
        Set<Score> scores = getScores();
        Set<Score> toSave = new HashSet<>();
        boolean exists = false;
        for(Score someScore: scores) {
            if(someScore.getPlayerName().equals(score.getPlayerName())) {
                exists = true;
                if(someScore.getScore() < score.getScore()) {
                    toSave.add(score);
                } else {
                    toSave.add(someScore);
                }
            }else {
                toSave.add(someScore);
            }
        }
        if(!exists) {
            toSave.add(score);
        }
        saveScores(toSave);
    }
    public void putInt(String key, int value) {
        sharedPref.edit().putInt(key, value).apply();
    }


    public String getString(String key,String value){
        return sharedPref.getString(key,value);
    }

    public static SharedPreferencesManager getInstance(Context context) {
        if(instance == null)
            instance= new SharedPreferencesManager(context);
        return instance;
    }


    private SharedPreferencesManager(Context context) {
        sharedPref = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
    }

}
