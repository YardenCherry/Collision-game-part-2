package com.example.assigment1.Utillities;

import com.google.gson.Gson;

public class Score {

    private String playerName;
    private int score;
    private double latitude, longitude;

    public Score(String playerName, int score, double latitude, double longitude) {
        this.playerName = playerName;
        this.score = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public static Score fromString(String s) {
        Gson g = new Gson();
        return g.fromJson(s, Score.class);
    }
    public String toJson() {
        Gson g = new Gson();
        return g.toJson(this);
    }

    public String getPlayerName() {
        return playerName;
    }

    public Score setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public int getScore() {
        return score;
    }

    public Score setScore(int score) {
        this.score = score;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Score setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Score setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}
