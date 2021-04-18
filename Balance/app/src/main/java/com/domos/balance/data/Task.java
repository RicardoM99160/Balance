package com.domos.balance.data;

public class Task {
    private String id;
    private String name;
    private String duration;
    private int count;
    private int emoji;
    private boolean state; //true = sin realizar || false = realizada (exitosa o fallida) esto lo indica el radio buton de la tarjeta de la tarea
    private boolean status; //true = exitosa || false = fallida
    private Pomodoro[] pomodoros;

    public Task(){}

    public Task(String id, String name, String duration, int count, int emoji, boolean state, boolean status, Pomodoro[] pomodoros) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.count = count;
        this.emoji = emoji;
        this.state = state;
        this.status = status;
        this.pomodoros = pomodoros;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getEmoji() {
        return emoji;
    }

    public void setEmoji(int emoji) {
        this.emoji = emoji;
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

    public Pomodoro[] getPomodoros() {
        return pomodoros;
    }

    public void setPomodoros(Pomodoro[] pomodoros) {
        this.pomodoros = pomodoros;
    }
}
