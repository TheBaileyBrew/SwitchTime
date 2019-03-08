package com.thebaileybrew.switchtime.adapter;

import java.util.ArrayList;
import java.util.List;

public class TaskList extends ArrayList<Task> {

    public static ArrayList<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.add(new Task("10", "Feed Bo", "Get Bo breakfast and dinner today."));
        allTasks.add(new Task("05", "Feed Bo with Travis (BONUS TIME)", "BONUS MINUTES: Help your brother get Bo food without fighting"));
        allTasks.add(new Task("10", "Clean your room", "Even though Travis may have gotten most of the toys out, clean it up as best you can"));
        allTasks.add(new Task("05", "Do your reading without being asked", "Get 15 minutes of reading in without being asked."));
        allTasks.add(new Task("10", "Clean up the living room", "Help clean up toys in the living room before dinner."));
        allTasks.add(new Task("15", "Eat your dinner without complaining", "Sometimes it's what you like, sometimes it's not... but you're gonna eat it and not complain"));
        allTasks.add(new Task("02","Help Travis remember to go potty", "Teach him to go as often as you do!"));

        return allTasks;
    }

    public static ArrayList<Task> getTravisTasks() {
        ArrayList<Task> moreTasks = new ArrayList<>();
        moreTasks.add(new Task("05", "Feed Bo with Brycen", "Don't fight"));
        moreTasks.add(new Task("10", "Pick up the living room", "Put your toys away"));
        moreTasks.add(new Task("20", "Go potty", "...and actually pee or poop"));
        moreTasks.add(new Task("05", "Take off your boots", "Try to get undressed when you get home"));

        return moreTasks;
    }
}
