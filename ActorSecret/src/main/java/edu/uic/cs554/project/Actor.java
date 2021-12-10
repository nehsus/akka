package edu.uic.cs554.project;

/**
 * Model for an actor
 */
public class Actor {
    private String actorName;
    private String actorMsg;

    public Actor(String actorName, String actorMsg) {
        this.actorName = actorName;
        this.actorMsg = actorMsg;
    }

    public String getActorName() {
        return actorName;
    }

    public String getActorMsg() {
        return actorMsg;
    }

}
