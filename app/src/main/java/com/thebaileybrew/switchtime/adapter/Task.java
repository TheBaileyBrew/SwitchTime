package com.thebaileybrew.switchtime.adapter;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {

    public String taskMinuteValue;
    public String taskDescription;
    public String taskExtendedDetails;

    public Task() {}

    public Task(String minutes, String description,  String details) {
        this.taskMinuteValue = minutes;
        this.taskDescription = description;
        this.taskExtendedDetails = details;
    }

    protected Task(Parcel in) {
        taskMinuteValue = in.readString();
        taskDescription = in.readString();
        taskExtendedDetails = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public void setTaskMinuteValue(String taskMinuteValue) {
        this.taskMinuteValue = taskMinuteValue;
    }

    public String getTaskMinuteValue() {
        return taskMinuteValue;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskExtendedDetails(String taskExtendedDetails) {
        this.taskExtendedDetails = taskExtendedDetails;
    }

    public String getTaskExtendedDetails() {
        return taskExtendedDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(taskMinuteValue);
        dest.writeString(taskDescription);
        dest.writeString(taskExtendedDetails);
    }
}
