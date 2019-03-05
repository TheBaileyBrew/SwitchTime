package com.thebaileybrew.switchtime.adapter;

public class Task {

    public String taskMinuteValue;
    public String taskDescription;
    public String taskExtendedDetails;

    public Task() {}

    public Task(String minutes, String description,  String details) {
        this.taskMinuteValue = minutes;
        this.taskDescription = description;
        this.taskExtendedDetails = details;
    }

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
}
