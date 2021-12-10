package edu.uic.cs554.project;

import java.util.ArrayList;

/**
 * JavaBean model for a Group of actors
 */
public class Group {

    public String group;
    public ArrayList<Actor> actor;

    public Group() {}

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public ArrayList<Actor> getActors() {
        return actor;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actor = actors;
    }
}
