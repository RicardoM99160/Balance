package com.domos.balance;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Task implements Serializable {

    String id;
    String emoji;
    String title;
    String timeMethod;
    int duration;
    boolean state;

    //Constructor sin parámetros
    public Task(){
        this.id = "T0001";
        this.emoji = "";
        this.title = "";
        this.timeMethod = "Pomodoro";
        this.duration = 2700000;
        this.state = true;
    }

    //Constructor con parametros
    public Task(String id,String emoji, String title, String timeMethod, int duration, boolean state){
        this.id = id;
        this.emoji = emoji;
        this.title = title;
        this.timeMethod = timeMethod;
        this.duration = duration;
        this.state = state;
    }


    //Set y get de todas la propiedades

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTimeMethod(String timeMethod) {
        this.timeMethod = timeMethod;
    }

    public String getTimeMethod() {
        return timeMethod;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean getState(){
        return state;
    }


}
