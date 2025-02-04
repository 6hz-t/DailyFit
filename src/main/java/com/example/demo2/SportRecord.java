package com.example.demo2;

public class SportRecord {
    private String date;
    private int time;
    private String sportType;

    public SportRecord(String date, int time, String sportType) {
        this.date = date;
        this.time = time;
        this.sportType = sportType;
    }

    public String getDate() {
        return date;
    }

    public int getTime() {
        return time;
    }

    public String getSportType() {
        return sportType;
    }
}