package com.domos.balance.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Task  implements Serializable {
    private String id;
    private String name;
    private String duration;
    private String creationDate;
    private int count;
    private int emoji;
    private int currentPomodoro;
    private boolean isStarted; //true = sin realizar || false = realizada (exitosa o fallida) esto lo indica el radio buton de la tarjeta de la tarea
    private boolean isSuccessful; //true = exitosa || false = fallida
    private ArrayList<String> notes;

    public Task(){}

    public Task(String id, String name, String duration, String creationDate, int count, int emoji, int currentPomodoro, boolean isStarted, boolean isSuccessful, ArrayList<String> notes) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.creationDate = creationDate;
        this.count = count;
        this.emoji = emoji;
        this.currentPomodoro = currentPomodoro;
        this.isStarted = isStarted;
        this.isSuccessful = isSuccessful;
        this.notes = notes;
    }

    public Task(String id, String name, String duration, String creationDate, int count, int emoji) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.creationDate = creationDate;
        this.count = count;
        this.emoji = emoji;
        this.isStarted = false;
        this.isSuccessful = false;
        this.currentPomodoro = 1;
        this.notes = null;
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

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        this.isStarted = started;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        this.isSuccessful = successful;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public int getCurrentPomodoro() {
        return currentPomodoro;
    }

    public void setCurrentPomodoro(int currentPomodoro) {
        this.currentPomodoro = currentPomodoro;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
