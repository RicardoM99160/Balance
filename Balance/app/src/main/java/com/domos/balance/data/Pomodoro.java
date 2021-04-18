package com.domos.balance.data;

public class Pomodoro {
    private int id;
    private String duration;
    private boolean state;
    private boolean status;

    public Pomodoro(){}

    public Pomodoro(int id, String duration, boolean state, boolean status) {
        this.id = id;
        this.duration = duration;
        this.state = state;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
